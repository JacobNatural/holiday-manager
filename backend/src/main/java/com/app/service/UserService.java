package com.app.service;

import com.app.controller.dto.user.*;
import com.app.model.Role;
import java.util.List;

public interface UserService {

    /**
     * Retrieves the currently logged-in user's details based on the provided token.
     *
     * @param token Authentication token of the current user
     * @return UserDto containing information about the logged-in user
     */
    UserDto getActualLoginUser(String token);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The ID of the user to retrieve
     * @return UserDto representing the user with the given ID
     */
    UserDto getUserById(Long id);

    /**
     * Creates a new user in the system.
     *
     * @param createUserDto DTO containing details of the user to create
     * @return The ID of the newly created user
     */
    Long createUser(CreateUserDto createUserDto);

    /**
     * Retrieves a list of users matching the specified criteria.
     *
     * @param userSpecificationDto DTO containing user filter criteria
     * @return List of UserDto matching the criteria
     */
    List<UserDto> getUsers(UserSpecificationDto userSpecificationDto);

    /**
     * Activates a user account using the provided activation token.
     *
     * @param userActivationTokenDto DTO containing the activation token
     * @return The ID of the activated user
     */
    Long activateUser(UserActivationTokenDto userActivationTokenDto);

    /**
     * Retrieves the role of the currently logged-in user based on the token.
     *
     * @param token Authentication token of the current user
     * @return Role of the logged-in user
     */
    Role getActualLoginRole(String token);

    /**
     * Refreshes the email verification token for a user.
     *
     * @param emailDto DTO containing the user's email
     * @return The ID of the user whose verification email token was refreshed
     */
    Long refreshVerificationEmailToken(EmailDto emailDto);

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param changePasswordDto DTO containing current and new password details
     * @param token             Authentication token of the current user
     * @return The ID of the user whose password was changed
     */
    Long changePassword(ChangePasswordDto changePasswordDto, String token);

    /**
     * Initiates a lost password process for a user by their email.
     *
     * @param emailDto DTO containing the user's email
     * @return The ID of the user who requested password recovery
     */
    Long lostPassword(EmailDto emailDto);

    /**
     * Sets a new password for a user using a verification token.
     *
     * @param newPasswordDto DTO containing the new password and verification token
     * @return The ID of the user whose password was reset
     */
    Long newPassword(NewPasswordDto newPasswordDto);

    /**
     * Changes the email address of the currently authenticated user.
     *
     * @param newEmailDto DTO containing current password and new email
     * @param token       Authentication token of the current user
     * @return The ID of the user whose email was changed
     */
    Long changeEmail(NewEmailDto newEmailDto, String token);

    /**
     * Updates user details such as role and holiday hours.
     *
     * @param updateUserDto DTO containing updated user information
     * @return The ID of the updated user
     */
    Long updateUser(UpdateUserDto updateUserDto);

    /**
     * Deletes the currently logged-in user (soft delete).
     * Admin users cannot be deleted.
     *
     * @param token Authentication token of the current user
     * @return The ID of the deleted user
     */
    Long deleteUser(String token);

    /**
     * Deletes a user by ID if the currently logged-in user has admin privileges.
     *
     * @param userId The ID of the user to delete
     * @param token  Authentication token of the current user
     * @return The ID of the deleted user
     */
    Long deleteUser(Long userId, String token);
}
