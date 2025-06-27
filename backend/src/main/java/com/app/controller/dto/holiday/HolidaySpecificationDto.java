package com.app.controller.dto.holiday;

import com.app.model.Status;
import com.app.persistence.specification.model.HolidaySpecification;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) used to encapsulate filtering criteria
 * for querying holiday requests based on various parameters.
 *
 * @param id        the unique identifier of the holiday request (optional)
 * @param userId    the ID of the user who submitted the holiday request (optional)
 * @param startDate the start date and time of the holiday (optional)
 * @param endDate   the end date and time of the holiday (optional)
 * @param status    the status of the holiday request (optional)
 */
public record HolidaySpecificationDto(
        Long id,
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Status status
) {
    /**
     * Converts this DTO into a {@link HolidaySpecification}, which can be
     * used for dynamic querying of holiday requests based on provided criteria.
     *
     * @return a new {@link HolidaySpecification} instance based on this DTO
     */
    public HolidaySpecification toHolidaySpecification() {
        return new HolidaySpecification(id, userId, startDate, endDate, status);
    }
}
