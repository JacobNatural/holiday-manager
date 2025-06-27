package com.app.controller.exceptionsHandler;

import com.app.controller.dto.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.core.AuthenticationException;

import java.security.SignatureException;

/**
 * Global exception handler for security and authentication related exceptions.
 * Handles JWT exceptions, authentication failures, access denial,
 * and account status issues, returning appropriate HTTP statuses and messages.
 */
@RestControllerAdvice
@Slf4j
public class GlobalSecurityExceptionHandler {

    /**
     * Handles invalid or expired JWT token exceptions.
     * Returns HTTP 401 UNAUTHORIZED with a message about invalid token.
     *
     * @param e the exception indicating JWT token error
     * @return response containing authentication failure message
     */
    @ExceptionHandler({
            ExpiredJwtException.class, MalformedJwtException.class,
            UnsupportedJwtException.class, SignatureException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<String> unauthorizedException(Exception e) {
        log.warn("Invalid token: ", e);
        return new ResponseDto<>("Authentication failed: Invalid token");
    }

    /**
     * Handles general authentication failures such as bad credentials,
     * missing credentials, or authentication service errors.
     * Returns HTTP 401 UNAUTHORIZED.
     *
     * @param e the authentication exception
     * @return response indicating authentication failure
     */
    @ExceptionHandler({
            BadCredentialsException.class,
            AuthenticationCredentialsNotFoundException.class,
            AuthenticationServiceException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<String> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication failed: ", e);
        return new ResponseDto<>("Authentication failed.");
    }

    /**
     * Handles access denied exceptions when authenticated users
     * try to access resources without sufficient permissions.
     * Returns HTTP 403 FORBIDDEN.
     *
     * @param e the access denied exception
     * @return response indicating access is denied
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDto<String> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: ", e);
        return new ResponseDto<>("Access denied");
    }

    /**
     * Handles exceptions related to user account status such as disabled,
     * locked, expired accounts, or expired credentials.
     * Returns HTTP 403 FORBIDDEN.
     *
     * @param e the authentication exception related to account status
     * @return response indicating an account issue
     */
    @ExceptionHandler({
            DisabledException.class,
            LockedException.class,
            AccountExpiredException.class,
            CredentialsExpiredException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDto<String> handleAccountStatusExceptions(AuthenticationException e) {
        log.warn("Account status exception: ", e);
        return new ResponseDto<>("Account issue.");
    }
}
