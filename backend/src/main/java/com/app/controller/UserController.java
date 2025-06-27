package com.app.controller;

import com.app.controller.dto.*;
import com.app.controller.dto.user.*;
import com.app.model.Role;
import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import com.app.security.service.TokenService;
import com.app.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management operations.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    /**
     * Creates a new user.
     *
     * @param createUserDto DTO containing user creation details.
     * @return ResponseDto containing the ID of the created user.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Long> createUser(@RequestBody CreateUserDto createUserDto) {
        return new ResponseDto<>(userService.createUser(createUserDto));
    }

    /**
     * Activates a user account using an activation token.
     *
     * @param userActivationTokenDto DTO containing the activation token.
     * @return ResponseDto containing the ID of the activated user.
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> activatedUser(@RequestBody UserActivationTokenDto userActivationTokenDto) {
        return new ResponseDto<>(userService.activateUser(userActivationTokenDto));
    }

    /**
     * Requests a new verification email token to be sent.
     *
     * @param emailDto DTO containing the user's email address.
     * @return ResponseDto containing the user ID for whom the token was refreshed.
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> refreshVerificationEmailToken(@RequestBody EmailDto emailDto) {
        return new ResponseDto<>(userService.refreshVerificationEmailToken(emailDto));
    }

    /**
     * Initiates a lost password process by sending a reset email.
     *
     * @param emailDto DTO containing the user's email address.
     * @return ResponseDto containing the user ID associated with the request.
     */
    @PatchMapping("/lost")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> lostPassword(@RequestBody EmailDto emailDto) {
        return new ResponseDto<>(userService.lostPassword(emailDto));
    }

    /**
     * Sets a new password for the user.
     *
     * @param newPasswordDto DTO containing the new password and related info.
     * @return ResponseDto containing the user ID whose password was updated.
     */
    @PatchMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> newPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return new ResponseDto<>(userService.newPassword(newPasswordDto));
    }

    /**
     * Refreshes authentication tokens using the refresh token from cookies.
     *
     * @param token   Refresh token from the cookie.
     * @param response HTTP response to set updated cookies.
     * @return ResponseDto containing the new tokens.
     */
    @GetMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<TokensDto> refreshToken(@CookieValue("RefreshToken") String token, HttpServletResponse response) {
        var tokens = tokenService.refreshToken(new RefreshTokenDto(token));
        tokenService.setCookie(tokens, response);
        return new ResponseDto<>(tokens);
    }

    /**
     * Checks if the user has access (token validation).
     *
     * @param token Access token from the cookie.
     * @return ResponseDto with success message.
     */
    @GetMapping("/in/access")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<String> hasAccess(@CookieValue("AccessToken") String token){
        return new ResponseDto<>("Access Successful");
    }

    /**
     * Logs out the user by invalidating authentication cookies.
     *
     * @param token    Access token from the cookie.
     * @param response HTTP response to clear cookies.
     * @return ResponseDto containing logout confirmation message.
     */
    @GetMapping("/in/disable")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<LogoutDto> disable(@CookieValue("AccessToken") String token, HttpServletResponse response) {

        var accessTokenCookie = new Cookie("AccessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setSecure(true);

        var refreshTokenCookie = new Cookie("RefreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setSecure(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        return new ResponseDto<>(new LogoutDto("Logout successful"));
    }

    /**
     * Retrieves information about the currently logged-in user.
     *
     * @param token Access token from the cookie.
     * @return ResponseDto containing the user data.
     */
    @GetMapping("/in/user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<UserDto> getActualLoginUser(@CookieValue("AccessToken") String token) {
        return new ResponseDto<>(userService.getActualLoginUser(token));
    }

    /**
     * Retrieves the role of the currently logged-in user.
     *
     * @param token Access token from the cookie.
     * @return ResponseDto containing the user's role.
     */
    @GetMapping("/in/role")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Role> getActualLoginRole(@CookieValue("AccessToken") String token) {
        return new ResponseDto<>(userService.getActualLoginRole(token));
    }

    /**
     * Changes the password of the currently logged-in user.
     *
     * @param changePasswordDto DTO containing old and new passwords.
     * @param token             Access token from the cookie.
     * @return ResponseDto containing the user ID whose password was changed.
     */
    @PatchMapping("/in/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @CookieValue("AccessToken") String token) {
        return new ResponseDto<>(userService.changePassword(changePasswordDto, token));
    }

    /**
     * Changes the email address of the currently logged-in user.
     *
     * @param newEmailDto DTO containing the new email.
     * @param token       Access token from the cookie.
     * @return ResponseDto containing the user ID whose email was changed.
     */
    @PatchMapping("/in/email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> changeEmail(
            @RequestBody NewEmailDto newEmailDto, @CookieValue("AccessToken") String token) {
        return new ResponseDto<>(userService.changeEmail(newEmailDto, token));
    }

    /**
     * Deletes the currently logged-in user.
     *
     * @param token Access token from the cookie.
     * @return ResponseDto containing the ID of the deleted user.
     */
    @DeleteMapping("/in")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> deleteUser(@CookieValue("AccessToken") String token) {
        return new ResponseDto<>(userService.deleteUser(token));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id ID of the user to retrieve.
     * @return ResponseDto containing the user data.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<UserDto> getUser(@PathVariable Long id) {
        return new ResponseDto<>(userService.getUserById(id));
    }

    /**
     * Retrieves users filtered by criteria.
     *
     * @param userSpecificationDto DTO containing filtering criteria.
     * @return ResponseDto containing a list of users matching the criteria.
     */
    @PatchMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<UserDto>> getUsers(@RequestBody UserSpecificationDto userSpecificationDto) {
        return new ResponseDto<>(userService.getUsers(userSpecificationDto));
    }

    /**
     * Deletes a user by their ID (admin operation).
     *
     * @param userId ID of the user to delete.
     * @param token  Authorization token from the request header.
     * @return ResponseDto containing the ID of the deleted user.
     */
    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> deleteUser(@RequestParam Long userId, @RequestHeader("Authorization") String token) {
        return new ResponseDto<>(userService.deleteUser(userId, token));
    }

    /**
     * Updates a user with new data.
     *
     * @param updateUserDto DTO containing updated user data.
     * @return ResponseDto containing the ID of the updated user.
     */
    @PatchMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> updateUser(@RequestBody UpdateUserDto updateUserDto) {
        return new ResponseDto<>(userService.updateUser(updateUserDto));
    }
}
