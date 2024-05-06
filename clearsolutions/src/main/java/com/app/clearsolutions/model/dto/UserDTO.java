package com.app.clearsolutions.model.dto;

import com.app.clearsolutions.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing user data.
 * This DTO encapsulates user information for data transfer between layers of the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    /**
     * Converts a User entity to a UserDTO object.
     *
     * @param user The User entity to be converted.
     * @return A UserDTO object representing the provided User entity.
     */
    public static UserDTO getDTOFromUser(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getBirthDate(), user.getAddress(), user.getPhoneNumber());
    }
}
