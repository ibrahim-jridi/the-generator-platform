package com.pfe.config;

import com.pfe.service.impl.AuthorityServiceImpl;
import com.pfe.service.impl.UserServiceImpl;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);
    private final AuthorityServiceImpl authorityServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @Value("${app.startup.enabled:true}")
    private boolean startupEnabled;

    public ApplicationStartup(AuthorityServiceImpl authorityServiceImpl,
        UserServiceImpl userServiceImpl) {
        this.authorityServiceImpl = authorityServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (startupEnabled) {
            log.info(" Startup tasks are disabled");
            return;
        }

        try {
            userServiceImpl.fixKeycloakIdsByUsername();
            log.info("fixKeycloakId done");

            authorityServiceImpl.syncAuthorities();
            log.info("syncAuthorities done");

            userServiceImpl.syncAdminUser();
            log.info("syncAdminUser done");

            userServiceImpl.syncDefaultUsers();
            log.info("syncDefaultUsers done");

//            userServiceImpl.assignEctdRoleToIndustryAccount();
//            log.info("assignEctdRoleToIndustryAccount done");

            log.info("Authorities synced to Keycloak successfully.");

        } catch (Exception e) {
            log.error("Startup failed: {}", e.getMessage(), e);
            throw new RuntimeException("Keycloak sync failed", e);
        }
    }

    @PreDestroy
    public void onDestroy() {
        executor.shutdown();
    }
}
