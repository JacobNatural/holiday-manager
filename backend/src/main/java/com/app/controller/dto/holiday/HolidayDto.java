package com.app.controller.dto.holiday;

import com.app.model.Status;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a holiday request with full details.
 * Includes identifiers, dates, and the current status of the request.
 *
 * @param id        the unique identifier of the holiday request
 * @param userId    the ID of the user who submitted the holiday request
 * @param startDate the start date and time of the holiday
 * @param endDate   the end date and time of the holiday
 * @param status    the current status of the holiday request
 */
public record HolidayDto(
        Long id,
        Long userId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Status status
) {
}
