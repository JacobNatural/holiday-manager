package com.app.controller.dto.user;

/**
 * Data Transfer Object (DTO) used for resetting or changing a user's password.
 * Contains the new password, its confirmation, and a token for verification.
 *
 * @param newPassword     the new password the user wants to set
 * @param confirmPassword confirmation of the new password to ensure correctness
 * @param token           the token used to verify the password reset request (e.g., from email)
 */
public record NewPasswordDto(String newPassword, String confirmPassword, String token) {
}
