package com.app.persistence.entity;

import com.app.controller.dto.holiday.HolidayDto;
import com.app.model.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Entity representing a holiday request or record.
 * Maps to the "holidays" table with a unique constraint on start and end dates.
 * Extends {@link BaseEntity} to inherit the identifier.
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "holidays",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"startDate", "endDate"})
        })
public class HolidayEntity extends BaseEntity {

    /**
     * The start date and time of the holiday.
     */
    private LocalDateTime startDate;

    /**
     * The end date and time of the holiday.
     */
    private LocalDateTime endDate;

    /**
     * The status of the holiday request, stored as a string.
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * The user who requested or is associated with this holiday.
     * Eagerly fetched for immediate availability.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    /**
     * Creates a new HolidayEntity with the given user entity, copying existing fields.
     *
     * @param userEntity the user to associate with this holiday
     * @return a new HolidayEntity instance with updated user
     */
    public HolidayEntity withUserEntity(UserEntity userEntity) {
        return HolidayEntity
                .builder()
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .userEntity(userEntity)
                .build();
    }

    /**
     * Creates a new HolidayEntity with the given status, copying existing fields including id.
     *
     * @param status the new status to set
     * @return a new HolidayEntity instance with updated status
     */
    public HolidayEntity withStatus(Status status) {
        return HolidayEntity.builder()
                .id(id)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .userEntity(userEntity)
                .build();
    }

    /**
     * Converts this entity to its corresponding DTO.
     *
     * @return a {@link HolidayDto} representing this holiday
     */
    public HolidayDto toHolidayDto() {
        return new HolidayDto(id, userEntity.getId(), startDate, endDate, status);
    }
}
