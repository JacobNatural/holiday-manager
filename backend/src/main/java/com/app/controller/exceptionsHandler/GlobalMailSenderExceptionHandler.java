package com.app.controller.exceptionsHandler;

import com.app.controller.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for email sending related exceptions.
 * Catches various Spring Mail exceptions and returns appropriate HTTP responses
 * with standardized error messages.
 */
@RestControllerAdvice
@Slf4j
public class GlobalMailSenderExceptionHandler {

    /**
     * Handles SMTP authentication failures.
     * Returns HTTP 401 UNAUTHORIZED.
     *
     * @param e the MailAuthenticationException indicating SMTP auth failure
     * @return a response with an authentication failure message
     */
    @ExceptionHandler({
            MailAuthenticationException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDto<String> handleMailAuthException(Exception e) {
        log.error("SMTP authentication failed: {}", e);
        return new ResponseDto<>("Email service authentication failed. Please check SMTP credentials.");
    }

    /**
     * Handles failures in sending email to one or more recipients.
     * Returns HTTP 424 FAILED DEPENDENCY.
     *
     * @param e the MailSendException containing failed messages details
     * @return a response indicating partial email send failure
     */
    @ExceptionHandler(MailSendException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public ResponseDto<String> handleMailSendException(MailSendException e) {
        log.error("Email sending failed to some recipients: {}", e.getFailedMessages());
        return new ResponseDto<>("Failed to send email to some recipients");
    }

    /**
     * Handles email message preparation or parsing errors, typically due to invalid message format.
     * Returns HTTP 400 BAD REQUEST.
     *
     * @param e the MailPreparationException or MailParseException describing validation failure
     * @return a response with details about the invalid email message
     */
    @ExceptionHandler({MailPreparationException.class, MailParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<String> handleMailValidationException(MailPreparationException e) {
        log.warn("Email validation failed: {}", e);
        return new ResponseDto<>("Invalid email message: " + e.getMessage());
    }

    /**
     * Handles all other generic MailExceptions not covered by specific handlers.
     * Returns HTTP 500 INTERNAL SERVER ERROR.
     *
     * @param e the generic MailException
     * @return a response indicating an internal email service error
     */
    @ExceptionHandler(MailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<String> handleGenericMailException(MailException e) {
        log.error("Email processing error: {}", e);
        return new ResponseDto<>("Internal email service error");
    }
}
