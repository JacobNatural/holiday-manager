package com.app.persistence.specification;

import org.springframework.data.jpa.domain.Specification;

/**
 * Generic interface for building dynamic {@link Specification} filters for JPA entities.
 *
 * @param <T> the type of the JPA entity to filter
 * @param <U> the type of the specification model containing filtering criteria
 */
public interface FilterSpecification<T, U> {

    /**
     * Builds a dynamic {@link Specification} for the given specification criteria.
     *
     * @param specification an object containing filter criteria
     * @return a JPA {@link Specification} to be used in repository queries
     */
    Specification<T> dynamicFilter(U specification);
}
