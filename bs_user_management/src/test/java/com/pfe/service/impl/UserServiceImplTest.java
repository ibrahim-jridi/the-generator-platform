package com.pfe.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.pfe.config.Constants;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.domain.enumeration.UserType;
import com.pfe.repository.UserRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.service.dto.ProfileDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.dto.response.CheckEligibilityResponse;
import com.pfe.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private UUID userId;
    private String userIdStr;
    private String profileDataInput;
    private ProfileDTO profileData;
    private ProfileDTO profileDTO;
    private Role role;
    private final String username = "testuser";
    private final String roleLabel = Constants.BS_ROLE_PM_SOCIETE_CONSULTING;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.userId = UUID.randomUUID();
        this.userIdStr = userId.toString();
        this.profileDataInput = "[{name=John,age=30}]";

        this.user = new User();
        this.user.setId(this.userId);
        this.user.setProfile(new HashMap<>());
        this.user.setProfileCompleted(false);
        this.userDTO = new UserDTO();
        this.userDTO.setId(this.userId);
        this.userDTO.setProfile(new HashMap<>());
        this.userDTO.setProfileCompleted(true);

        when(this.userRepository.findById(this.userId)).thenReturn(Optional.of(this.user));
        this.profileData = new ProfileDTO();
        Map<String, String> profileMap = new HashMap<>();
        profileMap.put("name", "John");
        profileMap.put("age", "30");
        this.profileData.setProfileData(profileMap);
        profileDTO = new ProfileDTO();
        user.setUsername(username);
        role = new Role();
        role.setLabel(roleLabel);
    }
    @Test
    void testSaveAndCompleteMyProfile_Success() {
        UserServiceImpl userServiceSpy = spy(userService);

        doNothing().when(userServiceSpy).updateUserAttribute(anyString(), anyString(), anyString());

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

            userDTO.getProfile().put("key1", "value1");
            userDTO.getProfile().put("key2", "value2");

            ProfileDTO testProfileData = new ProfileDTO();
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("key1", "value1");
            dataMap.put("key2", "value2");
            testProfileData.setProfileData(dataMap);

            ResponseEntity<UserDTO> response = userServiceSpy.saveAndCompleteMyProfile(userId, testProfileData);

            assertNotNull(response);
            assertEquals(userDTO, response.getBody());
            assertTrue(userDTO.getProfileCompleted());
            assertEquals(2, userDTO.getProfile().size());
            assertEquals("value1", userDTO.getProfile().get("key1"));
            assertEquals("value2", userDTO.getProfile().get("key2"));

            verify(userRepository, times(1)).save(user);
            verify(userServiceSpy, times(1)).updateUserAttribute(userId.toString(), "profile_completed", "true");
        }
    }

    @Test
    void testSaveAndCompleteMyProfile_InvalidData() {
        UserServiceImpl userServiceSpy = spy(userService);

        doNothing().when(userServiceSpy).updateUserAttribute(anyString(), anyString(), anyString());

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

            ProfileDTO invalidProfileData = new ProfileDTO();
            Map<String, String> invalidDataMap = new HashMap<>();
            invalidDataMap.put("customdatagrid", "value1");
            invalidDataMap.put("submit", "true");
            invalidDataMap.put("key1", "new ArrayList<>()");
            invalidProfileData.setProfileData(invalidDataMap);

            ResponseEntity<UserDTO> response = userServiceSpy.saveAndCompleteMyProfile(userId, invalidProfileData);

            assertNotNull(response);
            assertTrue(response.getBody().getProfileCompleted());
            assertTrue(response.getBody().getProfile().isEmpty());

            verify(userRepository, times(1)).save(user);
        }
    }

    @Test
    void testSaveAndCompleteMyProfile_userNotFound_throwsException() {
        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userIdStr);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.saveAndCompleteMyProfile(userId, profileData)
            );

            assertEquals("User not found with ID: " + userId, exception.getMessage());
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).save(any(User.class));
            verify(userMapper, never()).toDto(any(User.class));
        }
    }
    @Test
    void testDoesProfileKeyExist_ReturnsTrue() {

        UUID userId = UUID.randomUUID();
        String key = "someKey";
        when(userRepository.existsProfileKey(userId, key)).thenReturn(true);


        boolean result = userService.doesProfileKeyExist(userId, key);

        assertTrue(result);
        verify(userRepository).existsProfileKey(userId, key);
    }

    @Test
    void testDoesProfileKeyExist_ReturnsFalse() {

        UUID userId = UUID.randomUUID();
        String key = "nonExistingKey";
        when(userRepository.existsProfileKey(userId, key)).thenReturn(false);


        boolean result = userService.doesProfileKeyExist(userId, key);

        assertFalse(result);
        verify(userRepository).existsProfileKey(userId, key);
    }
    @Test
    void testGetProfileValue_WhenValueExists() {

        UUID userId = UUID.randomUUID();
        String key = "preferredLanguage";
        String expectedValue = "fr";
        when(userRepository.findProfileValue(userId, key)).thenReturn(Optional.of(expectedValue));

        Optional<String> result = userService.getProfileValue(userId, key);

        assertTrue(result.isPresent());
        assertEquals(expectedValue, result.get());
        verify(userRepository).findProfileValue(userId, key);
    }

    @Test
    void testGetProfileValue_WhenValueDoesNotExist() {

        UUID userId = UUID.randomUUID();
        String key = "unknownKey";
        when(userRepository.findProfileValue(userId, key)).thenReturn(Optional.empty());

        Optional<String> result = userService.getProfileValue(userId, key);

        assertFalse(result.isPresent());
        verify(userRepository).findProfileValue(userId, key);
    }
    @Test
    void testExistsUserByRole_WhenUserHasRole() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        when(userRepository.existsUserByRole(userId, roleId)).thenReturn(true);

        boolean result = userService.existsUserByRole(userId, roleId);

        assertTrue(result);
        verify(userRepository).existsUserByRole(userId, roleId);
    }

    @Test
    void testExistsUserByRole_WhenUserDoesNotHaveRole() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        when(userRepository.existsUserByRole(userId, roleId)).thenReturn(false);

        boolean result = userService.existsUserByRole(userId, roleId);

        assertFalse(result);
        verify(userRepository).existsUserByRole(userId, roleId);
    }

        @Test
        void testPhysicalUserProfileIncomplete() {
            user.setUserType(UserType.PHYSICAL);
            user.setIsActive(true);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(userRepository.existsByUsernameAndIsProfileCompleted(username, true)).thenReturn(false);
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, roleLabel);
            assertFalse(response.getStatus());
            assertEquals("PROFILE_INCOMPLETE", response.getHeaders().getFirst("X-Warning"));
        }

        @Test
        void testPhysicalUserValid() {
            user.setUserType(UserType.PHYSICAL);
            user.setIsActive(true);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(userRepository.existsByUsernameAndIsProfileCompleted(username, true)).thenReturn(true);
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, roleLabel);
            assertTrue(response.getStatus());
            assertNull(response.getHeaders().getFirst("X-Warning"));
        }


        @Test
        void testCompanyUserInvalidRoleLabel() {
            user.setUserType(UserType.COMPANY);
            user.setIsActive(true);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(userRepository.existsByUsernameAndIsProfileCompleted(username, true)).thenReturn(true);
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, "");
            assertFalse(response.getStatus());
            assertEquals("INVALID_ROLE_LABEL", response.getHeaders().getFirst("X-Warning"));
        }

        @Test
        void testCompanyUserNoRolesAssigned() {
            user.setUserType(UserType.COMPANY);
            user.setIsActive(true);
            user.setRoles(Collections.emptySet());
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(userRepository.existsByUsernameAndIsProfileCompleted(username, true)).thenReturn(true);
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, roleLabel);
            assertFalse(response.getStatus());
            assertEquals("NO_ROLES_ASSIGNED", response.getHeaders().getFirst("X-Warning"));
        }

        @Test
        void testCompanyUserMissingRole() {
            user.setUserType(UserType.COMPANY);
            user.setIsActive(true);
            user.setRoles(Set.of(new Role()));
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(userRepository.existsByUsernameAndIsProfileCompleted(username, true)).thenReturn(true);
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, roleLabel);
            assertFalse(response.getStatus());
            assertEquals("MISSING_ROLE", response.getHeaders().getFirst("X-Warning"));
        }

        @Test
        void testCompanyUserValid() {
            user.setUserType(UserType.COMPANY);
            user.setIsActive(true);
            user.setRoles(Set.of(role));
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(userRepository.existsByUsernameAndIsProfileCompleted(username, true)).thenReturn(true);
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, roleLabel);
            assertTrue(response.getStatus());
            assertNull(response.getHeaders().getFirst("X-Warning"));
        }

        @Test
        void testUserInactiveAccount() {
            user.setUserType(UserType.PHYSICAL);
            user.setIsActive(false);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
            CheckEligibilityResponse response = userService.checkUsernameEligibility(username, roleLabel);
            assertFalse(response.getStatus());
            assertEquals("INACTIVE_ACCOUNT", response.getHeaders().getFirst("X-Warning"));
        }

}

