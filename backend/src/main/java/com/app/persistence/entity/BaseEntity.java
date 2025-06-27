package com.app.persistence.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * Base entity class to be extended by all JPA entities.
 *
 * Provides a common identifier property `id` with auto-generated value,
 * and overrides {@link #equals(Object)} and {@link #hashCode()} methods
 * to ensure correct entity equality semantics, including support for Hibernate proxies.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    /**
     * The unique identifier of the entity.
     */
    @Id
    @GeneratedValue
    protected Long id;

    /**
     * Determines equality of entities based on their identifier.
     * Takes into account Hibernate proxies to avoid issues with lazy loading.
     *
     * @param o the object to compare to
     * @return true if entities are the same or have the same non-null id; false otherwise
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        BaseEntity that = (BaseEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    /**
     * Returns a hash code consistent with the equals method.
     * Uses the class hash code, accounting for Hibernate proxy classes.
     *
     * @return hash code of the entity
     */
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
