package com.app.controller.dto.user;

/**
 * Data Transfer Object (DTO) representing a logout response message.
 * Typically used to inform the client about the logout status or confirmation.
 *
 * @param message a message describing the logout outcome
 */
public record LogoutDto(String message) {
}
