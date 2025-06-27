package com.app.controller.dto.user;

/**
 * Data Transfer Object (DTO) used to request a password change.
 * Contains the current password and the new password (with confirmation).
 *
 * @param currentPassword  the user's current password
 * @param newPassword      the new password the user wants to set
 * @param confirmPassword  confirmation of the new password to ensure correctness
 */
public record ChangePasswordDto(
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
