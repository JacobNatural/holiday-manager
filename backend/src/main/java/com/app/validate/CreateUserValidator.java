package com.app.validate;

import com.app.controller.dto.user.CreateUserDto;
import com.app.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CreateUserValidator implements Validator<CreateUserDto> {

    private final String nameRegex;
    private final String surnameRegex;
    private final String usernameRegex;
    private final List<String> passwordRegex;
    private final String emailRegex;
    private final int minAge;
    private final int passwordMinLength;

    /**
     * Constructs a CreateUserValidator with regex patterns and validation constraints.
     *
     * @param nameRegex regex pattern for validating the user's name
     * @param surnameRegex regex pattern for validating the user's surname
     * @param usernameRegex regex pattern for validating the username
     * @param passwordRegex list of regex patterns for validating passwords
     * @param emailRegex regex pattern for validating email format
     * @param minAge minimum allowed age for the user
     * @param passwordMinLength minimum length required for the password
     */
    public CreateUserValidator(
            @Value("${validate.regex.name}") String nameRegex,
            @Value("${validate.regex.surname}") String surnameRegex,
            @Value("${validate.regex.username}") String usernameRegex,
            @Value("${validate.regex.password}") List<String> passwordRegex,
            @Value("${validate.regex.email}") String emailRegex,
            @Value("${validate.min.age}") int minAge,
            @Value("${validate.password.min.length}") int passwordMinLength) {
        this.nameRegex = nameRegex;
        this.surnameRegex = surnameRegex;
        this.usernameRegex = usernameRegex;
        this.passwordRegex = passwordRegex;
        this.minAge = minAge;
        this.emailRegex = emailRegex;
        this.passwordMinLength = passwordMinLength;
    }

    /**
     * Validates the CreateUserDto object by checking:
     * - Whether the DTO is null.
     * - If the name, surname, username, and email match the required regex patterns.
     * - If the user's age is above the minimum required age.
     * - If the password complies with password regex rules and minimum length.
     *
     * Collects all validation errors and throws ValidationException with all error messages joined.
     *
     * @param createUserDto the DTO containing user data to validate
     * @throws ValidationException if any validation errors are found
     */
    @Override
    public void validate(CreateUserDto createUserDto) {

        Map<String, String> errors = new HashMap<>();

        if (createUserDto == null) {
            errors.put("general", "Fill out the form with your data.");
        } else {
            Validator.validateStringField(
                    createUserDto.name(), nameRegex,
                    "Name should contain only letters", "Name", errors);

            Validator.validateStringField(
                    createUserDto.surname(), surnameRegex,
                    "Surname should contain only letters", "Surname", errors);

            Validator.validateStringField(
                    createUserDto.username(), usernameRegex,
                    "Username should contain only letters and numbers", "Username", errors);

            Validator.validateStringField(
                    createUserDto.email(), emailRegex,
                    "Email has invalid format", "Email", errors);

            if (createUserDto.age() < minAge) {
                errors.put("Age", "Age should be greater than " + minAge);
            }

            Validator.validatePassword(createUserDto.password(), "Password", passwordRegex, errors, passwordMinLength);
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(
                    errors
                            .values()
                            .stream()
                            .collect(Collectors.joining("\n")));
        }
    }
}
