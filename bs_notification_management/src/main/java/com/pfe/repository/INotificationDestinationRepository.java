package com.pfe.repository;

import com.pfe.domain.NotificationDestination;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for the NotificationDestination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface INotificationDestinationRepository extends BaseRepository<NotificationDestination, UUID> {

    List<NotificationDestination> findByNotificationIdAndDeletedFalse(UUID id);
    @Modifying
    @Query("DELETE FROM NotificationDestination nd WHERE nd.id IN :oldNotificationDestination")
    void deleteoldNotificationDestinationByID(@Param("oldNotificationDestination") Set<UUID> oldNotificationDestination);

}
