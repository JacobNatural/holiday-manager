package com.app.persistence.Repository;

import com.app.persistence.entity.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

/**
 * Repository interface for managing {@link HolidayEntity} entities.
 * Extends {@link JpaRepository} for basic CRUD operations and
 * {@link JpaSpecificationExecutor} for complex queries.
 */
public interface HolidayRepository extends JpaRepository<HolidayEntity, Long>, JpaSpecificationExecutor<HolidayEntity> {

    /**
     * Checks if there is any existing holiday for a given user that overlaps
     * with the specified start and end dates, excluding holidays with status REJECTED.
     *
     * <p>The method returns {@code true} if at least one holiday exists that:
     * <ul>
     *     <li>Belongs to the user identified by {@code userId}</li>
     *     <li>Has a date range that overlaps with the interval between {@code startDate} and {@code endDate}</li>
     *     <li>Is not in REJECTED status</li>
     * </ul>
     * Otherwise, it returns {@code false}.
     *
     * @param userId the ID of the user whose holidays to check
     * @param startDate the start date of the requested holiday period
     * @param endDate the end date of the requested holiday period
     * @return {@code true} if there is an overlapping holiday for the user (excluding rejected holidays), {@code false} otherwise
     */
    @Query("""
            select case when count(h) > 0 then true else false end from HolidayEntity h
            where h.userEntity.id = :userId
            and (:startDate between h.startDate and h.endDate
            or :endDate between h.startDate and h.endDate)
            and h.status != 'REJECTED'
            """)
    boolean isHolidayAllow(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
