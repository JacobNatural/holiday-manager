package com.app.validate;

import com.app.controller.dto.holiday.CreateHolidayDto;
import com.app.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.stream.Collectors;

@Component
public class CreateHolidayDtoValidator implements Validator<CreateHolidayDto> {

    /**
     * Validates the CreateHolidayDto object by checking:
     * - If the DTO is null.
     * - If the start date is null.
     * - If the end date is null.
     * - If the start date is after the end date.
     *
     * If any validation rule is violated, a ValidationException is thrown containing all error messages.
     *
     * @param createHolidayDto The DTO containing holiday start and end dates
     * @throws ValidationException if any validation error is found
     */
    @Override
    public void validate(CreateHolidayDto createHolidayDto) {
        var errors = new HashMap<String, String>();
        if (createHolidayDto == null) {
            errors.put("general", "Fill out the form with your data.");
        } else {
            if (createHolidayDto.startDate() == null) {
                errors.put("start date", "Start date cannot be null.");
            }

            if (createHolidayDto.endDate() == null) {
                errors.put("end date", "End date cannot be null.");
            }

            if (createHolidayDto.startDate() != null && createHolidayDto.endDate() != null
                    && createHolidayDto.startDate().isAfter(createHolidayDto.endDate())) {
                errors.put("start end date", "Start date cannot be after end date.");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors
                    .values()
                    .stream()
                    .collect(Collectors.joining("\n")));
        }
    }
}
