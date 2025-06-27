package com.app.validate;

import com.app.controller.dto.user.NewPasswordDto;
import com.app.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validator implementation for NewPasswordDto objects.
 * Validates the new password and confirm password fields according to configured password rules.
 */
@Component
public class NewPasswordDtoValidator implements Validator<NewPasswordDto> {

    private final List<String> passwordRegexs;
    private final int minLength;

    /**
     * Constructs a NewPasswordDtoValidator with the given password validation parameters.
     *
     * @param passwordRegexs list of regex patterns to validate password complexity
     * @param minLength minimum length required for the password
     */
    public NewPasswordDtoValidator(
            @Value("${validate.regex.password}") List<String> passwordRegexs,
            @Value("${validate.password.min.length}") int minLength) {
        this.passwordRegexs = passwordRegexs;
        this.minLength = minLength;
    }

    /**
     * Validates the given NewPasswordDto object.
     * Checks for null input and validates the new password and confirm password fields.
     * Collects errors and throws ValidationException if validation fails.
     *
     * @param newPasswordDto the NewPasswordDto object to validate
     * @throws ValidationException if any validation errors occur
     */
    @Override
    public void validate(NewPasswordDto newPasswordDto) {
        Map<String, String> errors = new HashMap<>();

        if (newPasswordDto == null) {
            errors.put("general", "Fill out the form with your data.");
        } else {
            Validator.validatePassword(
                    newPasswordDto.newPassword(), "New password",
                    passwordRegexs, errors, minLength);

            Validator.validatePassword(
                    newPasswordDto.confirmPassword(), "Confirm password",
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
