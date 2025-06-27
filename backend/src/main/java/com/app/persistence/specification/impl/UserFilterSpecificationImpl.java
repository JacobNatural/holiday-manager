package com.app.persistence.specification.impl;

import com.app.persistence.entity.UserEntity;
import com.app.persistence.specification.FilterSpecification;
import com.app.persistence.specification.model.UserSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link FilterSpecification} for filtering {@link UserEntity} instances
 * dynamically based on the provided {@link UserSpecification} criteria.
 */
@Component
public class UserFilterSpecificationImpl implements FilterSpecification<UserEntity, UserSpecification> {

    /**
     * Builds a dynamic JPA {@link Specification} for {@link UserEntity} based on
     * the non-null and non-empty fields of the given {@link UserSpecification}.
     *
     * <p>Supports filtering by:
     * <ul>
     *   <li>name - exact match</li>
     *   <li>surname - exact match</li>
     *   <li>username - exact match</li>
     *   <li>email - exact match</li>
     *   <li>minAge - users with age greater than or equal to this value</li>
     *   <li>maxAge - users with age less than or equal to this value</li>
     *   <li>minHolidayHours - users with holidaysHours greater than or equal to this value</li>
     *   <li>maxHolidayHours - users with holidaysHours less than or equal to this value</li>
     * </ul>
     *
     * @param userSpecification the filter criteria to apply
     * @return a JPA {@link Specification} representing the combined filter conditions
     */
    public Specification<UserEntity> dynamicFilter(UserSpecification userSpecification) {
        return ((root, query, cb) -> {
            var p = cb.conjunction();

            if (userSpecification.name() != null && !userSpecification.name().isEmpty()) {
                p = cb.and(
                        p,
                        cb.equal(root.get("name"), userSpecification.name())
                );
            }

            if (userSpecification.surname() != null && !userSpecification.surname().isEmpty()) {
                p = cb.and(
                        p,
                        cb.equal(root.get("surname"), userSpecification.surname())
                );
            }

            if (userSpecification.username() != null && !userSpecification.username().isEmpty()) {
                p = cb.and(
                        p,
                        cb.equal(root.get("username"), userSpecification.username())
                );
            }

            if (userSpecification.email() != null && !userSpecification.email().isEmpty()) {
                p = cb.and(
                        p,
                        cb.equal(root.get("email"), userSpecification.email())
                );
            }

            if (userSpecification.minAge() != null) {
                p = cb.and(
                        p,
                        cb.greaterThanOrEqualTo(root.get("age"), userSpecification.minAge())
                );
            }

            if (userSpecification.maxAge() != null) {
                p = cb.and(
                        p,
                        cb.lessThanOrEqualTo(root.get("age"), userSpecification.maxAge())
                );
            }

            if (userSpecification.minHolidayHours() != null) {
                p = cb.and(
                        p,
                        cb.greaterThanOrEqualTo(root.get("holidaysHours"), userSpecification.minHolidayHours())
                );
            }

            if (userSpecification.maxHolidayHours() != null) {
                p = cb.and(
                        p,
                        cb.lessThanOrEqualTo(root.get("holidaysHours"), userSpecification.maxHolidayHours())
                );
            }

            return p;
        });
    }
}
