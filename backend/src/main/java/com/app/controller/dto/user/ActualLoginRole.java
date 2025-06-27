package com.app.controller.dto.user;

import com.app.model.Role;

/**
 * Data Transfer Object (DTO) representing the current role
 * of the logged-in user in the application.
 *
 * @param role the role assigned to the currently authenticated user
 */
public record ActualLoginRole(Role role) {
}
