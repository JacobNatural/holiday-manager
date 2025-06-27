package com.app.persistence.specification.model;

/**
 * Specification model used to filter {@link com.app.persistence.entity.UserEntity}
 * entities based on dynamic criteria.
 *
 * <p>This record is intended to be used in conjunction with {@code Specification<UserEntity>}
 * to build type-safe, flexible queries in a declarative way.</p>
 *
 * @param name              the name of the user to filter by (exact match, optional)
 * @param surname           the surname of the user to filter by (exact match, optional)
 * @param username          the username to filter by (exact match, optional)
 * @param email             the email to filter by (exact match, optional)
 * @param minAge            the minimum age of the user (inclusive, optional)
 * @param maxAge            the maximum age of the user (inclusive, optional)
 * @param minHolidayHours   the minimum amount of holiday hours (inclusive, optional)
 * @param maxHolidayHours   the maximum amount of holiday hours (inclusive, optional)
 */
public record UserSpecification(
        String name,
        String surname,
        String username,
        String email,
        Integer minAge,
        Integer maxAge,
        Long minHolidayHours,
        Long maxHolidayHours
) {}
