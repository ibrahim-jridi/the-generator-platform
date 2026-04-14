package com.pfe.service.impl;

import com.pfe.domain.WaitingList;
import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import com.pfe.repository.WaitingListRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.service.UserService;
import com.pfe.service.dto.WaitingListDTO;
import com.pfe.service.dto.response.WaitingListResponse;
import com.pfe.service.dto.response.WaitingListWithNames;
import com.pfe.service.mapper.UserMapper;
import com.pfe.service.mapper.WaitingListMapper;
import java.util.Arrays;
import java.util.Collections;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WaitingListServiceImplTest {
    @Mock
    private WaitingListRepository waitingListRepository;

    @Mock
    private WaitingListMapper waitingListMapper;
    @Mock
    private UserService userService;
    private UUID userId;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private WaitingListServiceImpl waitingListService;

    private WaitingListDTO dto;

    private WaitingList existing;
    @BeforeEach
    void setUp() {
        existing = new WaitingList();
        existing.setIdUser(UUID.randomUUID());
        existing.setCategory(Category.CATEGORY_A);
        existing.setGovernorate(Governorate.TUNIS);
        existing.setDelegation("DelegationA");
        existing.setMunicipality(null);
        existing.setRank(1);
        existing.setStatus(StatusWaitingList.REGISTRED);

        dto = new WaitingListDTO();
        dto.setCategory(Category.CATEGORY_B);
        dto.setGovernorate(Governorate.ARIANA);
        dto.setMunicipality("CommuneB");
        dto.setDelegation(null);
    }


    @Test
    void save_ShouldThrowException_WhenUserAlreadyRegistered() {

        UUID userId = UUID.randomUUID();
        WaitingListDTO dto = new WaitingListDTO();
        dto.setIdUser(userId);
        dto.setStatus(StatusWaitingList.REGISTRED);

        when(this.waitingListRepository.existsByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.waitingListService.save(dto));
        assertEquals("Cet utilisateur est déjà inscrit dans la liste d'attente avec le statut REGISTRED.", exception.getMessage());

        verify(this.waitingListRepository, never()).save(any());
    }

    @Test
    void save_ShouldAssignCorrectRank_WhenStatusIsRegistred() {

        UUID userId = UUID.randomUUID();
        WaitingListDTO dto = new WaitingListDTO();
        dto.setIdUser(userId);
        dto.setStatus(StatusWaitingList.REGISTRED);
        dto.setGovernorate(Governorate.ARIANA);
        dto.setCategory(Category.CATEGORY_A);
        dto.setDelegation("TUNIS");
        dto.setMunicipality("TUNIS");

        WaitingList entity = new WaitingList();
        entity.setIdUser(userId);
        entity.setStatus(StatusWaitingList.REGISTRED);
        entity.setGovernorate(Governorate.ARIANA);
        entity.setCategory(Category.CATEGORY_A);
        entity.setDelegation("TUNIS");
        entity.setMunicipality("TUNIS");

        List<WaitingList> existingWaitingLists = List.of(new WaitingList(), new WaitingList()); // 2 éléments existants

        when(this.waitingListRepository.existsByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(false);
        when(this.waitingListMapper.toEntity(dto)).thenReturn(entity);
        when(this.waitingListRepository.findByCriteriaSortedByCreatedDate(
            dto.getGovernorate(),
            dto.getCategory(),
            dto.getDelegation(),
            dto.getMunicipality(),
            StatusWaitingList.REGISTRED
        )).thenReturn(existingWaitingLists);
        when(this.waitingListRepository.save(any())).thenReturn(entity);
        when(this.waitingListMapper.toDto(any(WaitingList.class))).thenReturn(dto);

        WaitingListDTO result = this.waitingListService.save(dto);

        assertNotNull(result);
        assertEquals(3, entity.getRank());

        verify(this.waitingListRepository).save(entity);
    }

    @Test
    void save_ShouldAssignRankZero_WhenStatusIsNotRegistred() {

        UUID userId = UUID.randomUUID();
        WaitingListDTO dto = new WaitingListDTO();
        dto.setIdUser(userId);
        dto.setStatus(StatusWaitingList.UNSUBSCRIBE);

        WaitingList entity = new WaitingList();
        entity.setStatus(StatusWaitingList.UNSUBSCRIBE);

        when(this.waitingListRepository.existsByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(false);
        when(this.waitingListMapper.toEntity(dto)).thenReturn(entity);
        when(this.waitingListRepository.save(any())).thenReturn(entity);
        when(this.waitingListMapper.toDto(any(WaitingList.class))).thenReturn(dto);


        WaitingListDTO result = this.waitingListService.save(dto);

        assertNotNull(result);
        assertEquals(0, entity.getRank());

        verify(this.waitingListRepository).save(entity);
    }

    @Test
    void isUserUnsubscribedOrNotExist_ShouldReturnTrue_WhenUserIsUnsubscribed() {

        String userId = UUID.randomUUID().toString();
        UUID userUuid = UUID.fromString(userId);
        WaitingList waitingList = new WaitingList();
        waitingList.setStatus(StatusWaitingList.UNSUBSCRIBE);


        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userId);
            when(this.waitingListRepository.findByIdUser(userUuid)).thenReturn(Optional.of(waitingList));

            boolean result = this.waitingListService.isUserUnsubscribedOrNotExist();

            assertTrue(result);
            verify(this.waitingListRepository).findByIdUser(userUuid);
        }
    }

    @Test
    void isUserUnsubscribedOrNotExist_ShouldReturnTrue_WhenUserDoesNotExist() {

        String userId = UUID.randomUUID().toString();
        UUID userUuid = UUID.fromString(userId);

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userId);
            when(this.waitingListRepository.findByIdUser(userUuid)).thenReturn(Optional.empty());

            boolean result = this.waitingListService.isUserUnsubscribedOrNotExist();

            assertTrue(result);
            verify(this.waitingListRepository).findByIdUser(userUuid);
        }
    }

    @Test
    void isUserUnsubscribedOrNotExist_ShouldReturnFalse_WhenUserIsNotUnsubscribed() {

        String userId = UUID.randomUUID().toString();
        UUID userUuid = UUID.fromString(userId);
        WaitingList waitingList = new WaitingList();
        waitingList.setStatus(StatusWaitingList.REGISTRED);

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userId);
            when(this.waitingListRepository.findByIdUser(userUuid)).thenReturn(Optional.of(waitingList));

            boolean result = this.waitingListService.isUserUnsubscribedOrNotExist();

            assertFalse(result);
            verify(this.waitingListRepository).findByIdUser(userUuid);
        }
    }
    @Test
    void getAllWaitingList_ShouldReturnListOfWaitingListResponse_WhenDataExists() {
        // Given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        // Create mock WaitingList entities
        WaitingList waitingList1 = new WaitingList();
        waitingList1.setIdUser(userId1);
        waitingList1.setStatus(StatusWaitingList.REGISTRED);
        waitingList1.setGovernorate(Governorate.ARIANA);
        waitingList1.setCategory(Category.CATEGORY_A);
        waitingList1.setRank(1);

        WaitingList waitingList2 = new WaitingList();
        waitingList2.setIdUser(userId2);
        waitingList2.setStatus(StatusWaitingList.REGISTRED);
        waitingList2.setGovernorate(Governorate.ARIANA);
        waitingList2.setCategory(Category.CATEGORY_B);
        waitingList2.setRank(2);

        // Create mock DTOs
        WaitingListDTO dto1 = new WaitingListDTO();
        dto1.setIdUser(userId1);
        dto1.setStatus(StatusWaitingList.REGISTRED);
        dto1.setGovernorate(Governorate.ARIANA);
        dto1.setCategory(Category.CATEGORY_A);
        dto1.setRank(1);

        WaitingListDTO dto2 = new WaitingListDTO();
        dto2.setIdUser(userId2);
        dto2.setStatus(StatusWaitingList.REGISTRED);
        dto2.setGovernorate(Governorate.ARIANA);
        dto2.setCategory(Category.CATEGORY_B);
        dto2.setRank(2);

        // Create mock projections
        WaitingListWithNames projection1 = mock(WaitingListWithNames.class);
        when(projection1.getWaitingList()).thenReturn(waitingList1);
        when(projection1.getFirstName()).thenReturn("John");
        when(projection1.getLastName()).thenReturn("Doe");

        WaitingListWithNames projection2 = mock(WaitingListWithNames.class);
        when(projection2.getWaitingList()).thenReturn(waitingList2);
        when(projection2.getFirstName()).thenReturn("Jane");
        when(projection2.getLastName()).thenReturn("Smith");

        List<WaitingListWithNames> projections = Arrays.asList(projection1, projection2);

        // Mock repository and mapper behavior
        when(waitingListRepository.findAllWithUserNames()).thenReturn(projections);
        when(waitingListMapper.toDto(waitingList1)).thenReturn(dto1);
        when(waitingListMapper.toDto(waitingList2)).thenReturn(dto2);

        // When
        List<WaitingListResponse> result = waitingListService.getAllWaitingList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first response
        WaitingListResponse response1 = result.get(0);
        assertEquals(dto1, response1.getWaitingList());
        assertEquals("John", response1.getFirstName());
        assertEquals("Doe", response1.getLastName());

        // Verify second response
        WaitingListResponse response2 = result.get(1);
        assertEquals(dto2, response2.getWaitingList());
        assertEquals("Jane", response2.getFirstName());
        assertEquals("Smith", response2.getLastName());

        // Verify interactions
        verify(waitingListRepository).findAllWithUserNames();
        verify(waitingListMapper, times(2)).toDto(any(WaitingList.class));
    }

    @Test
    void getAllWaitingList_ShouldReturnEmptyList_WhenNoDataExists() {
        // Given
        when(waitingListRepository.findAllWithUserNames()).thenReturn(Collections.emptyList());

        // When
        List<WaitingListResponse> result = waitingListService.getAllWaitingList();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify interactions
        verify(waitingListRepository).findAllWithUserNames();
        verify(waitingListMapper, never()).toDto((WaitingList) any());
    }

    @Test
    void getAllWaitingList_ShouldHandleNullNames_WhenUserNamesAreNull() {
        // Given
        UUID userId = UUID.randomUUID();

        WaitingList waitingList = new WaitingList();
        waitingList.setIdUser(userId);
        waitingList.setStatus(StatusWaitingList.REGISTRED);

        WaitingListDTO dto = new WaitingListDTO();
        dto.setIdUser(userId);
        dto.setStatus(StatusWaitingList.REGISTRED);

        WaitingListWithNames projection = mock(WaitingListWithNames.class);
        when(projection.getWaitingList()).thenReturn(waitingList);
        when(projection.getFirstName()).thenReturn(null);
        when(projection.getLastName()).thenReturn(null);

        List<WaitingListWithNames> projections = Collections.singletonList(projection);

        when(waitingListRepository.findAllWithUserNames()).thenReturn(projections);
        when(waitingListMapper.toDto(waitingList)).thenReturn(dto);

        // When
        List<WaitingListResponse> result = waitingListService.getAllWaitingList();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        WaitingListResponse response = result.get(0);
        assertEquals(dto, response.getWaitingList());
        assertNull(response.getFirstName());
        assertNull(response.getLastName());

        verify(waitingListRepository).findAllWithUserNames();
        verify(waitingListMapper).toDto(waitingList);
    }
        @Test
    void testUnsubscribeFromWaitingList_success() {
        UUID userId = UUID.fromString(UUID.randomUUID().toString());

        List<WaitingList> mockList = new ArrayList<>();
        WaitingList userBelow1 = new WaitingList(); userBelow1.setRank(4);
        WaitingList userBelow2 = new WaitingList(); userBelow2.setRank(5);
        mockList.add(userBelow1);
        mockList.add(userBelow2);
        WaitingList waitingList = new WaitingList();
        waitingList.setIdUser(userId);
        waitingList.setRank(3);
        waitingList.setCategory(Category.CATEGORY_A);
        waitingList.setGovernorate(Governorate.TUNIS);
        waitingList.setDelegation("SomeDelegation");
        waitingList.setMunicipality("SomeMunicipality");

        when(waitingListRepository.findByIdUser(userId)).thenReturn(Optional.of(waitingList));
        when(waitingListRepository.findAllWithRankGreaterThanByCategoryAndGovernorateAndDelegation(
            any(), any(), any() , anyInt())).thenReturn(mockList);

        waitingListService.unsubscribeFromWaitingList(userId);

        verify(waitingListRepository).save(waitingList);
        assertEquals(0, waitingList.getRank());
        assertEquals(StatusWaitingList.UNSUBSCRIBE, waitingList.getStatus());

        verify(waitingListRepository).saveAll(anyList());
        assertEquals(3, userBelow1.getRank());
        assertEquals(4, userBelow2.getRank());
    }

    @Test
    void testUnsubscribe_userNotFound_shouldThrowException() {
        UUID userId = UUID.fromString(UUID.randomUUID().toString());

        when(waitingListRepository.findByIdUser(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            waitingListService.unsubscribeFromWaitingList(userId);
        });

        assertEquals("User not found in waiting list", exception.getMessage());
    }

    @Test
    void testUnsubscribe_missingScope_shouldThrowException() {
        UUID userId = UUID.randomUUID();

        WaitingList waitingList = new WaitingList();
        waitingList.setIdUser(userId);
        waitingList.setRank(3);
        waitingList.setCategory(Category.CATEGORY_A);
        waitingList.setGovernorate(Governorate.TUNIS);
        waitingList.setDelegation(null);
        waitingList.setMunicipality(null);

        when(waitingListRepository.findByIdUser(userId)).thenReturn(Optional.of(waitingList));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            waitingListService.unsubscribeFromWaitingList(userId);
        });

        assertEquals("User must have either delegation or municipality", exception.getMessage());
        verify(waitingListRepository).findByIdUser(userId);
    }



    @Test
    void testGetWaitingListByUserId_success() {
        UUID userId = UUID.randomUUID();

        WaitingList waitingList = new WaitingList();
        waitingList.setIdUser(userId);
        waitingList.setRank(3);
        waitingList.setCategory(Category.CATEGORY_A);
        waitingList.setGovernorate(Governorate.TUNIS);
        waitingList.setDelegation("SomeDelegation");
        waitingList.setMunicipality("SomeMunicipality");

        WaitingListDTO dto = new WaitingListDTO();
        dto.setIdUser(userId);
        dto.setRank(3);
        dto.setCategory(Category.CATEGORY_A);
        dto.setGovernorate(Governorate.TUNIS);
        dto.setDelegation("SomeDelegation");
        dto.setMunicipality("SomeMunicipality");

        when(waitingListRepository.findByIdUser(userId)).thenReturn(Optional.of(waitingList));
        when(waitingListMapper.toDto(waitingList)).thenReturn(dto);

        ResponseEntity<WaitingListDTO> response = waitingListService.getWaitingListByUserId(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(dto, response.getBody());
        verify(waitingListRepository).findByIdUser(userId);
        verify(waitingListMapper).toDto(waitingList);
    }


    @Test
    void testGetWaitingListByUserId_userNotFound_shouldThrowException() {
        UUID userId = UUID.fromString(UUID.randomUUID().toString());

        when(waitingListRepository.findByIdUser(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            waitingListService.getWaitingListByUserId(userId);
        });
    }

    @Test
    void testUpdateCategory_SuccessfulCategoryChange() {
        WaitingList updated = new WaitingList();
        updated.setId(UUID.randomUUID());
        WaitingListDTO expectedDto = new WaitingListDTO();

        when(waitingListRepository.findByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(Optional.of(existing));
        when(waitingListRepository.findByCriteriaSortedByCreatedDate(any(), any(), any(), any(), any()))
            .thenReturn(Collections.emptyList());
        when(waitingListRepository.save(any())).thenReturn(updated);
        when(waitingListMapper.toDto((WaitingList) any())).thenReturn(expectedDto);

        WaitingListDTO result = waitingListService.updateCategory(userId, dto);

        assertEquals(expectedDto, result);
        verify(waitingListRepository).save(any());
        verify(waitingListMapper).toDto((WaitingList) any());
    }

    @Test
    void testUpdateCategory_NoChange_ThrowsException() {
        existing.setCategory(Category.CATEGORY_A);
        existing.setGovernorate(Governorate.TUNIS);
        existing.setDelegation("DelA");
        existing.setMunicipality(null);

        dto.setCategory(Category.CATEGORY_A);
        dto.setGovernorate(Governorate.TUNIS);
        dto.setDelegation("DelA");
        dto.setMunicipality(null);
        when(waitingListRepository.findByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            waitingListService.updateCategory(userId, dto)
        );
        assertEquals("Le renouvellement est applicable uniquement en cas de changement de localisation ou de catégorie.", ex.getMessage());
    }


    @Test
    void testUpdateCategory_InvalidCategoryA_MissingDelegation_ThrowsException() {
        dto.setCategory(Category.CATEGORY_A);
        dto.setDelegation(null);
        dto.setMunicipality("SomeCommune");

        when(waitingListRepository.findByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            waitingListService.updateCategory(userId, dto)
        );

        assertEquals("Catégorie A nécessite une délégation et pas de commune.", ex.getMessage());
    }

    @Test
    void testUpdateCategory_InvalidCategoryB_MissingCommune_ThrowsException() {
        dto.setCategory(Category.CATEGORY_B);
        dto.setMunicipality(null);
        dto.setDelegation("SomeDelegation");

        when(waitingListRepository.findByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(Optional.of(existing));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            waitingListService.updateCategory(userId, dto)
        );

        assertEquals("Catégorie B nécessite une commune et pas de délégation.", ex.getMessage());
    }

    @Test
    void testUpdateCategory_UserNotFound_ThrowsException() {
        when(waitingListRepository.findByIdUserAndStatus(userId, StatusWaitingList.REGISTRED))
            .thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            waitingListService.updateCategory(userId, dto)
        );

        assertEquals("L'utilisateur n'est pas inscrit.", ex.getMessage());
    }
}
