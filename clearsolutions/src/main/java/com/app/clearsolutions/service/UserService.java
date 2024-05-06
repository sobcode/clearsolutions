package com.app.clearsolutions.service;

import com.app.clearsolutions.exception.InvalidInputFormatException;
import com.app.clearsolutions.model.dto.DataDTO;
import com.app.clearsolutions.model.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {
    ResponseEntity<Void> createUser(UserDTO userDTO, UriComponentsBuilder uriBuilder) throws InvalidInputFormatException;

    ResponseEntity<DataDTO> getUsers(String from, String to, Pageable pageable);

    ResponseEntity<DataDTO> getUserById(long id);

    ResponseEntity<Void> deleteUser(long id);

    ResponseEntity<Void> updateAllUserFields(UserDTO userDTO, long id) throws InvalidInputFormatException;

    ResponseEntity<Void> partialUpdateUser(UserDTO userDTO, long id) throws InvalidInputFormatException;
}
