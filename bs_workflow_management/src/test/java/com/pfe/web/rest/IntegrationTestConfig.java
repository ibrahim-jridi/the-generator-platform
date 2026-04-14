package com.pfe.web.rest;

import com.pfe.dto.FormDTO;
import com.pfe.dto.UserDTO;
import com.pfe.feignServices.FormService;
import com.pfe.feignServices.UserService;
import com.pfe.security.SecurityUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestConfiguration
public class IntegrationTestConfig {

    @MockBean
    private UserService userService;

    @MockBean
    private FormService formService;

    private MockedStatic<SecurityUtils> utilities;

    @PostConstruct
    public void postConstruct() {
        try (MockedStatic<SecurityUtils> utilities = Mockito.mockStatic(SecurityUtils.class)) {
            utilities.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(TestUtil.getRootID().toString());
        }

        Mockito.doReturn(TestUtil.getRootID()).when(this.userService).getRootId();

        UserDTO mockUser = new UserDTO();
        mockUser.setId(TestUtil.getRootID());
        mockUser.setUsername("test-user");
        ResponseEntity<UserDTO> mockResponse = ResponseEntity.ok(mockUser);
        when(userService.getuser(any(UUID.class))).thenReturn(mockResponse);

        FormDTO mockForm = new FormDTO();
        mockForm.setId(UUID.fromString("5f4e4a59-0ec4-4ca4-a364-a673a573d7ec"));
        ResponseEntity<FormDTO> mockFormResponse = ResponseEntity.ok(mockForm);
        when(formService.getForm(any(UUID.class))).thenReturn(mockFormResponse);
    }

    @PreDestroy
    public void preDestroy() {
        if (utilities != null) {
            utilities.close();
        }
    }
}
