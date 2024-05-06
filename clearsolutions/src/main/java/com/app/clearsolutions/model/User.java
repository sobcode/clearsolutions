package com.app.clearsolutions.model;

import com.app.clearsolutions.model.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

/**
 * Entity class representing a user.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", unique = true)
    @NonNull
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format!")
    private String email;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Column(name = "birth_date")
    @NonNull
    @Past(message = "Date of birth should be in the past")
    private LocalDate birthDate;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_active")
    private boolean isActive = true;

    /**
     * Creates a User object from a UserDTO object.
     *
     * @param userDTO The UserDTO object containing user data.
     * @return A User object created from the provided UserDTO.
     */
    public static User getUserFromDTO(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getEmail(), userDTO.getFirstName(),
                userDTO.getLastName(), userDTO.getBirthDate(), userDTO.getAddress(),
                userDTO.getPhoneNumber(), true);
    }

    public boolean getIsActive() {
        return isActive;
    }
}
