package com.pfe.config;

import com.pfe.feignServices.UserFeignClient;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);
  private UUID rootId;
  private final UserFeignClient userFeignClient;

  public ApplicationStartUp(UserFeignClient userFeignClient) {
    this.userFeignClient = userFeignClient;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    this.rootId = this.userFeignClient.getRootId();
    if (this.rootId != null) {
      Constants.setRootId(this.rootId);
      log.info("Initialized ROOT_ID: {}", this.rootId);
    } else {
      log.error("Initialized ROOT_ID failed");
      throw new RuntimeException();
    }

  }

}
