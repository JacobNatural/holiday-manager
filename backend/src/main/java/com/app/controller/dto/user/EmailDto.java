package com.app.controller.dto.user;

/**
 * Data Transfer Object (DTO) used to transfer an email address,
 * typically for operations like password reset, email verification,
 * or user lookup by email.
 *
 * @param email the user's email address
 */
public record EmailDto(String email) {
}
