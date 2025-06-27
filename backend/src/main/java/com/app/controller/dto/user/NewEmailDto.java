package com.app.controller.dto.user;

/**
 * Data Transfer Object (DTO) used for requesting an email change.
 * Contains the current password for verification and the new email with confirmation.
 *
 * @param currentPassword the user's current password for authentication
 * @param newEmail       the new email address to be set
 * @param confirmEmail   confirmation of the new email address to avoid typos
 */
public record NewEmailDto(String currentPassword, String newEmail, String confirmEmail) {
}
