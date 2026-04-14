package com.pfe.web.rest;

import com.pfe.feignServices.UserFeignClient;
import jakarta.annotation.PostConstruct;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

@TestConfiguration
public class IntegrationTestConfig {

    @MockBean
    private  UserFeignClient userFeignClient;

    @PostConstruct
    public void postConstruct() {
        Mockito.doReturn(UUID.randomUUID()).when(this.userFeignClient).getRootId();
    }
}
