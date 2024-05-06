package com.app.clearsolutions.service.impl;

import com.app.clearsolutions.exception.InvalidInputFormatException;
import com.app.clearsolutions.helper.UpdateHelper;
import com.app.clearsolutions.model.User;
import com.app.clearsolutions.model.dto.BirthDateRangeDTO;
import com.app.clearsolutions.model.dto.DataDTO;
import com.app.clearsolutions.model.dto.Pagination;
import com.app.clearsolutions.model.dto.UserDTO;
import com.app.clearsolutions.repository.UserRepository;
import com.app.clearsolutions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Service implementation for managing user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Environment environment;
    private final UpdateHelper updateHelper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, Environment environment, UpdateHelper updateHelper) {
        this.userRepository = userRepository;
        this.environment = environment;
        this.updateHelper = updateHelper;
    }

    /**
     * Creates a new user based on provided user DTO.
     *
     * @param userDTO      The user DTO containing user information.
     * @param uriBuilder   The URI components builder for creating resource URIs.
     * @return ResponseEntity with HTTP status indicating success or failure.
     * @throws InvalidInputFormatException If the input data is invalid.
     */
    @Override
    public ResponseEntity<Void> createUser(UserDTO userDTO, UriComponentsBuilder uriBuilder)
            throws InvalidInputFormatException {
        checkUserDTO(userDTO); // Check if userDTO contains valid data
        if (userDTO.getBirthDate() != null) {
            checkUserAge(userDTO.getBirthDate()); // Check user age
        }

        User user;

        try {
            user = userRepository.save(User.getUserFromDTO(userDTO));
        } catch (NullPointerException e) {
            throw new InvalidInputFormatException("User cannot be created without required fields " +
                    "(email, firstName, lastName, birthDate).");
        }


        URI uriLocation = uriBuilder.path("/api/users/{id}") // Build URI for created user
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(uriLocation).build();
    }

    /**
     * Retrieves all users with optional pagination and filtering by birth date range.
     *
     * @param from     The start date of the birth date range filter.
     * @param to       The end date of the birth date range filter.
     * @param pageable The pagination information.
     * @return ResponseEntity containing a list of users and pagination details.
     */
    @Override
    public ResponseEntity<DataDTO> getUsers(String from, String to, Pageable pageable) {
        Page<User> usersPage;

        if (from.isEmpty() && to.isEmpty()) { // Retrieve all users with pagination
            usersPage = userRepository.findUsersByIsActiveIsTrue(pageable);
        } else {
            try { // Retrieve users within specified birth date range with pagination
                usersPage = getUsersByBirthDateRange(new BirthDateRangeDTO(from, to), pageable);
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("Wrong date format. Example of correct format: '2000-01-01'");
            }
        }
        DataDTO usersData = new DataDTO(new Pagination(usersPage.getTotalElements(), usersPage.getTotalPages()),
                usersPage.getContent().stream()
                        .map(UserDTO::getDTOFromUser)
                        .map(u -> (Object)u)
                        .toList());

        return ResponseEntity.ok(usersData);
    }

    /**
     * Helper method which retrieves users based on provided birth date range parameters.
     *
     * @param birthDateRangeDTO The birth date range DTO containing 'from' and 'to' dates.
     * @param pageable          The pagination information.
     * @return A page of active users within the specified birth date range.
     * @throws IllegalArgumentException If the 'from' date is after the 'to' date.
     */
    public Page<User> getUsersByBirthDateRange(BirthDateRangeDTO birthDateRangeDTO, Pageable pageable) {
        // Check if 'from' date is before 'to' date
        if (birthDateRangeDTO.getFrom() != null && birthDateRangeDTO.getTo() != null &&
                birthDateRangeDTO.getFrom().isAfter(birthDateRangeDTO.getTo())) {
            throw new IllegalArgumentException("Chronology failure. 'From' should be earlier than 'to'!");
        }

        Page<User> usersPage;

        // Retrieve users based on specified birth date range
        if(birthDateRangeDTO.getFrom() == null) {
            usersPage = userRepository.findUsersByBirthDateBeforeAndIsActiveIsTrue(birthDateRangeDTO.getTo(), pageable);
        } else if(birthDateRangeDTO.getTo() == null) {
            usersPage = userRepository.findUsersByBirthDateAfterAndIsActiveIsTrue(birthDateRangeDTO.getFrom(), pageable);
        } else {
            usersPage = userRepository.findUsersByBirthDateBetweenAndIsActiveIsTrue(
                    birthDateRangeDTO.getFrom(), birthDateRangeDTO.getTo(), pageable);
        }

        return usersPage;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user data.
     */
    @Override
    public ResponseEntity<DataDTO> getUserById(long id) {
        User user = userRepository.findUserById(id);

        if (!user.getIsActive()) {
            throw new NullPointerException("User with such an id has been deleted.");
        }

        return ResponseEntity.ok(new DataDTO(UserDTO.getDTOFromUser(user)));
    }

    /**
     * Deletes a user by ID (sets isActive to false).
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity indicating success or failure.
     */
    @Override
    public ResponseEntity<Void> deleteUser(long id) {
        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new NullPointerException("There is no user with such an id.");
        }

        user.setActive(false); // Set user as inactive (deleted)
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates all fields of a user with the provided user DTO.
     *
     * @param userDTO The user DTO containing updated user information.
     * @param id      The ID of the user to update.
     * @return ResponseEntity indicating success or failure.
     * @throws InvalidInputFormatException If the input data is invalid.
     */
    @Override
    public ResponseEntity<Void> updateAllUserFields(UserDTO userDTO, long id)
            throws InvalidInputFormatException {
        checkUserDTO(userDTO); // Check if userDTO contains valid data
        if (userDTO.getBirthDate() != null) {
            checkUserAge(userDTO.getBirthDate());
        }
        User user = userRepository.findUserById(id); // Check user age
        userDTO.setId(id);

        if (!user.getIsActive()) {
            throw new NullPointerException("User with such an id has been deleted.");
        }

        try {
            if (!updateHelper.checkIfFieldsAreNonNull(userDTO)) { // Check if all fields are specified
                throw new IllegalArgumentException("You need to specify all the fields to update with 'put'.");
            }
            userRepository.save(User.getUserFromDTO(userDTO));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Partially updates a user with the provided user DTO.
     *
     * @param userDTO The user DTO containing updated user information.
     * @param id      The ID of the user to update.
     * @return ResponseEntity indicating success or failure.
     * @throws InvalidInputFormatException If the input data is invalid.
     */
    @Override
    public ResponseEntity<Void> partialUpdateUser(UserDTO userDTO, long id)
            throws InvalidInputFormatException {
        if (userDTO.getBirthDate() != null) {
            checkUserAge(userDTO.getBirthDate()); // Check user age
        }
        checkUserDTO(userDTO); // Check if userDTO contains valid data

        User user = userRepository.findUserById(id);
        userDTO.setId(id);

        if (!user.getIsActive()) {
            throw new NullPointerException("User with such an id has been deleted.");
        }

        try {
            updateHelper.userPatcher(user, userDTO); // Partially update user fields
            userRepository.save(user);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Checks if the user's age meets the threshold defined in the environment properties.
     *
     * @param birthDate The birth date of the user.
     * @throws IllegalArgumentException If the user's age is below the defined threshold.
     */
    private void checkUserAge(LocalDate birthDate) throws IllegalArgumentException {
        int ageThreshold = Integer.parseInt(Objects.
                requireNonNull(environment.getProperty("age-threshold")));
        if (Period.between(birthDate, LocalDate.now()).getYears() < ageThreshold
        ) {
            throw new IllegalArgumentException(String
                    .format("Users must be over %d years old!", ageThreshold));
        }
    }

    /**
     * Validates the fields of the user DTO based on regex patterns.
     *
     * @param userDTO The user DTO to validate.
     * @throws InvalidInputFormatException If the input data does not match the expected format.
     */
    private void checkUserDTO(UserDTO userDTO) throws InvalidInputFormatException {
        String nameRegex = "^[\\p{L}\\s]+$",
                emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                phoneRegex = "^\\+\\d{5,13}$";

        if (userDTO.getFirstName() != null && !Pattern.matches(nameRegex, userDTO.getFirstName()) ||
                userDTO.getLastName() != null && !Pattern.matches(nameRegex, userDTO.getLastName()) ||
                (userDTO.getEmail() != null && !Pattern.matches(emailRegex, userDTO.getEmail())) ||
                (userDTO.getPhoneNumber() != null && !Pattern.matches(phoneRegex, userDTO.getPhoneNumber()))) {
            throw new InvalidInputFormatException("Invalid format of input data.");
        }
    }
}
