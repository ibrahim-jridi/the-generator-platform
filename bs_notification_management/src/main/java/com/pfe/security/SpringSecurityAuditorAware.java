package com.pfe.security;

import com.pfe.config.Constants;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<UUID> {

  @Override
  public Optional<UUID> getCurrentAuditor() {
    String currentUser = SecurityUtils.getUserIdFromCurrentUser();
    if (currentUser != null) {
      return Optional.of(UUID.fromString(currentUser));
    } else {
      return Optional.of((Constants.getRootId()));
    }
  }

}
