package com.app.controller.dto.user;

/**
 * Data Transfer Object (DTO) representing a user activation token.
 * Typically used during account activation to verify the user's email or identity.
 *
 * @param token the activation token string
 */
public record UserActivationTokenDto(String token) {
}
