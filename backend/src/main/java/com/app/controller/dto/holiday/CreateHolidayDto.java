package com.app.controller.dto.holiday;

import com.app.model.Status;
import com.app.persistence.entity.HolidayEntity;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) used to create a new holiday request.
 * Contains the start and end dates of the holiday.
 *
 * @param startDate the start date and time of the holiday
 * @param endDate   the end date and time of the holiday
 */
public record CreateHolidayDto(LocalDateTime startDate, LocalDateTime endDate) {

    /**
     * Converts this DTO into a {@link HolidayEntity}.
     * Sets the status of the holiday to {@link Status#PROCESSING} by default.
     *
     * @return a new instance of {@link HolidayEntity} based on this DTO
     */
    public HolidayEntity toEntity() {
        return HolidayEntity
                .builder()
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.PROCESSING)
                .build();
    }
}
