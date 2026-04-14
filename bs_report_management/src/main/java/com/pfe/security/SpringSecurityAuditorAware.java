package com.pfe.security;

import com.pfe.config.Constants;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

    private static final Logger log = LoggerFactory.getLogger(SpringSecurityAuditorAware.class);

    @Override
    public Optional<UUID> getCurrentAuditor() {
        String currentUser = SecurityUtils.getUserIdFromCurrentUser();
        if (currentUser != null) {
            log.info("currentuser : {}", currentUser);
            return Optional.of(UUID.fromString(currentUser));
        } else {
            return Optional.of(Constants.getRootId());
        }
    }
}
