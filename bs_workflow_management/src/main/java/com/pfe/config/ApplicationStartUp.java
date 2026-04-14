package com.pfe.config;

import com.pfe.feignServices.UserService;
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
  private final UserService userService;

  public ApplicationStartUp(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    this.rootId = this.userService.getRootId();
    if (this.rootId != null) {
      Constants.setRootId(this.rootId);
      log.info("Initialized ROOT_ID: {}", this.rootId);
    } else {
      log.error("Initialized ROOT_ID failed");
      throw new RuntimeException();
    }

  }

}
