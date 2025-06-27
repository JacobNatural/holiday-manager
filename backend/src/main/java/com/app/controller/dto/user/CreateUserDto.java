package com.app.controller.dto.user;

import com.app.persistence.entity.UserEntity;
import com.app.model.Role;

/**
 * Data Transfer Object (DTO) used to create a new regular user.
 * Contains personal and account information needed for user registration.
 *
 * @param firstName  the user's first name (currently unused in mapping)
 * @param name       the user's given name (used in entity)
 * @param surname    the user's surname
 * @param username   the desired username for login
 * @param password   the user's password
 * @param email      the user's email address
 * @param age        the user's age
 */
public record CreateUserDto(
        String firstName,
        String name,
        String surname,
        String username,
        String password,
        String email,
        int age
) {
    /**
     * Converts this DTO into a {@link UserEntity} with default settings.
     * Sets the role to {@link Role#ROLE_WORKER}, disables the account by default,
     * and initializes holiday hours to 0.
     *
     * @return a new {@link UserEntity} instance based on this DTO
     */
    public UserEntity toEntity() {
        return UserEntity
                .builder()
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(Role.ROLE_WORKER)
                .enable(false)
                .holidaysHours(0)
                .build();
    }
}
