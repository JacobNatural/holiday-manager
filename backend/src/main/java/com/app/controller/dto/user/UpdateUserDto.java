package com.app.controller.dto.user;

import com.app.model.Role;

/**
 * Data Transfer Object (DTO) for updating user details.
 * Contains the user ID, number of holiday hours, and the user's role.
 *
 * @param userId       the unique identifier of the user to update
 * @param holidayHours the updated number of holiday hours assigned to the user
 * @param role         the updated role of the user
 */
public record UpdateUserDto(Long userId, Long holidayHours, Role role) {
}
