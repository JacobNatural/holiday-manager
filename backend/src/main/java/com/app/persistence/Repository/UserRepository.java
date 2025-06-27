package com.app.persistence.Repository;

import com.app.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * Repository interface for managing {@link UserEntity} entities.
 * Provides standard CRUD operations via {@link JpaRepository} and supports
 * complex queries via {@link JpaSpecificationExecutor}.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the found {@link UserEntity}, or empty if no user found
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an {@link Optional} containing the found {@link UserEntity}, or empty if no user found
     */
    Optional<UserEntity> findByEmail(String email);
}
