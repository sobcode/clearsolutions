package com.app.clearsolutions.exception;

/**
 * Exception thrown to indicate that input data does not conform to the expected
 * format or validation rules.
 */
public class InvalidInputFormatException extends Exception{
    public InvalidInputFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidInputFormatException(String msg) {
        super(msg);
    }
}
