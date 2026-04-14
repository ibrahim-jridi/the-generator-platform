package com.pfe.repository;

import com.pfe.domain.Notification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the Notification entity.
 */
@Repository
public interface INotificationRepository extends BaseRepository<Notification, UUID>{

    Optional<Notification> findByNameAndDeletedFalse(String name);
}
