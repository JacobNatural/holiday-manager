package com.app.controller.exceptionsHandler;

import com.app.controller.dto.ResponseDto;
import com.app.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for validation-related exceptions.
 * Handles ValidationException and IllegalArgumentException by returning
 * a 400 BAD REQUEST status with the exception message.
 * Catches all other exceptions to return a 500 INTERNAL SERVER ERROR status.
 */
@RestControllerAdvice
@Slf4j
public class GlobalValidExceptionHandler {

    /**
     * Handles validation and illegal argument exceptions.
     * Returns HTTP 400 BAD REQUEST with the exception message.
     *
     * @param e the validation or illegal argument exception
     * @return response containing the exception message
     */
    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<String> illegalArgumentException(Exception e) {
        log.error("Invalid arguments: ", e);
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles any other uncaught exceptions.
     * Returns HTTP 500 INTERNAL SERVER ERROR with a generic error message.
     *
     * @param e the general exception
     * @return response indicating an internal server error occurred
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<String> exception(Exception e) {
        log.error("Internal server error: ", e);
        return new ResponseDto<>("Internal server error.");
    }
}
