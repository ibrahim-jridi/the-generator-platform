package com.pfe.web.rest;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

import com.pfe.IntegrationTest;
import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import com.pfe.service.WaitingListService;
import com.pfe.service.dto.WaitingListDTO;
import com.pfe.service.dto.response.WaitingListResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestPropertySource(properties = "bs-app.endpoint.external-api-url=http://localhost:8088")
public class OpenResourceIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WaitingListService waitingListService;

    private WaitingListDTO waitingListDTO;

    private static final String ENTITY_API_URL = "/api/v1/open-api";

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
    void getAllWaitingListDisplay_ShouldReturnListOfWaitingListResponse() throws Exception {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        WaitingListDTO dto1 = new WaitingListDTO();
        dto1.setIdUser(userId1);
        dto1.setGovernorate(Governorate.ARIANA);
        dto1.setDelegation("TUNIS");
        dto1.setCategory(Category.CATEGORY_A);
        dto1.setStatus(StatusWaitingList.REGISTRED);
        dto1.setRank(1);

        WaitingListDTO dto2 = new WaitingListDTO();
        dto2.setIdUser(userId2);
        dto2.setGovernorate(Governorate.ARIANA);
        dto2.setDelegation("SFAX");
        dto2.setCategory(Category.CATEGORY_B);
        dto2.setStatus(StatusWaitingList.REGISTRED);
        dto2.setRank(2);

        WaitingListResponse response1 = new WaitingListResponse(dto1, "John", "Doe");
        WaitingListResponse response2 = new WaitingListResponse(dto2, "Jane", "Smith");

        List<WaitingListResponse> expectedResponses = Arrays.asList(response1, response2);

        when(waitingListService.getAllWaitingList()).thenReturn(expectedResponses);

        // When & Then
        mockMvc.perform(get(ENTITY_API_URL + "/get-all-waiting-list")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].waitingList.idUser").value(userId1.toString()))
            .andExpect(jsonPath("$[0].waitingList.governorate").value("ARIANA"))
            .andExpect(jsonPath("$[0].waitingList.delegation").value("TUNIS"))
            .andExpect(jsonPath("$[0].waitingList.category").value("CATEGORY_A"))
            .andExpect(jsonPath("$[0].waitingList.status").value("REGISTRED"))
            .andExpect(jsonPath("$[0].waitingList.rank").value(1))
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[1].waitingList.idUser").value(userId2.toString()))
            .andExpect(jsonPath("$[1].waitingList.governorate").value("ARIANA"))
            .andExpect(jsonPath("$[1].waitingList.delegation").value("SFAX"))
            .andExpect(jsonPath("$[1].waitingList.category").value("CATEGORY_B"))
            .andExpect(jsonPath("$[1].waitingList.status").value("REGISTRED"))
            .andExpect(jsonPath("$[1].waitingList.rank").value(2))
            .andExpect(jsonPath("$[1].firstName").value("Jane"))
            .andExpect(jsonPath("$[1].lastName").value("Smith"));

        verify(waitingListService, times(1)).getAllWaitingList();
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getAllWaitingListDisplay_ShouldReturnEmptyList_WhenNoDataExists() throws Exception {
        // Given
        when(waitingListService.getAllWaitingList()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get(ENTITY_API_URL + "/get-all-waiting-list")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(0)));

        verify(waitingListService, times(1)).getAllWaitingList();
    }

}
