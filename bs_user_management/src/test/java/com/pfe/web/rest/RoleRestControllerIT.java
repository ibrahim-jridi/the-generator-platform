package com.pfe.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pfe.IntegrationTest;
import com.pfe.domain.Authority;
import com.pfe.domain.Role;
import com.pfe.repository.AuthorityRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.service.RoleService;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.service.dto.RoleDTO;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleRestControllerIT {

  private static final String DEFAULT_LABEL = "default_label";
  private static final String DEFAULT_DESCRIPTION = "default_description";
  private static final String UPDATED_DESCRIPTION = "updated_description";

  private static final String ENTITY_API_URL = "/api/v1/roles";

  @Autowired
  private MockMvc restInternalRoleMockMvc;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleService roleService;

  @Autowired
  private MockMvc restRoleMockMvc;


  @Autowired
  private AuthorityRepository authorityRepository;

  private RoleDTO roleDTO;
  private Role role;
  private Authority authority;
  private AuthorityDTO authorityDTO;

  public static RoleDTO createEntity() {
    RoleDTO role = new RoleDTO();
    role.setLabel(DEFAULT_LABEL);
    role.setDescription(DEFAULT_DESCRIPTION);
    role.setIsActive(false);
    return role;
  }


  @BeforeEach
  public void initTest() {
    this.roleDTO = createEntity();
  }

  private Authority initAuthority() {
    this.authority = new Authority();
    this.authority.setLabel("AUTH");
    this.authority.setDescription("AUTH_DESC");
    this.authority.setDeleted(false);
    this.authority.setFirstInsert(true);
    return this.authorityRepository.save(this.authority);
  }

  private AuthorityDTO initAuthorityDto() {
    this.authorityDTO = new AuthorityDTO();
    this.authorityDTO.setId(this.authority.getId());
    this.authorityDTO.setDeleted(this.authority.getDeleted());
    this.authorityDTO.setLabel(this.authority.getLabel());
    this.authorityDTO.setDescription(this.authority.getDescription());
    return this.authorityDTO;
  }

  @Test
  @Order(1)
  @WithMockUser(username = "bs-admin")
  void saveRoleWithForbiddenAuthorities() throws Exception {
    this.restRoleMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.roleDTO)))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(2)
  @WithUnauthenticatedMockUser
  void saveParentRoleWithUnauthorized() throws Exception {
    this.restRoleMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.roleDTO)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @Order(3)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void saveRoleErrorIdExists() throws Exception {
    this.roleDTO.setId(UUID.randomUUID());
    this.restRoleMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.roleDTO)))
        .andExpect(status().isInternalServerError());
    this.roleDTO.setId(null);
  }

  @Test
  @Order(4)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void saveRoleSuccess() throws Exception {
    this.authority = this.initAuthority();
    this.authorityDTO = new AuthorityDTO();
    this.authorityDTO.setId(this.authority.getId());
    this.authorityDTO.setDeleted(this.authority.getDeleted());
    this.authorityDTO.setLabel(this.authority.getLabel());
    this.authorityDTO.setDescription(this.authority.getDescription());
    this.roleDTO.setAuthorities(new HashSet<>(Collections.singletonList(this.authorityDTO)));
    this.restRoleMockMvc
        .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(this.roleDTO)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.label").value(this.roleDTO.getLabel()))
        .andExpect(jsonPath("$.description").value(this.roleDTO.getDescription()));
  }

  @Test
  @Order(5)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void updateRoleSFailed() throws Exception {
    this.role = this.roleRepository.findByLabel(DEFAULT_LABEL).orElse(null);
    this.roleDTO.setId(this.role.getId());
    this.roleDTO.setDescription(UPDATED_DESCRIPTION);
    this.roleDTO.setLabel("CANNOT_UPDATE_LABEL");
    this.restRoleMockMvc
        .perform(
            put(ENTITY_API_URL + "/" + this.role.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(this.roleDTO)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  @Order(6)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void findAllRoles() throws Exception {
    this.restRoleMockMvc
        .perform(get(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.length()").isNotEmpty())
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
  }

  @Test
  @Order(7)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void updateRoleSuccess() throws Exception {
    this.role = this.roleRepository.findByLabel(DEFAULT_LABEL).orElse(null);
    this.authority = this.authorityRepository.findAll().stream().findFirst().get();
    this.authorityDTO = initAuthorityDto();
    this.roleDTO.setId(this.role.getId());
    this.roleDTO.setDescription(UPDATED_DESCRIPTION);
    this.roleDTO.setAuthorities(new HashSet<>(Collections.singletonList(this.authorityDTO)));
    this.restRoleMockMvc
        .perform(
            put(ENTITY_API_URL + "/" + this.role.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(this.roleDTO)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
        .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION));
  }

  @Test
  @Order(8)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void getRoleById() throws Exception {
    this.role = this.roleRepository.findByLabel(DEFAULT_LABEL).orElse(null);

    this.restRoleMockMvc
        .perform(get(ENTITY_API_URL + "/" + this.role.getId()).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.label").value(this.role.getLabel()))
        .andExpect(jsonPath("$.description").value(this.role.getDescription()));
  }

  @Test
  @Order(9)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void getRoleByIdNotFound() throws Exception {
    this.restRoleMockMvc
        .perform(
            get(ENTITY_API_URL + "/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }


  @Test
  @Order(10)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void activateRole() throws Exception {
    this.role = this.roleRepository.findByLabel(DEFAULT_LABEL).orElse(null);
    this.restRoleMockMvc
        .perform(get(
            ENTITY_API_URL + "/activate-or-deactivate-role-by-id/" + this.role.getId()).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    Assertions.assertFalse(this.role.getIsActive());
  }

  @Test
  @Order(11)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void deactivateRole() throws Exception {
    this.role = this.roleRepository.findByLabel(DEFAULT_LABEL).orElse(null);
    this.restRoleMockMvc
        .perform(get(
            ENTITY_API_URL + "/activate-or-deactivate-role-by-id/" + this.role.getId()).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    Assertions.assertTrue(this.role.getIsActive());
  }

  @Test
  @Order(12)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void deleteRolesSuccess() throws Exception {

    this.role = this.roleRepository.findByLabel(DEFAULT_LABEL).orElse(null);
    this.restRoleMockMvc
        .perform(delete(ENTITY_API_URL + "/" + this.role.getId()).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().is(204));
  }

  @Test
  @Order(13)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void deleteRoleTestFailedForRoleWithIdNotFound() throws Exception {
    UUID id = UUID.randomUUID();
    this.restRoleMockMvc
        .perform(delete(ENTITY_API_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.detail").value("Role with id not found"));
  }


}
