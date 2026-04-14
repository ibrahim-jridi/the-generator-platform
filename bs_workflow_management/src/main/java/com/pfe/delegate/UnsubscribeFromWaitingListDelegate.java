package com.pfe.delegate;

import com.pfe.feignServices.WaitingListService;
import com.pfe.security.SecurityUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UnsubscribeFromWaitingListDelegate implements JavaDelegate {

    private final WaitingListService waitingListService;
    private static final Logger log = LoggerFactory.getLogger(UnsubscribeFromWaitingListDelegate.class);

    public UnsubscribeFromWaitingListDelegate(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Starting execution of UnsubscribeFromWaitingListDelegate");

        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();
            log.debug("Retrieved user ID from security context: {}", userId);

            UUID userUuid = UUID.fromString(userId);
            log.debug("Converted user ID to UUID: {}", userUuid);

            waitingListService.unsubscribe(userUuid);
            log.info("Successfully unsubscribed user [{}] from waiting list", userUuid);

        } catch (Exception e) {
            log.error("Error occurred during execution of UnsubscribeFromWaitingListDelegate: {}", e.getMessage(), e);
            throw e;
        }
    }
}
