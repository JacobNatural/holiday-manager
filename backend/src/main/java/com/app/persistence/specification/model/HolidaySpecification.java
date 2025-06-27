package com.app.persistence.specification.model;

import com.app.model.Status;
import java.time.LocalDateTime;

/**
 * Specification model used to filter {@link com.app.persistence.entity.HolidayEntity}
 * entities in a dynamic and type-safe way.
 *
 * <p>This record is typically used in conjunction with {@code Specification<HolidayEntity>}
 * to construct dynamic queries based on the provided fields.</p>
 *
 * @param id         the unique identifier of the holiday (optional filter)
 * @param userId     the ID of the user associated with the holiday (optional filter)
 * @param startDate  the start date for filtering holidays that start after or on this date
 * @param endDate    the end date for filtering holidays that end before or on this date
 * @param status     the status of the holiday (e.g., APPROVED, PENDING, REJECTED)
 */
public record HolidaySpecification(
        Long id,
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Status status
) {}
