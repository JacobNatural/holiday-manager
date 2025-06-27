package com.app.controller.dto.user;

import com.app.model.Role;
import com.app.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Data Transfer Object (DTO) for creating an admin user.
 * The fields are populated from application properties using {@link Value}.
 *
 * <p>This class is also marked as a Spring {@link Component},
 * allowing it to be injected wherever needed.</p>
 *
 * @param name     the admin's first name, injected from the property {@code admin.name}
 * @param surname  the admin's surname, injected from the property {@code admin.surname}
 * @param username the admin's username, injected from the property {@code admin.username}
 * @param password the admin's password, injected from the property {@code admin.password}
 * @param email    the admin's email address, injected from the property {@code admin.email}
 * @param age      the admin's age, injected from the property {@code admin.age}
 */
@Component
public record CreateAdminUserDto(

        @Value("${admin.name}")
        String name,

        @Value("${admin.surname}")
        String surname,

        @Value("${admin.username}")
        String username,

        @Value("${admin.password}")
        String password,

        @Value("${admin.email}")
        String email,

        @Value("${admin.age}")
        int age

) {
        /**
         * Converts this DTO into a {@link UserEntity} with admin privileges.
         * Sets the user's role to {@link Role#ROLE_ADMIN}, enables the account,
         * and initializes holiday hours to 0.
         *
         * @return a new instance of {@link UserEntity} configured as an admin
         */
        public UserEntity toUserEntity() {
                return UserEntity
                        .builder()
                        .name(name)
                        .surname(surname)
                        .username(username)
                        .password(password)
                        .email(email)
                        .age(age)
                        .enable(true)
                        .role(Role.ROLE_ADMIN)
                        .holidaysHours(0)
                        .build();
        }
}
