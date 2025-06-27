package com.app.persistence.entity;

import com.app.controller.dto.user.UserDto;
import com.app.model.Role;
import com.app.security.dto.UserDetailsDto;
import com.app.security.dto.UsernamePasswordAuthenticationTokenDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 * This entity maps to the "users" table in the database.
 * Extends {@link BaseEntity} to inherit the ID.
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    /**
     * User's first name.
     */
    private String name;

    /**
     * User's surname/last name.
     */
    private String surname;

    /**
     * Unique username used for authentication.
     */
    private String username;

    /**
     * Encrypted password for user authentication.
     */
    private String password;

    /**
     * User's email address.
     */
    private String email;

    /**
     * User's age.
     */
    private int age;

    /**
     * User's role in the system (e.g., admin, worker).
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Number of holiday hours available to the user.
     */
    private long holidaysHours;

    /**
     * Flag indicating whether the user account is enabled (active).
     */
    private boolean enable;

    /**
     * List of holidays associated with this user.
     * Cascade persist to save holidays automatically when saving the user.
     */
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<HolidayEntity> users = new ArrayList<>();

    /**
     * Returns a new UserEntity instance with updated holiday hours (incremented).
     * Copies all other fields from the current instance.
     *
     * @param holidaysHours the amount of holiday hours to add
     * @return a new UserEntity with updated holidaysHours
     */
    public UserEntity UserEntityWithHolidays(long holidaysHours) {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(role)
                .enable(enable)
                .holidaysHours(this.holidaysHours + holidaysHours)
                .build();
    }

    /**
     * Returns a new UserEntity instance with the specified password.
     *
     * @param password new password
     * @return new UserEntity with updated password
     */
    public UserEntity withPassword(String password) {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(role)
                .enable(enable)
                .holidaysHours(holidaysHours)
                .build();
    }

    /**
     * Returns a new UserEntity instance with account enabled.
     *
     * @return new UserEntity with enable = true
     */
    public UserEntity withActivation() {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(role)
                .enable(true)
                .holidaysHours(holidaysHours)
                .build();
    }

    /**
     * Returns a new UserEntity instance with account disabled.
     *
     * @return new UserEntity with enable = false
     */
    public UserEntity withDeactivation() {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(role)
                .enable(false)
                .holidaysHours(holidaysHours)
                .build();
    }

    /**
     * Returns a new UserEntity instance representing a deleted user.
     * Email is suffixed with "-delete" and account is disabled.
     *
     * @return new UserEntity marked as deleted
     */
    public UserEntity withDelete() {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email + "-delete")
                .password(password)
                .age(age)
                .role(role)
                .enable(false)
                .holidaysHours(holidaysHours)
                .build();
    }

    /**
     * Returns a new UserEntity instance with a new email and account enabled.
     *
     * @param email new email address
     * @return new UserEntity with updated email and enabled
     */
    public UserEntity withNewEmail(String email) {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(role)
                .enable(true)
                .holidaysHours(holidaysHours)
                .build();
    }

    /**
     * Returns a new UserEntity with updated role and holiday hours, account enabled.
     *
     * @param holidaysHours new holiday hours
     * @param role          new user role
     * @return new UserEntity with updated role and holidaysHours
     */
    public UserEntity withNewRoleAndNewHolidaysHours(Long holidaysHours, Role role) {
        return UserEntity.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .username(username)
                .email(email)
                .password(password)
                .age(age)
                .role(role)
                .enable(true)
                .holidaysHours(holidaysHours)
                .build();
    }

    /**
     * Converts this entity to a UserDto.
     *
     * @return UserDto with this user's data
     */
    public UserDto toUserDto() {
        return new UserDto(id, name, surname, username, email, age, holidaysHours, role);
    }

    /**
     * Converts this entity to UserDetailsDto used for security authentication.
     *
     * @return UserDetailsDto with username, password, enable flag and role
     */
    public UserDetailsDto toUserDetailsDto() {
        return new UserDetailsDto(username, password, enable, role.toString());
    }

    /**
     * Converts this entity to UsernamePasswordAuthenticationTokenDto,
     * which contains the username and role string.
     *
     * @return UsernamePasswordAuthenticationTokenDto with username and role
     */
    public UsernamePasswordAuthenticationTokenDto toUsernamePasswordAuthenticationTokenDto() {
        return new UsernamePasswordAuthenticationTokenDto(username, role.toString());
    }

    /**
     * Checks if the user has the admin role.
     *
     * @return true if user role is ROLE_ADMIN, false otherwise
     */
    public boolean isAdmin() {
        return role.equals(Role.ROLE_ADMIN);
    }
}
