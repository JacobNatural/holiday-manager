package com.app.controller.dto.user;

import com.app.model.Role;

/**
 * Data Transfer Object (DTO) representing user details.
 * Typically used to transfer user information between layers or to the client.
 *
 * @param id            the unique identifier of the user
 * @param name          the user's given name
 * @param surname       the user's surname
 * @param username      the user's login username
 * @param email         the user's email address
 * @param age           the user's age
 * @param holidaysHours the number of holiday hours the user has accrued
 * @param role          the role assigned to the user (e.g., admin, worker)
 */
public record UserDto(
        Long id,
        String name,
        String surname,
        String username,
        String email,
        Integer age,
        Long holidaysHours,
        Role role
) { }
