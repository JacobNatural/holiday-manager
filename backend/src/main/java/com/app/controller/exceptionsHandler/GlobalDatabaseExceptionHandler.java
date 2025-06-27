package com.app.controller.exceptionsHandler;

import com.app.controller.dto.ResponseDto;
import com.app.exception.ResourceAlreadyExistException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for database-related exceptions across all controllers.
 * Handles common persistence exceptions and returns appropriate HTTP status codes
 * and standardized error responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalDatabaseExceptionHandler {

    /**
     * Handles the case when a requested entity is not found in the database.
     * Returns HTTP 404 NOT FOUND.
     *
     * @param e the EntityNotFoundException thrown by JPA
     * @return a response containing the error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto<String> entityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity not found: ", e);
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles data integrity violations such as unique constraint breaches.
     * Returns HTTP 409 CONFLICT.
     *
     * @param e the exception representing data integrity violation
     * @return a generic error message about data integrity violation
     */
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDto<String> handleDataIntegrityViolation(Exception e) {
        log.error("Data Integrity violation: ", e);
        return new ResponseDto<>("Data Integrity violation.");
    }

    /**
     * Handles exceptions related to optimistic and pessimistic locking failures,
     * including deadlocks or inability to acquire locks.
     * Returns HTTP 409 CONFLICT.
     *
     * @param e the exception indicating locking conflict
     * @return a user-friendly message advising to retry the operation
     */
    @ExceptionHandler({
            ObjectOptimisticLockingFailureException.class,
            PessimisticLockingFailureException.class,
            CannotAcquireLockException.class,
            DeadlockLoserDataAccessException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDto<String> handleLockingExceptions(Exception e) {
        log.warn("Concurrent modification detected: ", e);
        return new ResponseDto<>("Operation conflicted with another request. Please retry.");
    }

    /**
     * Handles transaction-related exceptions that occur when a transaction fails.
     * Returns HTTP 500 INTERNAL SERVER ERROR.
     *
     * @param e the TransactionSystemException thrown on transaction failure
     * @return a generic transaction failure message
     */
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<String> handleTransactionException(TransactionSystemException e) {
        log.error("Transaction failed: ", e);
        return new ResponseDto<>("Transaction failed.");
    }

    /**
     * Handles exceptions related to JPA system errors.
     * Returns HTTP 500 INTERNAL SERVER ERROR.
     *
     * @param e the JpaSystemException representing JPA system errors
     * @return a generic database operation error message
     */
    @ExceptionHandler(JpaSystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<String> handleJpaSystemException(JpaSystemException e) {
        log.error("Jpa system exception: ", e);
        return new ResponseDto<>("Database operation error.");
    }

    /**
     * Handles the case when a resource that is attempted to be created already exists.
     * Returns HTTP 409 CONFLICT.
     *
     * @param e the ResourceAlreadyExistException indicating resource duplication
     * @return the error message from the exception
     */
    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseDto<String> handleResourceAlreadyExistException(ResourceAlreadyExistException e) {
        log.error("Resource already exist: ", e);
        return new ResponseDto<>(e.getMessage());
    }

    /**
     * Handles generic database access exceptions not covered by more specific handlers.
     * Returns HTTP 401 UNAUTHORIZED.
     *
     * @param e the database access exception
     * @return a generic message indicating database operation failure
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<String> dataBaseException(Exception e) {
        log.error("Database operation failed: ", e);
        return new ResponseDto<>("Database operation failed.");
    }
}
