package com.app.controller.dto.user;

import com.app.persistence.specification.model.UserSpecification;

/**
 * Data Transfer Object (DTO) for filtering and querying users based on various criteria.
 *
 * @param name             the user's given name to filter by (optional)
 * @param surname          the user's surname to filter by (optional)
 * @param username         the user's username to filter by (optional)
 * @param email            the user's email to filter by (optional)
 * @param minAge           the minimum age to filter by (optional)
 * @param maxAge           the maximum age to filter by (optional)
 * @param minHolidayHours  the minimum holiday hours to filter by (optional)
 * @param maxHolidayHours  the maximum holiday hours to filter by (optional)
 */
public record UserSpecificationDto(
        String name, String surname,
        String username, String email,
        Integer minAge, Integer maxAge,
        Long minHolidayHours, Long maxHolidayHours) {

    /**
     * Converts this DTO to a {@link UserSpecification} used for querying the database.
     *
     * @return a new instance of {@link UserSpecification} initialized with the criteria from this DTO
     */
    public UserSpecification toUserSpecification() {
        return new UserSpecification(
                name, surname, username, email, minAge, maxAge, minHolidayHours, maxHolidayHours);
    }
}
