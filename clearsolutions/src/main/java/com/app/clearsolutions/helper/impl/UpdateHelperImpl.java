package com.app.clearsolutions.helper.impl;

import com.app.clearsolutions.helper.UpdateHelper;
import com.app.clearsolutions.model.User;
import com.app.clearsolutions.model.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Helper implementation to assist with updating User entities based on UserDTO objects.
 */
@Component
public class UpdateHelperImpl implements UpdateHelper {
    /**
     * Updates fields of an existing User object with non-null values from a UserDTO object.
     * The 'id' field is excluded from the update.
     *
     * @param existingUser The existing User object to be updated.
     * @param newUser      The UserDTO object containing new values.
     * @throws IllegalAccessException If access to a field is denied.
     * @throws NoSuchFieldException   If a field is not found.
     */
    public void userPatcher(User existingUser, UserDTO newUser)
            throws IllegalAccessException, NoSuchFieldException {
        Class<?> userDTOClass = UserDTO.class;
        Field[] userDTOFields = userDTOClass.getDeclaredFields();

        for(Field field : userDTOFields) {
            field.setAccessible(true);

            Object value = field.get(newUser);
            if(value != null && !field.getName().equals("id")) {
                Field userField = User.class.getDeclaredField(field.getName());
                userField.setAccessible(true);

                userField.set(existingUser, value);

                userField.setAccessible(false);
            }

            field.setAccessible(false);
        }
    }

    /**
     * Checks if all fields of a UserDTO object are non-null, except for the 'id' field.
     *
     * @param userDTO The UserDTO object to be checked.
     * @return true if all fields (except 'id') are non-null, false otherwise.
     * @throws IllegalAccessException If access to a field is denied.
     */
    public boolean checkIfFieldsAreNonNull(UserDTO userDTO) throws IllegalAccessException {
        Class<?> userClass = UserDTO.class;
        Field[] userFields = userClass.getDeclaredFields();

        for(Field field : userFields) {
            field.setAccessible(true);

            Object value = field.get(userDTO);
            if(value == null && !field.getName().equals("id")) {
                return false;
            }

            field.setAccessible(false);
        }
        return true;
    }
}
