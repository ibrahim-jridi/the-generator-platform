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
import com.pfe.domain.Group;
import com.pfe.repository.GroupRepository;
import com.pfe.service.dto.GroupDTO;
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
public class GroupResourceIT {
    private static final String DEFAULT_LABEL = "default_label";
    private static final String UPDATED_LABEL = "updated_label";
    private static final String PARENT_LABEL = "parent_label";
    private static final String DEFAULT_DESCRIPTION = "default_description";
    private static final String UPDATED_DESCRIPTION = "updated_description";
    private static final String PARENT_DESCRIPTION = "parent_description";
    private static final UUID PARENT_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/v1/groups";

    @Autowired
    private MockMvc restGroupMockMvc;

    @Autowired
    private GroupRepository groupRepository;

    private GroupDTO groupDTO;
    private Group group;
    private GroupDTO parentGroupDto;
    private Group parentGroup;

    public static GroupDTO createEntity() {
        GroupDTO group = new GroupDTO();
        group.setLabel(DEFAULT_LABEL);
        group.setDescription(DEFAULT_DESCRIPTION);
      group.setIsActive(false);
        return group;
    }

    public static GroupDTO createParentEntity() {
        GroupDTO group = new GroupDTO();
        group.setLabel(PARENT_LABEL);
        group.setDescription(PARENT_DESCRIPTION);
        group.setParent(new GroupDTO());
        return group;
    }

    @BeforeEach
    public void initTest() {
        this.groupDTO = createEntity();
        this.parentGroupDto = createParentEntity();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "bs-admin")
    void saveParentGroupWithForbiddenAuthorities() throws Exception {
        this.restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(this.parentGroupDto)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    @WithUnauthenticatedMockUser
    void saveParentGroupWithUnauthorized() throws Exception {
        this.restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(this.parentGroupDto)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void saveParentGroupErrorIdExists() throws Exception {
        this.parentGroupDto.setId(PARENT_ID);
        this.restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(this.parentGroupDto)))
            .andExpect(status().isBadRequest());
        this.parentGroupDto.setId(null);
    }

    @Test
    @Order(4)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void saveParentGroupSuccess() throws Exception {
        this.restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(this.parentGroupDto)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.label").value(this.parentGroupDto.getLabel()))
            .andExpect(jsonPath("$.description").value(this.parentGroupDto.getDescription()));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void saveGroupSuccessWithParentGroup() throws Exception {
        this.parentGroup = this.groupRepository.findFirstByLabel(PARENT_LABEL);
        this.parentGroupDto.setId(this.parentGroup.getId());
        this.groupDTO.setParent(this.parentGroupDto);
        this.restGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(this.groupDTO)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Order(6)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void findAllGroups() throws Exception {
        this.restGroupMockMvc
            .perform(get(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.length()").isNotEmpty())
            .andExpect(jsonPath("$.[*].label").value(hasItem(PARENT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(PARENT_DESCRIPTION)));
    }

    @Test
    @Order(7)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void updateGroupSuccess() throws Exception {
        this.group = this.groupRepository.findFirstByLabel(DEFAULT_LABEL);
        this.parentGroup = this.groupRepository.findFirstByLabel(PARENT_LABEL);
        this.parentGroupDto.setId(this.parentGroup.getId());
        this.groupDTO.setParent(this.parentGroupDto);
        this.groupDTO.setId(this.group.getId());
        this.groupDTO.setDescription(UPDATED_DESCRIPTION);
        this.restGroupMockMvc
            .perform(put(ENTITY_API_URL + "/" + this.group.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(this.groupDTO)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getGroupById() throws Exception {
        this.parentGroup = this.groupRepository.findFirstByLabel(PARENT_LABEL);
        this.restGroupMockMvc
            .perform(get(ENTITY_API_URL + "/" + this.parentGroup.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.label").value(this.parentGroupDto.getLabel()))
            .andExpect(jsonPath("$.description").value(this.parentGroupDto.getDescription()));
    }

    @Test
    @Order(9)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getGroupByIdNotFound() throws Exception {
        this.restGroupMockMvc
            .perform(get(ENTITY_API_URL + "/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void deleteGroupsSuccess() throws Exception {
        this.parentGroup = this.groupRepository.findFirstByLabel(PARENT_LABEL);
        this.restGroupMockMvc
            .perform(delete(ENTITY_API_URL + "/" + this.parentGroup.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(204));
    }

    @Test
    @Order(11)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void deleteGroupTestFailedForGroupWithIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        this.restGroupMockMvc
            .perform(delete(ENTITY_API_URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.detail").value("Group with id  not found"));
    }

    @Test
    @Order(12)
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void deleteGroupTestSuccess() throws Exception {
        this.group = this.groupRepository.findFirstByLabel(DEFAULT_LABEL);
        this.restGroupMockMvc
            .perform(delete(ENTITY_API_URL + "/" + this.group.getId()).contentType(
                MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

  @Test
  @Order(13)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void activateGroup() throws Exception {
    this.group = this.groupRepository.findFirstByLabel(DEFAULT_LABEL);
    this.restGroupMockMvc
        .perform(put(
            ENTITY_API_URL + "/activate-or-deactivate-group/" + this.group.getId()).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    Assertions.assertTrue(this.group.getIsActive());
  }

  @Test
  @Order(14)
  @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
  void deactivateGroup() throws Exception {
    this.group = this.groupRepository.findFirstByLabel(DEFAULT_LABEL);
    this.restGroupMockMvc
        .perform(put(
            ENTITY_API_URL + "/activate-or-deactivate-group/" + this.group.getId()).contentType(
            MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    Assertions.assertFalse(this.group.getIsActive());
  }


}
