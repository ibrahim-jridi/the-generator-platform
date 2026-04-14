package com.pfe.service.impl;

import com.pfe.config.Constants;
import com.pfe.domain.*;
import com.pfe.domain.DesignationsList;
import com.pfe.feignService.ApplicationConnector;
import com.pfe.repository.DesignationsListRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.service.RoleService;
import com.pfe.service.UserService;
import com.pfe.service.dto.DesignationsListDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.dto.request.ApplicantListUserResponse;
import com.pfe.service.mapper.DesignationsListMapper;
import com.pfe.service.mapper.RoleMapper;
import com.pfe.service.mapper.UserMapper;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesignationsListImplTest {

    @Mock
    private DesignationsListMapper designationsListMapper;

    @Mock
    private DesignationsListRepository designationsListRepository;

    @InjectMocks
    private DesignationsListImpl designationsListService;
    @InjectMocks
    private DesignationsListDTO designationsListDTO;
    @Mock
    private DesignationsList designationsList;
    private UUID testId;
    private String userId;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private  Keycloak keycloak;
    @Value("${bs-app.keycloak.realm}")
    private String realm;
    @Mock
    private UsersResource usersResource;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UserResource userResource;
    @Mock
    private ApplicationConnector applicationConnector;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Logger mockLogger;
    private Pageable pageable;
    private RoleDTO bsEctdRoleDTO;
    private Role bsEctdRole;
    private RoleDTO bsLocRoleDTO;
    private Role bsLocRole;
    private UserDTO userDTO;
    private User user;
    private DesignationsList designation;
    private String testUsername;

    private UUID pmUserId;
    DesignationsListImplTest() {

    }

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        userId = UUID.randomUUID().toString();
        designationsListDTO = new DesignationsListDTO();
        designationsList = new DesignationsList();
        pageable = PageRequest.of(0, 10);


        designation = new DesignationsList();
        designation.setDesignatedUser(user);
        designation.setId(testId);
        bsEctdRoleDTO = new RoleDTO();
        bsEctdRoleDTO.setLabel(Constants.BS_ROLE_ECTD);
        bsEctdRole = new Role();
        bsEctdRole.setLabel(Constants.BS_ROLE_ECTD);

        bsLocRoleDTO = new RoleDTO();
        bsLocRoleDTO.setLabel(Constants.BS_ROLE_PM_REPRI_LABO);
        bsLocRole = new Role();
        bsLocRole.setLabel(Constants.BS_ROLE_PM_REPRI_LABO);

        userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        user = new User();
        user.setId(userDTO.getId());
        user.setRoles(new HashSet<>());
        user.setUsername(testUsername);


    }

    @Test
    void testGetListByPmUserId_SuccessfulRetrieval() {
        DesignationsList designation1 = new DesignationsList(/* constructor params */);
        DesignationsList designation2 = new DesignationsList(/* constructor params */);
        List<DesignationsList> designationsList = Arrays.asList(designation1, designation2);
        Page<DesignationsList> designationsPage = new PageImpl<>(designationsList, pageable, 2);

        when(designationsListRepository.findByPmUserId(testId, pageable))
            .thenReturn(designationsPage);

        List<DesignationsList> result = designationsListService.getListByPmUserId(testId, pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(designationsList, result);

        verify(designationsListRepository).findByPmUserId(testId, pageable);
        verifyNoMoreInteractions(designationsListRepository);
    }

    @Test
    void testGetListByPmUserId_EmptyList() {
        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userId);

            Page<DesignationsList> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

            when(designationsListRepository.findByPmUserId(testId, pageable)).thenReturn(emptyPage);

            List<DesignationsList> result = designationsListService.getListByPmUserId(testId, pageable);

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(designationsListRepository, times(1)).findByPmUserId(testId, pageable);
        }
    }

    @Test
    void testGetDesignatedUserIdsByPmId_shouldReturnValidUserList() {
        UUID pmKeycloakId = UUID.randomUUID();
        UUID pmUserId = UUID.randomUUID();

        User pmUser = new User();
        pmUser.setId(pmUserId);
        pmUser.setKeycloakId(pmKeycloakId);
        when(userRepository.findByKeycloakId(pmKeycloakId)).thenReturn(pmUser);

        User designatedUser = new User();
        designatedUser.setUsername("ext-1111157");

        User laboratoryUser = new User();
        laboratoryUser.setUsername("pm-ext-1111296");
        laboratoryUser.setDenomination("3M SANTE");

        Role validRole = new Role();
        validRole.setLabel("BS_ROLE_PM_REPRI_LABO");

        DesignationsList designation = new DesignationsList();
        designation.setDesignatedUser(designatedUser);
        designation.setLaboratoryUser(laboratoryUser);
        designation.setRole(validRole);

        when(designationsListRepository.findByPmUserId(any(UUID.class)))
            .thenReturn(List.of(designation));


        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setLabel("BS_ROLE_PM_REPRI_LABO");

        when(roleService.getRoleByLabel("BS_ROLE_PM_REPRI_LABO")).thenReturn(roleDTO);

        Map<String,String> result = designationsListService.getDesignatedUserIdsByPmId(pmKeycloakId);

        Assertions.assertEquals(1, result.size());
        assertTrue(result.containsKey("ext-1111157"));

        String labos = result.get("ext-1111157");
         assertTrue(labos.contains("pm-ext-1111296:3M SANTE"));
    }

    @Test
    void testGetDesignatedUserIdsByPmId_shouldReturnEmptyList_whenPmUserNotFound() {
        UUID pmKeycloakId = UUID.randomUUID();

        when(userRepository.findByKeycloakId(pmKeycloakId)).thenReturn(null);

        Map<String,String> result = designationsListService.getDesignatedUserIdsByPmId(pmKeycloakId);

        assertTrue(result.isEmpty());
    }
    @Test
    void testNullOrEmptyDesignationsList() {
        assertDoesNotThrow(() -> designationsListService.processDesignatedUsersRoles(null));
        assertDoesNotThrow(() -> designationsListService.processDesignatedUsersRoles(new ArrayList<>()));

        verifyNoInteractions(roleService, userService, roleMapper, userMapper);
    }
@Test
void testUserWithoutIndustryOrPromotionRoleIsSkipped() {
    Set<Role> roles = new HashSet<>();
    // Don't add any industry or promotion roles
    Role someOtherRole = new Role();
    someOtherRole.setLabel("SOME_OTHER_ROLE");
    roles.add(someOtherRole);

    user.setRoles(roles);
    designation = new DesignationsList();
    designation.setDesignatedUser(user);

    when(userService.findOne(user.getId())).thenReturn(Optional.of(userDTO));
    when(userMapper.toEntity(userDTO)).thenReturn(user);

    designationsListService.processDesignatedUsersRoles(Collections.singletonList(designation));

    // Verify that saveUser and reassignUserToNewRoles are NOT called
    verify(userService, never()).saveUser(any(UserDTO.class));
    verify(userService, never()).reassignUserToNewRoles(any());
}
    @Test
    void testGetDesignationListOfIndustryUsers_WithDesignations() {
        UUID keycloakId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setKeycloakId(keycloakId);

        DesignationsList d1 = new DesignationsList();

        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(mockUser);
        when(designationsListRepository.findByPmUserId(userId)).thenReturn(List.of(d1));

        List<DesignationsList> result = designationsListService.getDesignationListOfIndustryUsers(keycloakId);

        assertEquals(1, result.size());
    }
    @Test
    void testSaveDesignationsIndustrialUsersAttributes_UserNotFound() {
        String username = "testuser";
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
            designationsListService.saveDesignationsIndustrialUsersAttributes(username));
    }

    @Test
    void testPmHasEctdByDesignatedUser_pmHasEctdRole() {
        UUID designatedUserId = UUID.randomUUID();
        Role ectdRole = new Role();
        ectdRole.setLabel(Constants.BS_ROLE_ECTD);

        User pmUser = new User();
        pmUser.setRoles(new HashSet<>(Collections.singletonList(ectdRole)));

        DesignationsList designation = new DesignationsList();
        designation.setPmUser(pmUser);

        when(designationsListRepository.findByDesignatedUserId(designatedUserId))
            .thenReturn(Collections.singletonList(designation));

        boolean result = designationsListService.pmHasEctdByDesignatedUser(designatedUserId);

        assertTrue(result, "PM has eCTD role, should return true");
    }

    @Test
    void testPmHasEctdByDesignatedUser_pmHasNoRoles() {
        UUID designatedUserId = UUID.randomUUID();

        User pmUser = new User();
        pmUser.setRoles(Collections.emptySet());

        DesignationsList designation = new DesignationsList();
        designation.setPmUser(pmUser);

        when(designationsListRepository.findByDesignatedUserId(designatedUserId))
            .thenReturn(Collections.singletonList(designation));

        boolean result = designationsListService.pmHasEctdByDesignatedUser(designatedUserId);

        assertFalse(result, "PM has no eCTD role, should return false");
    }

    @Test
    void testPmHasEctdByDesignatedUser_noDesignationFound() {
        UUID designatedUserId = UUID.randomUUID();

        when(designationsListRepository.findByDesignatedUserId(designatedUserId))
            .thenReturn(Collections.emptyList());

        boolean result = designationsListService.pmHasEctdByDesignatedUser(designatedUserId);

        assertFalse(result, "No designation found, should return false");
    }

    @Test
    void testPmHasEctdByDesignatedUser_nullPmUser() {
        UUID designatedUserId = UUID.randomUUID();

        DesignationsList designation = new DesignationsList();
        designation.setPmUser(null); // PM is null

        when(designationsListRepository.findByDesignatedUserId(designatedUserId))
            .thenReturn(Collections.singletonList(designation));

        boolean result = designationsListService.pmHasEctdByDesignatedUser(designatedUserId);

        assertFalse(result, "PM is null, should return false");
    }
    @Test
    private void mockBearerAuthentication() {
        BearerTokenAuthentication authentication = Mockito.mock(BearerTokenAuthentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    void shouldReturnApplicantsForPhysicalUsers() {
        mockBearerAuthentication();

        UserRepresentation physicalUser = new UserRepresentation();
        physicalUser.setUsername("ext-john");

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("applicant", List.of("app1", "app2"));
        physicalUser.setAttributes(attributes);

        UserRepresentation nonPhysicalUser = new UserRepresentation();
        nonPhysicalUser.setUsername("admin");

        when(userService.getAllUsersFromKeycloak())
            .thenReturn(List.of(physicalUser, nonPhysicalUser));

        ResponseEntity<List<ApplicantListUserResponse>> response =
            designationsListService.getApplicantsAttributes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);

        ApplicantListUserResponse result = response.getBody().get(0);
        assertThat(result.getUsername()).isEqualTo("ext-john");
        assertThat(result.getApplicants()).contains("app1");
    }

    @Test
    void shouldReturnUnauthorizedWhenNoBearerToken() {
        SecurityContextHolder.clearContext();

        ResponseEntity<List<ApplicantListUserResponse>> response =
            designationsListService.getApplicantsAttributes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEmpty();
    }
    @Test
    void shouldSkipUsersWithoutApplicantAttribute() {
        mockBearerAuthentication();

        UserRepresentation physicalUser = new UserRepresentation();
        physicalUser.setUsername("ext-1111132");
        physicalUser.setAttributes(Collections.emptyMap());

        when(userService.getAllUsersFromKeycloak())
            .thenReturn(List.of(physicalUser));

        ResponseEntity<List<ApplicantListUserResponse>> response =
            designationsListService.getApplicantsAttributes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

}



