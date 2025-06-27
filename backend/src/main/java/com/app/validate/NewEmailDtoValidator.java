package com.app.validate;

import com.app.controller.dto.user.NewEmailDto;
import com.app.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validator implementation for NewEmailDto objects.
 * Validates the new email, confirm email fields and current password against configured regex patterns.
 */
@Component
public class NewEmailDtoValidator implements Validator<NewEmailDto> {

    private final String emailRegex;
    private final List<String> passwordRegexs;
    private final int minLength;

    /**
     * Constructs a NewEmailDtoValidator with the provided validation parameters.
     *
     * @param emailRegex     regex pattern for validating email format
     * @param passwordRegexs list of regex patterns for validating password complexity
     * @param minLength      minimum length required for the password
     */
    public NewEmailDtoValidator(
            @Value("${validate.regex.email}") String emailRegex,
            @Value("${validate.regex.password}") List<String> passwordRegexs,
            @Value("${validate.password.min.length}") int minLength) {
        this.emailRegex = emailRegex;
        this.passwordRegexs = passwordRegexs;
        this.minLength = minLength;
    }

    /**
     * Validates the given NewEmailDto object.
     * Checks if the DTO is null, validates new email and confirm email fields against the email regex,
     * and validates the current password using configured password regexes and minimum length.
     * Collects errors and throws ValidationException if any validation fails.
     *
     * @param newEmailDto the NewEmailDto object to validate
     * @throws ValidationException if validation errors are found
     */
    @Override
    public void validate(NewEmailDto newEmailDto) {

        Map<String, String> errors = new HashMap<>();

        if (newEmailDto == null) {
            errors.put("general", "Fill out the form with your data.");
        } else {
            Validator.validateStringField(
                    newEmailDto.newEmail(), emailRegex,
                    "New email address is not valid.", "New email", errors);

            Validator.validateStringField(
                    newEmailDto.confirmEmail(), emailRegex,
                    "Confirm email address is not valid.", "Confirm email", errors);

            Validator.validatePassword(
                    newEmailDto.currentPassword(), "Current password",
                    passwordRegexs, errors, minLength);
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
