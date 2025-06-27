package com.app.persistence.specification.impl;

import com.app.persistence.entity.HolidayEntity;
import com.app.persistence.entity.UserEntity;
import com.app.persistence.specification.FilterSpecification;
import com.app.persistence.specification.model.HolidaySpecification;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link FilterSpecification} for filtering {@link HolidayEntity} instances
 * dynamically based on the provided {@link HolidaySpecification} criteria.
 */
@Component
public class HolidayFilterSpecificationImpl implements FilterSpecification<HolidayEntity, HolidaySpecification> {

    /**
     * Builds a dynamic JPA {@link Specification} for {@link HolidayEntity} based on
     * the non-null fields of the given {@link HolidaySpecification}.
     *
     * <p>Supports filtering by:
     * <ul>
     *   <li>startDate - filters holidays with startDate &gt;= specified startDate</li>
     *   <li>endDate - filters holidays with endDate &lt;= specified endDate</li>
     *   <li>status - filters holidays matching the given status</li>
     *   <li>id - filters holidays matching the given id</li>
     *   <li>userId - filters holidays belonging to the user with the given userId</li>
     * </ul>
     *
     * @param specification the filter criteria to apply
     * @return a JPA {@link Specification} representing the combined filter conditions
     */
    @Override
    public Specification<HolidayEntity> dynamicFilter(HolidaySpecification specification) {
        return ((root, query, cb) -> {
            var p = cb.conjunction();

            if (specification.startDate() != null) {
                p = cb.and(
                        p,
                        cb.greaterThanOrEqualTo(root.get("startDate"), specification.startDate())
                );
            }

            if (specification.endDate() != null) {
                p = cb.and(
                        p,
                        cb.lessThanOrEqualTo(root.get("endDate"), specification.endDate())
                );
            }

            if (specification.status() != null) {
                p = cb.and(
                        p,
                        cb.equal(root.get("status"), specification.status())
                );
            }

            if (specification.id() != null) {
                p = cb.and(
                        p,
                        cb.equal(root.get("id"), specification.id())
                );
            }

            if (specification.userId() != null) {
                Join<HolidayEntity, UserEntity> userJoin = root.join("userEntity");
                p = cb.and(
                        p,
                        cb.equal(userJoin.get("id"), specification.userId())
                );
            }

            return p;
        });
    }
}
