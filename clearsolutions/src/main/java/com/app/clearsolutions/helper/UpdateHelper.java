package com.app.clearsolutions.helper;

import com.app.clearsolutions.model.User;
import com.app.clearsolutions.model.dto.UserDTO;

/**
 * Helper interface to assist with updating User entities based on UserDTO objects.
 */
public interface UpdateHelper {
    void userPatcher(User existingUser, UserDTO newUser) throws IllegalAccessException, NoSuchFieldException;

    boolean checkIfFieldsAreNonNull(UserDTO userDTO) throws IllegalAccessException;
}
