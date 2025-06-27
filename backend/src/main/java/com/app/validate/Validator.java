package com.app.validate;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Generic Validator interface for validating objects of type T.
 * Provides static helper methods for common validation tasks such as string field validation and password validation.
 *
 * @param <T> the type of object to validate
 */
public interface Validator<T> {

    /**
     * Validates the given object.
     * Implementations should throw an exception or handle errors as appropriate.
     *
     * @param t the object to validate
     */
    void validate(T t);

    /**
     * Validates a string field against a given regex pattern.
     * Checks if the string is null or empty and if it matches the regex pattern.
     * Adds error messages to the provided errors map.
     *
     * @param text the string value to validate
     * @param regex the regex pattern the string must match
     * @param message the error message if the regex does not match
     * @param fieldName the name of the field being validated (used as key in errors map)
     * @param errors map to collect validation errors
     */
    static void validateStringField(
            String text, String regex,
            String message, String fieldName,
            Map<String, String> errors) {
        if (text == null || text.isEmpty()) {
            errors.put(fieldName, "%s can not be empty.".formatted(fieldName));
        } else if (!text.matches(regex)) {
            errors.put(fieldName, message);
        }
    }

    /**
     * Validates a password string against multiple regex patterns and minimum length.
     * Checks if the password is null or empty, verifies length, and validates presence of required character groups.
     * Adds error messages to the provided errors map.
     *
     * @param password the password string to validate
     * @param fieldName the name of the password field (used as key in errors map)
     * @param regexs list of regex patterns that the password must satisfy (e.g., lowercase, uppercase, digit, special char)
     * @param errors map to collect validation errors
     * @param minLength minimum length required for the password
     */
    static void validatePassword(
            String password,
            String fieldName,
            List<String> regexs,
            Map<String, String> errors,
            int minLength) {
        if (password == null || password.isEmpty()) {
            errors.put("password null or empty", "Password can not be empty.");
        }

        if (password.length() < minLength) {
            errors.put(fieldName + " password length", fieldName + " must be at least " + minLength + " characters.");
        }

        for (var regex : regexs) {
            var pattern = Pattern.compile(regex);
            var matcher = pattern.matcher(password);
            if (!matcher.find()) {
                errors.put(
                        fieldName,
                        "Password should contains: small letter, big letter, number and special character: (!@#$%^&*?)");
                break; // Stop after first failed pattern to avoid multiple identical errors
            }
        }
    }
}
