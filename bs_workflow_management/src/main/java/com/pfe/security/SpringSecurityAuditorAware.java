package com.pfe.security;

import com.pfe.config.Constants;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String currentUser = SecurityUtils.getUserIdFromCurrentUser();
        if (currentUser != null) {
            return Optional.of(currentUser);
        } else {
            return Optional.of(String.valueOf(Constants.getRootId()));
        }
    }
}
