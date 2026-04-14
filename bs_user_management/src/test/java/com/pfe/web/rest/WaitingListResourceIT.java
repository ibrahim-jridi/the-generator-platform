package com.pfe.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pfe.IntegrationTest;
import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import com.pfe.service.WaitingListService;
import com.pfe.service.dto.WaitingListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class WaitingListResourceIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaitingListService waitingListService;

    private WaitingListDTO waitingListDTO;

    private static final String ENTITY_API_URL = "/api/v1/waiting-list";

    @BeforeEach
    void setUp() {
        waitingListDTO = new WaitingListDTO();
        waitingListDTO.setIdUser(UUID.randomUUID());
        waitingListDTO.setGovernorate(Governorate.ARIANA);
        waitingListDTO.setDelegation("TUNIS");
        waitingListDTO.setCategory(Category.CATEGORY_A);
        waitingListDTO.setStatus(StatusWaitingList.REGISTRED);
        waitingListDTO.setRank(1);
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void createWaitingList_ShouldReturnOk_WhenValidRequest() throws Exception {
        when(waitingListService.save(any(WaitingListDTO.class))).thenReturn(waitingListDTO);
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        mockMvc.perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(waitingListDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.idUser").value(waitingListDTO.getIdUser().toString()))
            .andExpect(jsonPath("$.governorate").value(waitingListDTO.getGovernorate().toString()))
            .andExpect(jsonPath("$.delegation").value(waitingListDTO.getDelegation()))
            .andExpect(jsonPath("$.category").value(waitingListDTO.getCategory().toString()))
            .andExpect(jsonPath("$.status").value(waitingListDTO.getStatus().toString()));

        verify(waitingListService, times(1)).save(any(WaitingListDTO.class));
    }


    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void createWaitingList_ShouldReturnBadRequest_WhenIdIsNotNull() throws Exception {
        waitingListDTO.setId(UUID.randomUUID());

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        mockMvc.perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(waitingListDTO)))
            .andExpect(status().isBadRequest());
        verify(waitingListService, never()).save(any(WaitingListDTO.class));
    }


    @Test
    void createWaitingList_ShouldReturnForbidden_WhenUserHasNoPermission() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        mockMvc.perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(waitingListDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void createWaitingList_ShouldReturnUnauthorized_WhenUserIsNotAuthenticated() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc.perform(post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(waitingListDTO)))
            .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void isUserUnsubscribed_ShouldReturnTrue_WhenUserIsUnsubscribed() throws Exception {

        when(waitingListService.isUserUnsubscribedOrNotExist()).thenReturn(true);

        mockMvc.perform(get(ENTITY_API_URL+"/is-unsubscribed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(waitingListService, times(1)).isUserUnsubscribedOrNotExist();
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void isUserUnsubscribed_ShouldReturnFalse_WhenUserIsStillRegistered() throws Exception {

        when(waitingListService.isUserUnsubscribedOrNotExist()).thenReturn(false);

        mockMvc.perform(get(ENTITY_API_URL+"/is-unsubscribed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false")); // Vérifie que la réponse est bien "false"

        verify(waitingListService, times(1)).isUserUnsubscribedOrNotExist();
    }
    @Test
    @WithUnauthenticatedMockUser
    void isUserUnsubscribed_ShouldReturnUnauthorized_WhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get(ENTITY_API_URL+"/is-unsubscribed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(waitingListService, never()).isUserUnsubscribedOrNotExist();
    }

    @Test
    @WithMockUser(username = "test-user", authorities = {"OTHER_ROLE"})
    void isUserUnsubscribed_ShouldReturnForbidden_WhenUserHasNoPermission() throws Exception {
        mockMvc.perform(get(ENTITY_API_URL+"/is-unsubscribed")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(waitingListService, never()).isUserUnsubscribedOrNotExist();
    }
}
