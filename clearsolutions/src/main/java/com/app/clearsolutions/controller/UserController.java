package com.app.clearsolutions.controller;

import com.app.clearsolutions.exception.InvalidInputFormatException;
import com.app.clearsolutions.model.dto.DataDTO;
import com.app.clearsolutions.model.dto.UserDTO;
import com.app.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller class responsible for handling user-related HTTP requests.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to create a new user.
     * @param userDTO The UserDTO object containing user data.
     * @param uriBuilder UriComponentsBuilder for building the response URI.
     * @return ResponseEntity indicating success or failure of the operation.
     * @throws InvalidInputFormatException If the format of input data is invalid.
     */
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDTO userDTO,
                                           UriComponentsBuilder uriBuilder) throws InvalidInputFormatException {
        return userService.createUser(userDTO, uriBuilder);
    }

    /**
     * Endpoint to retrieve a paginated list of users, optionally filtered by birth date range.
     * Users can be filtered by specifying both "from" and "to" parameters, either one of them,
     * or without any parameter. Pagination is supported using the provided Pageable object.
     * @param from Starting date for filtering users.
     * @param to Ending date for filtering users.
     * @param pageable Pageable object for pagination.
     * @return ResponseEntity containing the list of users and pagination information.
     */
    @GetMapping
    public ResponseEntity<DataDTO> readUsers(@RequestParam(defaultValue = "") String from,
                                             @RequestParam(defaultValue = "") String to,
                                             Pageable pageable) {
        return userService.getUsers(from, to, pageable);
    }

    /**
     * Endpoint to retrieve a user by ID.
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the user data.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataDTO> readUserBuId(@PathVariable @Valid @Min(1) long id) {
        return userService.getUserById(id);
    }

    /**
     * Endpoint to delete a user by ID (set isActive to false).
     * @param id The ID of the user to delete.
     * @return ResponseEntity indicating success or failure of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid @Min(1) long id) {
        return userService.deleteUser(id);
    }

    /**
     * Endpoint to update a user's information (all the fields).
     * @param id The ID of the user to update.
     * @param userDTO The UserDTO object containing updated user data.
     * @return ResponseEntity indicating success or failure of the operation.
     * @throws InvalidInputFormatException If the input format is invalid.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable @Valid @Min(1) long id,
                                           @RequestBody UserDTO userDTO) throws InvalidInputFormatException {
        return userService.updateAllUserFields(userDTO, id);
    }

    /**
     * Endpoint to partially update a user's information (one/some fields).
     * @param id The ID of the user to update.
     * @param userDTO The UserDTO object containing partial user data.
     * @return ResponseEntity indicating success or failure of the operation.
     * @throws InvalidInputFormatException If the input format is invalid.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> partialUpdateUser(@PathVariable @Valid @Min(1) long id,
                                                  @RequestBody UserDTO userDTO) throws InvalidInputFormatException {
        return userService.partialUpdateUser(userDTO, id);
    }
}
