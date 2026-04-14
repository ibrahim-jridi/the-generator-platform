package com.pfe.repository;

import com.pfe.domain.NotificationPushed;

import com.pfe.service.dto.NotificationPushedDTO;
import java.util.List;
import java.util.UUID;

import com.pfe.domain.enums.BroadcastChannel;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NotificationPushed entity.
 */
@Repository
public interface INotificationPushedRepository extends BaseRepository<NotificationPushed, UUID> {

    List<NotificationPushed> findByRecipientIdAndEventNotification_Notification_BroadcastChannelNotAndDeletedFalse(
        UUID recipientId,
        BroadcastChannel broadcastChannel,
        Sort sort
    );

    @Query("SELECT new com.pfe.service.dto.NotificationPushedDTO( " +
        "p.notificationDate, p.recipientId, n.name, p.description, p.message, e.taskId, n.fileName, p.isSeen, n.broadcastChannel) " +
        "FROM NotificationPushed p " +
        "LEFT JOIN p.eventNotification e " +
        "LEFT JOIN e.notification n " +
        "WHERE e.taskId = :taskId AND p.deleted = false")
    Page<NotificationPushedDTO> findByTaskId(@Param("taskId") String taskId, Pageable pageable);

}
