package com.app.service.impl;

import com.app.controller.dto.user.*;
import com.app.exception.ResourceAlreadyExistException;
import com.app.exception.ValidationException;
import com.app.model.Role;
import com.app.persistence.Repository.UserRepository;
import com.app.persistence.Repository.VerificationTokenRepository;
import com.app.persistence.entity.BaseEntity;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.specification.FilterSpecification;
import com.app.persistence.specification.model.UserSpecification;
import com.app.security.service.TokenService;
import com.app.service.UserService;
import com.app.validate.Validator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link UserService} providing user management functionality.
 * This service handles user creation, activation, password management,
 * email changes, user updates, retrieval and deletion operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CreateAdminUserDto createAdminUserDto;
    private final TokenService tokenService;
    private final FilterSpecification<UserEntity, UserSpecification> userFilterSpecification;
    private final Validator<CreateUserDto> createUserDtovalidator;
    private final Validator<EmailDto> emailDtoValidator;
    private final Validator<ChangePasswordDto> changePasswordDtoValidator;
    private final Validator<NewPasswordDto> newPasswordDtoValidator;
    private final Validator<NewEmailDto> newEmailDtoValidator;

    /**
     * Initializes the service by saving an admin user on startup.
     * The password is encoded before saving.
     */
    @PostConstruct
    public void init() {

        if (userRepository.findByUsername(createAdminUserDto.username()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists");
        }

        userRepository.save(createAdminUserDto.toUserEntity()
                .withPassword(passwordEncoder.encode(createAdminUserDto.password())));
    }

    /**
     * Creates a new user with given data.
     * Validates the input and checks for existing username and email.
     * Encodes the password before saving.
     * Publishes a user activation event.
     *
     * @param createUserDto the DTO containing user creation data
     * @return the ID of the created user
     * @throws ResourceAlreadyExistException if username or email already exists
     * @throws ValidationException if validation of DTO fails
     */
    @Override
    public Long createUser(CreateUserDto createUserDto) {

        createUserDtovalidator.validate(createUserDto);

        if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists");
        }

        if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new ResourceAlreadyExistException("Email already exists");
        }

        var userEntityId = userRepository.save(
                createUserDto.toEntity().withPassword(passwordEncoder.encode(createUserDto.password()))).getId();

        eventPublisher.publishEvent(new UserActivationDto(userEntityId));

        return userEntityId;
    }

    /**
     * Activates a user based on the provided activation token.
     * Validates the token and deletes it after successful activation.
     *
     * @param userActivationTokenDto DTO containing the activation token
     * @return the ID of the activated user
     * @throws ValidationException if token is null or expired
     * @throws EntityNotFoundException if token is not found
     */
    public Long activateUser(UserActivationTokenDto userActivationTokenDto) {
        if (userActivationTokenDto == null) {
            throw new ValidationException("Activation token cannot be null");
        }

        var verificationToken =
                verificationTokenRepository.findByToken(userActivationTokenDto.token())
                        .orElseThrow(() -> new EntityNotFoundException("Verification token not found"));

        var userEntity = verificationToken.validate();
        verificationTokenRepository.delete(verificationToken);
        userEntity.ifPresent(user -> userRepository.save(user.withActivation()));

        return userEntity.map(BaseEntity::getId).orElseThrow(() ->
                new ValidationException("Verification token expired"));
    }

    /**
     * Refreshes the verification email token for a user.
     * Validates the input email and checks if the user is already activated.
     * If an existing valid token exists, throws an exception.
     * Otherwise, deletes expired token and publishes a new activation event.
     *
     * @param emailDto DTO containing the user's email
     * @return the ID of the user for whom the token was refreshed
     * @throws ValidationException if user is already activated or validation fails
     * @throws EntityNotFoundException if user is not found
     * @throws ResourceAlreadyExistException if valid token already exists
     */
    public Long refreshVerificationEmailToken(EmailDto emailDto) {

        emailDtoValidator.validate(emailDto);

        var userFromDb = userRepository
                .findByEmail(emailDto.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userFromDb.isEnable()) {
            throw new ValidationException("User already activated");
        }

        verificationTokenRepository
                .findByUserId(userFromDb.getId())
                .ifPresent(verificationToken -> {
                    if (verificationToken.validate().isPresent()) {
                        throw new ResourceAlreadyExistException("Token already exists");
                    } else {
                        verificationTokenRepository.delete(verificationToken);
                    }
                });

        eventPublisher.publishEvent(new UserActivationDto(userFromDb.getId()));

        return userFromDb.getId();
    }

    /**
     * Changes the password for the currently authenticated user.
     * Validates input and verifies current password before changing.
     *
     * @param changePasswordDto DTO containing current and new password
     * @param token authentication token of the user
     * @return the ID of the user whose password was changed
     * @throws ValidationException if validation fails
     * @throws EntityNotFoundException if user is not found
     * @throws IllegalArgumentException if current password does not match
     */
    public Long changePassword(ChangePasswordDto changePasswordDto, String token) {

        changePasswordDtoValidator.validate(changePasswordDto);

        var userId = tokenService.id(token.replace("Bearer ", ""));

        var userFromDb = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.currentPassword(), userFromDb.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        return userRepository.save(userFromDb
                        .withPassword(passwordEncoder.encode(changePasswordDto.newPassword())))
                .getId();
    }

    /**
     * Initiates password reset process by publishing activation event.
     * Validates the email and verifies user existence.
     *
     * @param emailDto DTO containing the user's email
     * @return the ID of the user who lost the password
     * @throws ValidationException if validation fails
     * @throws EntityNotFoundException if user is not found
     */
    public Long lostPassword(EmailDto emailDto) {

        emailDtoValidator.validate(emailDto);

        var userFromDB = userRepository
                .findByEmail(emailDto.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        eventPublisher.publishEvent(new UserActivationDto(userFromDB.getId()));
        return userFromDB.getId();
    }

    /**
     * Sets a new password for a user based on a valid token.
     * Validates the new password DTO and the token.
     *
     * @param newPasswordDto DTO containing the token and new password
     * @return the ID of the user whose password was updated, or null if invalid
     * @throws ValidationException if validation fails
     * @throws EntityNotFoundException if token is not found
     */
    public Long newPassword(NewPasswordDto newPasswordDto) {

        newPasswordDtoValidator.validate(newPasswordDto);

        var verificationToken = verificationTokenRepository
                .findByToken(newPasswordDto.token())
                .orElseThrow(() -> new EntityNotFoundException("Verification token not found"));

        var useFromDb = verificationToken.validate();

        verificationTokenRepository.delete(verificationToken);
        useFromDb.ifPresent(user ->
                userRepository.save(user.withPassword(passwordEncoder.encode(newPasswordDto.newPassword()))));

        return useFromDb.map(BaseEntity::getId).orElse(null);
    }

    /**
     * Changes the email address of the authenticated user.
     * Validates input and verifies current password before updating.
     *
     * @param newEmailDto DTO containing the current password and new email
     * @param token authentication token of the user
     * @return the ID of the user whose email was changed
     * @throws ValidationException if validation fails
     * @throws EntityNotFoundException if user is not found
     * @throws IllegalArgumentException if current password does not match
     */
    public Long changeEmail(NewEmailDto newEmailDto, String token) {

        newEmailDtoValidator.validate(newEmailDto);

        var userId = tokenService.id(token.replace("Bearer ", ""));

        var userFromDb = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(newEmailDto.currentPassword(), userFromDb.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        return userRepository.save(userFromDb.withNewEmail(newEmailDto.newEmail())).getId();
    }

    /**
     * Updates user role and holiday hours.
     *
     * @param updateUserDto DTO containing user ID, new role, and holiday hours
     * @return the ID of the updated user
     * @throws EntityNotFoundException if user is not found
     */
    @Override
    public Long updateUser(UpdateUserDto updateUserDto) {

        var userFromDb = userRepository
                .findById(updateUserDto.userId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userRepository
                .save(userFromDb.withNewRoleAndNewHolidaysHours(updateUserDto.holidayHours(), updateUserDto.role())).getId();
    }

    /**
     * Retrieves a list of users filtered by the given specification.
     *
     * @param userSpecificationDto DTO with user filtering criteria
     * @return list of users matching the specification
     * @throws ValidationException if the specification DTO is null
     */
    public List<UserDto> getUsers(UserSpecificationDto userSpecificationDto) {

        if (userSpecificationDto == null) {
            throw new ValidationException("UserSpecificationDto cannot be null");
        }

        return userRepository
                .findAll(userFilterSpecification.dynamicFilter(userSpecificationDto.toUserSpecification()))
                .stream()
                .map(UserEntity::toUserDto)
                .toList();
    }

    /**
     * Retrieves the currently logged-in user based on the authentication token.
     *
     * @param token authentication token
     * @return the DTO of the logged-in user
     * @throws EntityNotFoundException if user is not found
     */
    public UserDto getActualLoginUser(String token) {
        var userId = tokenService.id(token.replace("Bearer ", ""));
        return getUserById(userId);
    }

    /**
     * Retrieves the role of the currently logged-in user based on the authentication token.
     *
     * @param token authentication token
     * @return the role of the logged-in user
     * @throws EntityNotFoundException if user is not found
     */
    public Role getActualLoginRole(String token) {

        var userId = tokenService.id(token.replace("Bearer ", ""));

        return userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
                .getRole();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return the user DTO
     * @throws EntityNotFoundException if user is not found
     */
    @Override
    public UserDto getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."))
                .toUserDto();
    }

    /**
     * Deletes the currently authenticated user.
     * Admin users cannot be deleted.
     *
     * @param token authentication token of the user to delete
     * @return the ID of the deleted user
     * @throws ValidationException if user is admin
     * @throws EntityNotFoundException if user is not found
     */
    public Long deleteUser(String token) {
        var userId = tokenService.id(token.replace("Bearer ", ""));

        var userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userFromDb.isAdmin()) {
            throw new ValidationException("Admin user cannot be deleted");
        }

        return userRepository.save(userFromDb.withDelete()).getId();
    }

    /**
     * Deletes a user by ID if the logged-in user is an admin.
     *
     * @param userId the ID of the user to delete
     * @param token authentication token of the logged-in user
     * @return the ID of the deleted user
     * @throws EntityNotFoundException if logged-in user or user to delete is not found,
     * or if logged-in user is not admin
     */
    public Long deleteUser(Long userId, String token) {
        var userIdLogin = tokenService.id(token.replace("Bearer ", ""));

        var userFromDbLogin = userRepository.findById(userIdLogin)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));


        if (userFromDbLogin.isAdmin()) {
            var userToDelete = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));

            return userRepository.save(userToDelete.withDelete()).getId();
        }

        throw new EntityNotFoundException("User not found");
    }
}
