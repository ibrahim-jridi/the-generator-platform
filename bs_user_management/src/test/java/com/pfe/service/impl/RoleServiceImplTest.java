package com.pfe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.pfe.domain.Role;
import com.pfe.domain.enumeration.RolesType;
import com.pfe.repository.RoleRepository;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.mapper.RoleMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private RoleMapper roleMapper;

  @InjectMocks
  private RoleServiceImpl roleService;

  private Role interneRole1;
  private Role interneRole2;
  private RoleDTO interneRoleDTO1;
  private RoleDTO interneRoleDTO2;
  private UUID testId1;
  private UUID testId2;

  @BeforeEach
  void setUp() {
    testId1 = UUID.randomUUID();
    testId2 = UUID.randomUUID();

    interneRole1 = new Role();
    interneRole1.setId(testId1);
    interneRole1.setLabel("BS_ROLE_INT_SERVICE_INSPECTION");
    interneRole1.setRoleType(RolesType.INTERNE);
    interneRole1.setIsActive(true);
    interneRole1.setDescription("Internal inspection service role");

    interneRole2 = new Role();
    interneRole2.setId(testId2);
    interneRole2.setLabel("BS_ROLE_INT_MINISTERE_DE_LA_SANTE");
    interneRole2.setRoleType(RolesType.INTERNE);
    interneRole2.setIsActive(true);
    interneRole2.setDescription("Internal ministry of health role");

    interneRoleDTO1 = new RoleDTO();
    interneRoleDTO1.setId(testId1);
    interneRoleDTO1.setLabel("BS_ROLE_INT_SERVICE_INSPECTION");
    interneRoleDTO1.setRoleType(RolesType.INTERNE);
    interneRoleDTO1.setIsActive(true);
    interneRoleDTO1.setDescription("Internal inspection service role");

    interneRoleDTO2 = new RoleDTO();
    interneRoleDTO2.setId(testId2);
    interneRoleDTO2.setLabel("BS_ROLE_INT_MINISTERE_DE_LA_SANTE");
    interneRoleDTO2.setRoleType(RolesType.INTERNE);
    interneRoleDTO2.setIsActive(true);
    interneRoleDTO2.setDescription("Internal ministry of health role");
  }

  @Test
  void testGetActiveInterneRoles_SuccessfulRetrieval() {

    List<Role> interneRoles = Arrays.asList(interneRole1, interneRole2);
    List<RoleDTO> interneRoleDTOs = Arrays.asList(interneRoleDTO1, interneRoleDTO2);

    when(roleRepository.findByRoleTypeAndIsActive(RolesType.INTERNE, true))
        .thenReturn(interneRoles);
    when(roleMapper.toDto(interneRoles))
        .thenReturn(interneRoleDTOs);

    List<RoleDTO> result = roleService.getActiveInterneRoles();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(interneRoleDTOs, result);
    assertEquals(RolesType.INTERNE, result.get(0).getRoleType());
    assertEquals(RolesType.INTERNE, result.get(1).getRoleType());
    assertTrue(result.get(0).getIsActive());
    assertTrue(result.get(1).getIsActive());

    verify(roleRepository).findByRoleTypeAndIsActive(RolesType.INTERNE, true);
    verify(roleMapper).toDto(interneRoles);
    verifyNoMoreInteractions(roleRepository, roleMapper);
  }

  @Test
  void testGetActiveInterneRoles_EmptyList() {
    List<Role> emptyRolesList = Collections.emptyList();
    List<RoleDTO> emptyRoleDTOsList = Collections.emptyList();

    when(roleRepository.findByRoleTypeAndIsActive(RolesType.INTERNE, true))
        .thenReturn(emptyRolesList);
    when(roleMapper.toDto(emptyRolesList))
        .thenReturn(emptyRoleDTOsList);

    List<RoleDTO> result = roleService.getActiveInterneRoles();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertEquals(0, result.size());

    verify(roleRepository).findByRoleTypeAndIsActive(RolesType.INTERNE, true);
    verify(roleMapper).toDto(emptyRolesList);
    verifyNoMoreInteractions(roleRepository, roleMapper);
  }

  @Test
  void testGetActiveInterneRoles_SingleRole() {
    List<Role> singleRoleList = Arrays.asList(interneRole1);
    List<RoleDTO> singleRoleDTOList = Arrays.asList(interneRoleDTO1);

    when(roleRepository.findByRoleTypeAndIsActive(RolesType.INTERNE, true))
        .thenReturn(singleRoleList);
    when(roleMapper.toDto(singleRoleList))
        .thenReturn(singleRoleDTOList);

    List<RoleDTO> result = roleService.getActiveInterneRoles();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(interneRoleDTO1, result.get(0));
    assertEquals(RolesType.INTERNE, result.get(0).getRoleType());
    assertTrue(result.get(0).getIsActive());

    verify(roleRepository).findByRoleTypeAndIsActive(RolesType.INTERNE, true);
    verify(roleMapper).toDto(singleRoleList);
    verifyNoMoreInteractions(roleRepository, roleMapper);
  }

  @Test
  void testGetActiveInterneRoles_RepositoryThrowsException() {
    when(roleRepository.findByRoleTypeAndIsActive(RolesType.INTERNE, true))
        .thenThrow(new RuntimeException("Database connection error"));

    assertThrows(RuntimeException.class, () -> {
      roleService.getActiveInterneRoles();
    });

    verify(roleRepository).findByRoleTypeAndIsActive(RolesType.INTERNE, true);
    verifyNoInteractions(roleMapper);
  }

  @Test
  void testGetActiveInterneRoles_MapperThrowsException() {
    List<Role> interneRoles = Arrays.asList(interneRole1, interneRole2);

    when(roleRepository.findByRoleTypeAndIsActive(RolesType.INTERNE, true))
        .thenReturn(interneRoles);
    when(roleMapper.toDto(interneRoles))
        .thenThrow(new RuntimeException("Mapping error"));

    assertThrows(RuntimeException.class, () -> {
      roleService.getActiveInterneRoles();
    });

    verify(roleRepository).findByRoleTypeAndIsActive(RolesType.INTERNE, true);
    verify(roleMapper).toDto(interneRoles);
  }
}
