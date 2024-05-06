package com.app.clearsolutions.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for representing exception responses.
 * This DTO encapsulates status code, error message, and exception name for handling exceptions in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private int status;
    private String message;
    private String exceptionName;
}
