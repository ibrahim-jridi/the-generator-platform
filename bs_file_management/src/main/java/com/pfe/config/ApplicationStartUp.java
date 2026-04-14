package com.pfe.config;

import com.pfe.feignServices.UserFeignClient;

import com.pfe.services.servicesImplementation.FolderServiceImpl;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartUp.class);
    private UUID rootId;
    private final UserFeignClient userFeignClient;
    private final FolderServiceImpl folderService;

    public ApplicationStartUp(UserFeignClient userFeignClient, FolderServiceImpl folderService) {
        this.userFeignClient = userFeignClient;
        this.folderService = folderService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.rootId = this.userFeignClient.getRootId();
        if (this.rootId != null) {
            Constants.setRootId(this.rootId);
            log.info("Initialized ROOT_ID: {}", this.rootId);
            try {
                this.folderService.initializeBucketsAndFolders();
                log.info("Initializing buckets and folders");
            } catch (Exception e) {
                log.error("Initializing Buckets and folders failed");
                throw new RuntimeException(e);
            }
        } else {
            log.error("Initialized ROOT_ID failed");
            throw new RuntimeException();
        }

    }


}
