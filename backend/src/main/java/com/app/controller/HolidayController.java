package com.app.controller;

import com.app.controller.dto.holiday.CreateHolidayDto;
import com.app.controller.dto.holiday.HolidayDto;
import com.app.controller.dto.holiday.HolidaySpecificationDto;
import com.app.controller.dto.ResponseDto;
import com.app.model.Status;
import com.app.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for handling holiday-related operations.
 */
@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    /**
     * Creates a new holiday request.
     *
     * @param createHolidayDto DTO with holiday details.
     * @param token            Access token from cookie for authentication.
     * @return ID of the created holiday.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Long> createHoliday(
            @RequestBody CreateHolidayDto createHolidayDto,
            @CookieValue("AccessToken") String token) {
        return new ResponseDto<>(holidayService.createHoliday(createHolidayDto, token));
    }

    /**
     * Retrieves holidays filtered by optional start and end dates.
     *
     * Note: @Param is intended for repository query parameters,
     * better to use @RequestParam for controller method parameters.
     *
     * @param token     Access token from cookie for authentication.
     * @param startDate Optional start date filter.
     * @param endDate   Optional end date filter.
     * @return List of holidays matching the date criteria.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<HolidayDto>> getHolidays(
            @CookieValue("AccessToken") String token,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        return new ResponseDto<>(holidayService.getHolidaysByDate(token, startDate, endDate));
    }

    /**
     * Changes the status of a holiday.
     *
     * @param holidayId ID of the holiday to update.
     * @param status    New status to apply.
     * @return ID of the updated holiday.
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> changeStatus(@RequestParam Long holidayId, @RequestParam Status status) {
        return new ResponseDto<>(holidayService.changeStatus(holidayId, status));
    }

    /**
     * Retrieves holidays filtered by complex criteria.
     *
     * @param holidaySpecificationDto DTO specifying filter criteria.
     * @return List of holidays matching the filter criteria.
     */
    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<HolidayDto>> getHolidays(
            @RequestBody HolidaySpecificationDto holidaySpecificationDto) {
        return new ResponseDto<>(holidayService.getHolidays(holidaySpecificationDto));
    }
}
