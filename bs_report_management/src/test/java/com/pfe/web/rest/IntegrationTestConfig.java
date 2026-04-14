package com.pfe.web.rest;

import com.pfe.feignServices.UserService;
import jakarta.annotation.PostConstruct;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class IntegrationTestConfig {

    @MockBean
    private UserService userService;

    @PostConstruct
    public void postConstruct() {
        Mockito.when(this.userService.getRootId()).thenReturn(TestUtil.getRootID());
    }
}

