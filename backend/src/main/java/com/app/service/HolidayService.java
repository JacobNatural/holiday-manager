package com.app.service;

import com.app.controller.dto.holiday.CreateHolidayDto;
import com.app.controller.dto.holiday.HolidayDto;
import com.app.controller.dto.holiday.HolidaySpecificationDto;
import com.app.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface HolidayService {

    /**
     * Creates a new holiday request.
     *
     * @param createHolidayDto DTO containing the holiday details to be created
     * @param token            Authentication token of the user creating the holiday
     * @return The ID of the created holiday
     */
    Long createHoliday(CreateHolidayDto createHolidayDto, String token);

    /**
     * Changes the status of an existing holiday request.
     *
     * @param id     The ID of the holiday request to update
     * @param status The new status to set
     * @return The ID of the updated holiday
     */
    Long changeStatus(Long id, Status status);

    /**
     * Retrieves a holiday DTO by its ID.
     *
     * @param id The ID of the holiday request
     * @return The HolidayDto corresponding to the given ID
     */
    HolidayDto getHolidayDto(Long id);

    /**
     * Retrieves a list of holidays matching the provided specification.
     *
     * @param holidaySpecificationDto DTO containing criteria to filter holidays
     * @return A list of HolidayDto matching the specification
     */
    List<HolidayDto> getHolidays(HolidaySpecificationDto holidaySpecificationDto);

    /**
     * Retrieves a list of holidays for the authenticated user between specified dates.
     *
     * @param token     Authentication token of the user
     * @param startDate The start date/time to filter holidays (inclusive)
     * @param endDate   The end date/time to filter holidays (inclusive)
     * @return A list of HolidayDto within the specified date range for the user
     */
    List<HolidayDto> getHolidaysByDate(String token, LocalDateTime startDate, LocalDateTime endDate);
}
