package com.app.clearsolutions.controller.exceptionHandler;

import com.app.clearsolutions.model.dto.ExceptionResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * Global exception handler for Spring MVC controllers.
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
    /**
     * Handles DataIntegrityViolationException when attempting to insert duplicate data.
     * @param exception DataIntegrityViolationException instance.
     * @return ResponseEntity with an error http status, custom message and name.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleDataUniquenessFailure(DataIntegrityViolationException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                "User with such an email already exists!", exception.getClass().getSimpleName());
    }

    /**
     * Handles IllegalArgumentException when providing illegal arguments.
     * @param exception DataIntegrityViolationException instance.
     * @return ResponseEntity with an error http status, message and name.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleIllegalArgumentException(IllegalArgumentException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    /**
     * Handles IllegalArgumentException when attempting to access null value.
     * @param exception DataIntegrityViolationException instance.
     * @return ResponseEntity with an error http status, message and name.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleNullPointerException(NullPointerException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    /**
     * Handles RuntimeException when having internal server error.
     * @param exception DataIntegrityViolationException instance.
     * @return ResponseEntity with an error http status, message and name.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleRuntimeExceptions(RuntimeException exception) {
        return provideResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    /**
     * Handles RuntimeException when passing data fails validation.
     * @param exception DataIntegrityViolationException instance.
     * @return ResponseEntity with an error http status, message and name.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleValidationException(HandlerMethodValidationException exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                "Validation failure.", exception.getClass().getSimpleName());
    }

    /**
     * Handles all other exceptions in order to receive them in JSON format.
     * @param exception Exception instance.
     * @return ResponseEntity with a custom error message.
     */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> handleException(Exception exception) {
        return provideResponseEntity(HttpStatus.BAD_REQUEST,
                exception.getMessage(), exception.getClass().getSimpleName());
    }

    /**
     * Helper method to construct ResponseEntity with an ExceptionResponseDTO.
     * @param status HTTP status code.
     * @param message Error message.
     * @param simpleName Simple name of the exception class.
     * @return ResponseEntity containing ExceptionResponseDTO.
     */
    private ResponseEntity<ExceptionResponseDTO> provideResponseEntity(HttpStatus status,
                                                                       String message, String simpleName) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(status.value(), message, simpleName);

        return new ResponseEntity<>(responseDTO, status);
    }
}
