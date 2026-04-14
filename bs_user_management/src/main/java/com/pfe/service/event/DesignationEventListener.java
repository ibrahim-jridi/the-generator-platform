package com.pfe.service.event;

import com.pfe.config.Constants;
import com.pfe.domain.DesignationsList;
import com.pfe.repository.DesignationsListRepository;
import com.pfe.repository.UserRepository;
import com.pfe.service.UserService;
import com.pfe.service.dto.UserDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component

public class DesignationEventListener {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DesignationEventListener.class);

    private final UserRepository userRepository;
    private final UserService userService;

    private final DesignationsListRepository designationsListRepository;


    public DesignationEventListener(ApplicationEventPublisher applicationEventPublisher, UserRepository userRepository, UserService userService, DesignationsListRepository designationsListRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.userRepository = userRepository;
        this.userService = userService;
        this.designationsListRepository = designationsListRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDesignationDeleted(DesignationDeletedEvent event) {
        try {
            // Call ECTD/Keycloak updates after the transaction is committed
            handleEctdAfterDesignationDeletion(event.getDesignatedUser());

            log.info("ECTD attributes updated successfully for user {} after designation {} deletion"
            );
        } catch (Exception e) {
            log.error("Failed to update ECTD attributes for user {} after designation deletion: {}",
                event.getDesignatedUser().getUsername(), e.getMessage(), e);
            // Handle the error appropriately (retry, alert, etc.)
        }
    }
    public void handleEctdAfterDesignationDeletion(UserDTO designated) {
        log.info("Handling ECTD after designation deletion for user {}", designated.getId());

        List<DesignationsList> designations = designationsListRepository
            .findByDesignatedUserIdAndDeletedFalse(designated.getId());

        // Check if user has ECTD role
        boolean hasEctdRole = designated.getRoles().stream()
            .anyMatch(role -> Constants.BS_ROLE_ECTD.equals(role.getLabel()));

        if (!hasEctdRole) {
            log.info("User {} has no ECTD role → nothing to update", designated.getUsername());
            return;
        }

        if (designations.isEmpty())  {
            //  remove everything
            log.info("No remaining designations for user {} - removing ECTD completely", designated.getUsername());
            this.userService.removeEctdCompletely(designated);
        } else {
            //Update Applicant and submitter in keycloak
            log.info("User {} still has {} designations - recalculating attributes",
                designated.getUsername(), designations.size());
            this.userService.updateEctdUserAttributes(designated.getUsername());
        }

        log.info("ECTD handling completed for user {}", designated.getUsername());
    }
}
