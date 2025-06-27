package com.app.validate;

import com.app.controller.dto.user.EmailDto;
import com.app.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validator implementation for EmailDto objects.
 * Validates the email field against a configured regex pattern.
 */
@Component
public class EmailDtoValidator implements Validator<EmailDto> {

    private final String emailRegex;

    /**
     * Constructs an EmailDtoValidator with the provided email regex pattern.
     *
     * @param emailRegex regex pattern for validating email format
     */
    public EmailDtoValidator(@Value("${validate.regex.email}") String emailRegex) {
        this.emailRegex = emailRegex;
    }

    /**
     * Validates the given EmailDto object.
     * Checks if the DTO is null and if the email field matches the email regex pattern.
     * Collects errors and throws ValidationException if any validation fails.
     *
     * @param emailDto the EmailDto object to validate
     * @throws ValidationException if validation errors are found
     */
    @Override
    public void validate(EmailDto emailDto) {

        Map<String, String> errors = new HashMap<>();

        if (emailDto == null) {
            errors.put("general", "Fill out the form with your data.");
        } else {
            Validator.validateStringField(
                    emailDto.email(), emailRegex,
                    "Email has invalid format.", "Email",
                    errors);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(
                    errors
                            .values()
                            .stream()
                            .collect(Collectors.joining("\n"))
            );
        }
    }
}
