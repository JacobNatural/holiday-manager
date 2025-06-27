package com.app.validate;

import com.app.controller.dto.user.ChangePasswordDto;
import com.app.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChangePasswordDtoValidator implements Validator<ChangePasswordDto> {

    private final List<String> passwordRegexs;
    private final int minLength;

    /**
     * Constructs a ChangePasswordDtoValidator with password validation rules.
     *
     * @param passwordRegexs List of regex patterns to validate password complexity
     * @param minLength      Minimum length required for the password
     */
    public ChangePasswordDtoValidator(
            @Value("${validate.regex.password}") List<String> passwordRegexs,
            @Value("${validate.password.min.length}") int minLength) {
        this.passwordRegexs = passwordRegexs;
        this.minLength = minLength;
    }

    /**
     * Validates the ChangePasswordDto object by checking:
     * - If the DTO is null.
     * - If the current password meets complexity requirements.
     * - If the new password and confirmation password are equal.
     * - If the new password meets complexity requirements.
     * - If the confirmation password meets complexity requirements.
     *
     * If any validation fails, a ValidationException is thrown containing all error messages.
     *
     * @param changePasswordDto The DTO containing current, new and confirmation passwords
     * @throws ValidationException if any validation rule is violated
     */
    @Override
    public void validate(ChangePasswordDto changePasswordDto) {
        var errors = new HashMap<String, String>();

        if (changePasswordDto == null) {
            errors.put("general", "Fill out the form with your data.");
        } else {
            Validator.validatePassword(
                    changePasswordDto.currentPassword(),
                    "Current password",
                    passwordRegexs,
                    errors,
                    minLength);

            if (!changePasswordDto.newPassword().equals(changePasswordDto.confirmPassword())) {
                errors.put("password not equals", "New password and confirm password are not the same.");
            }

            Validator.validatePassword(
                    changePasswordDto.newPassword(),
                    "New password",
                    passwordRegexs,
                    errors,
                    minLength);

            Validator.validatePassword(
                    changePasswordDto.confirmPassword(),
                    "Confirm password",
                    passwordRegexs,
                    errors,
                    minLength);
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
