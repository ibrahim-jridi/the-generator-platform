package com.pfe.web.rest;


import com.pfe.service.impl.AuthorityServiceImpl;
import com.pfe.service.impl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class IntegrationTestConfig {

    @MockBean
    private AuthorityServiceImpl authorityServiceImpl;

    @MockBean
    private UserServiceImpl internalUserServiceImpl;

    @PostConstruct
    public void postConstruct() {
        Mockito.doNothing().when(this.authorityServiceImpl).syncAuthorities();
        Mockito.doNothing().when(this.internalUserServiceImpl).syncAdminUser();
    }
}
