package com.app.service.impl;

import com.app.controller.dto.holiday.CreateHolidayDto;
import com.app.controller.dto.holiday.HolidayDto;
import com.app.controller.dto.holiday.HolidaySpecificationDto;
import com.app.persistence.Repository.HolidayRepository;
import com.app.persistence.Repository.UserRepository;
import com.app.model.Status;
import com.app.persistence.entity.HolidayEntity;
import com.app.persistence.specification.FilterSpecification;
import com.app.persistence.specification.impl.HolidayFilterSpecificationImpl;
import com.app.persistence.specification.model.HolidaySpecification;
import com.app.security.service.TokenService;
import com.app.service.HolidayService;
import com.app.validate.CreateHolidayDtoValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service responsible for handling business logic related to holidays.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final UserRepository userRepository;
    private final HolidayRepository holidayRepository;
    private final FilterSpecification<HolidayEntity, HolidaySpecification> filterSpecification;
    private final HolidayFilterSpecificationImpl holidayFilterSpecificationImpl;
    private final TokenService tokenService;
    private final CreateHolidayDtoValidator createHolidayDtoValidator;

    /**
     * Creates a new holiday request for a user.
     *
     * @param createHolidayDto Data transfer object containing holiday details.
     * @param token            Authentication token to identify the user.
     * @return The ID of the created holiday entity.
     * @throws IllegalArgumentException if the holiday overlaps with an existing one or
     *                                  the user doesn't have enough holiday hours.
     * @throws EntityNotFoundException  if the user is not found.
     */
    @Override
    public Long createHoliday(CreateHolidayDto createHolidayDto, String token) {
        createHolidayDtoValidator.validate(createHolidayDto);

        var userID = tokenService.id(token);

        var userEntity = userRepository
                .findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        if (holidayRepository.isHolidayAllow(userID, createHolidayDto.startDate(), createHolidayDto.endDate())) {
            throw new IllegalArgumentException("Holiday already exists.");
        }

        var holidayHours = getHoursBetween(createHolidayDto.startDate(), createHolidayDto.endDate());

        if (holidayHours > userEntity.getHolidaysHours()) {
            throw new IllegalArgumentException("You have only %s holiday hours, you applied for %s hours."
                    .formatted(userEntity.getHolidaysHours(), holidayHours));
        }

        userRepository.save(userEntity.UserEntityWithHolidays(-holidayHours));
        var holidayEntity = holidayRepository.save(createHolidayDto.toEntity().withUserEntity(userEntity));

        return holidayEntity.getId();
    }

    /**
     * Changes the status of an existing holiday request.
     *
     * @param id     The ID of the holiday entity.
     * @param status The new status to be set.
     * @return The ID of the updated holiday entity.
     * @throws IllegalArgumentException if the status parameter is null.
     * @throws EntityNotFoundException  if the holiday or user is not found.
     */
    @Override
    public Long changeStatus(Long id, Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        var holidayEntity = holidayRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Holiday not found."));

        var approvedHolidayEntity = holidayRepository.save(holidayEntity.withStatus(status));

        // If the holiday is rejected, return the holiday hours back to the user
        if (status.equals(Status.REJECTED)) {
            var userEntity = userRepository.findById(approvedHolidayEntity.getUserEntity().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found."));

            var holidayHours = getHoursBetween(holidayEntity.getStartDate(), holidayEntity.getEndDate());
            userRepository.save(userEntity.UserEntityWithHolidays(holidayHours));
        }

        return approvedHolidayEntity.getId();
    }

    /**
     * Retrieves a holiday DTO by its ID.
     *
     * @param id The ID of the holiday.
     * @return HolidayDto containing holiday details.
     * @throws EntityNotFoundException if the holiday is not found.
     */
    public HolidayDto getHolidayDto(Long id) {
        return holidayRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Holiday not found."))
                .toHolidayDto();
    }

    /**
     * Retrieves holidays for a user within a date range.
     *
     * @param token     Authentication token to identify the user.
     * @param startDate Start date of the range.
     * @param endDate   End date of the range.
     * @return List of HolidayDto objects matching the criteria.
     */
    @Override
    public List<HolidayDto> getHolidaysByDate(String token, LocalDateTime startDate, LocalDateTime endDate) {
        var userId = tokenService.id(token);
        return getHolidays(new HolidaySpecificationDto(null, userId, startDate, endDate, null));
    }

    /**
     * Retrieves holidays matching given specification criteria.
     *
     * @param holidaySpecificationDto Specification DTO with filter criteria.
     * @return List of HolidayDto objects matching the filter.
     * @throws IllegalArgumentException if the specification DTO is null.
     */
    @Override
    public List<HolidayDto> getHolidays(HolidaySpecificationDto holidaySpecificationDto) {
        if (holidaySpecificationDto == null) {
            throw new IllegalArgumentException("HolidaySpecificationDto cannot be null.");
        }
        return holidayRepository
                .findAll(holidayFilterSpecificationImpl
                        .dynamicFilter(holidaySpecificationDto.toHolidaySpecification()))
                .stream()
                .map(HolidayEntity::toHolidayDto)
                .toList();
    }

    /**
     * Calculates the number of holiday hours between two timestamps,
     * excluding weekends and validating holiday length per day.
     *
     * @param startTime Start date and time of the holiday.
     * @param endTime   End date and time of the holiday.
     * @return Number of holiday hours.
     * @throws IllegalArgumentException if holiday hours per day exceed 8.
     */
    private static Long getHoursBetween(LocalDateTime startTime, LocalDateTime endTime) {

        var days = ChronoUnit.DAYS.between(startTime, endTime);
        if (days == 0) {
            var hours = ChronoUnit.HOURS.between(startTime, endTime);
            if (hours > 8) {
                throw new IllegalArgumentException("Wrong hours time for day holiday.");
            }
            return hours;
        }

        var weeks = days / 7;
        var daysWithoutWeekends = weeks * 5;

        var fromDay = startTime.plusDays(weeks * 7);

        for (; fromDay.isBefore(endTime); fromDay = fromDay.plusDays(1)) {
            if (fromDay.getDayOfWeek() != DayOfWeek.SUNDAY && fromDay.getDayOfWeek() != DayOfWeek.SATURDAY) {
                daysWithoutWeekends++;
            }
        }

        return daysWithoutWeekends * 8;
    }
}
