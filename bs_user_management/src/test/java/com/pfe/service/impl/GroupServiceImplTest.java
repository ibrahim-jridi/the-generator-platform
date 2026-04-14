package com.pfe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pfe.domain.Group;
import com.pfe.repository.GroupRepository;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.mapper.GroupMapper;
import com.pfe.validator.impl.GroupValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class GroupServiceImplTest {

    @InjectMocks
    private GroupServiceImpl groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private GroupValidator groupValidator;

    private GroupDTO groupDTO;

    private Group existingGroup;

    private GroupDTO existingGroupDto;

    private Group parentGroup;

    private Group updatedGroup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID groupId = UUID.randomUUID();
        UUID parentId = UUID.randomUUID();

        this.groupDTO = new GroupDTO();
        this.groupDTO.setId(groupId);
        this.groupDTO.setLabel("label");
        this.groupDTO.setDescription("Updated Description");

        GroupDTO parentDTO = new GroupDTO();
        parentDTO.setId(parentId);
        this.groupDTO.setParent(parentDTO);

        this.existingGroup = new Group();
        this.existingGroup.setId(groupId);
        this.existingGroup.setLabel("label");
        this.existingGroup.setDescription("Original Description");

        this.parentGroup = new Group();
        this.parentGroup.setId(parentId);
        this.parentGroup.setLabel("parent label");
        this.parentGroup.setDescription("Parent Group");

        this.existingGroup.setParent(this.parentGroup);

        this.updatedGroup = new Group();
        this.updatedGroup.setId(groupId);
        this.updatedGroup.setLabel("label");
        this.updatedGroup.setDescription("Updated Description");

        this.existingGroupDto = new GroupDTO();
        this.existingGroupDto.setLabel(this.existingGroup.getLabel());
        this.existingGroupDto.setId(this.existingGroup.getId());
        this.existingGroupDto.setDescription(this.existingGroup.getDescription());
        this.existingGroupDto.setParent(parentDTO);

    }

    @AfterEach
    void tearDown() {
        this.groupDTO = null;
        this.existingGroup = null;
        this.parentGroup = null;
        this.updatedGroup = null;
    }

    @Test
    void saveSuccessWithNullParent() {
        Mockito.doReturn(this.existingGroup).when(this.groupRepository).save(any(Group.class));
        when(this.groupMapper.toEntity(this.groupDTO)).thenReturn(this.existingGroup);
        when(this.groupMapper.toDto(this.existingGroup)).thenReturn(this.groupDTO);

        GroupDTO groupDTOSaved = this.groupService.save(this.groupDTO);

        assertEquals(groupDTOSaved.getId(), this.groupDTO.getId());
        assertEquals(groupDTOSaved.getLabel(), this.groupDTO.getLabel());
        assertEquals(groupDTOSaved.getDescription(), this.groupDTO.getDescription());
    }

    @Test
    void saveSuccessWithParentThatExist() {
        GroupDTO parentDTO = new GroupDTO();
        parentDTO.setLabel(this.parentGroup.getLabel());
        this.groupDTO.setParent(parentDTO);

        Mockito.doReturn(this.parentGroup).when(this.groupRepository).findFirstByLabel("parent label");
        Mockito.doReturn(this.existingGroup).when(this.groupRepository).save(any(Group.class));
        when(this.groupMapper.toEntity(this.groupDTO)).thenReturn(this.existingGroup);
        when(this.groupMapper.toDto(this.existingGroup)).thenReturn(this.groupDTO);

        GroupDTO groupDTOSaved = this.groupService.save(this.groupDTO);

        assertNotNull(groupDTOSaved);
        verify(this.groupRepository).findFirstByLabel("parent label");
        assertEquals(groupDTOSaved.getId(), this.groupDTO.getId());
        assertEquals(groupDTOSaved.getLabel(), this.groupDTO.getLabel());
        assertEquals(groupDTOSaved.getParent().getLabel(), this.parentGroup.getLabel());
        assertEquals(groupDTOSaved.getDescription(), this.groupDTO.getDescription());
        assertEquals(groupDTOSaved.getIsActive(), this.groupDTO.getIsActive());

    }

    @Test
    void updateGroupThatHasGroupParentShouldUpdateGroupAndParent() {

        doReturn(Optional.of(this.existingGroup)).when(this.groupRepository).findById(this.groupDTO.getId());
        doReturn(Optional.of(this.parentGroup)).when(this.groupRepository).findById(this.parentGroup.getId());
        when(this.groupRepository.save(this.parentGroup)).thenReturn(this.parentGroup);
        when(this.groupRepository.save(this.updatedGroup)).thenReturn(this.updatedGroup);
        when(this.groupMapper.toDto(any(Group.class))).thenReturn(this.groupDTO);

        GroupDTO result = this.groupService.update(this.groupDTO);

        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        verify(this.groupRepository).findById(this.groupDTO.getId());
        verify(this.groupRepository, never()).findById(null);
        verify(this.groupRepository, times(1)).save(any(Group.class));
        verify(this.groupMapper).toDto(this.updatedGroup);
    }

    @Test
    void updateGroupThatHasNoParentGroupShouldUpdateGroupOnly() {
        this.groupDTO.setParent(null);
        this.existingGroup.setParent(null);
        doReturn(Optional.of(this.existingGroup)).when(this.groupRepository).findById(this.groupDTO.getId());
        when(this.groupRepository.save(this.updatedGroup)).thenReturn(this.updatedGroup);
        when(this.groupMapper.toDto(any(Group.class))).thenReturn(this.groupDTO);

        GroupDTO result = this.groupService.update(this.groupDTO);

        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        verify(this.groupRepository).findById(this.groupDTO.getId());
        verify(this.groupRepository, never()).findById(null);
        verify(this.groupRepository, times(1)).save(any(Group.class));
        verify(this.groupMapper).toDto(this.updatedGroup);
    }

    @Test
    void updateGroupNotFoundShouldThrowException() {
        when(this.groupRepository.findById(this.groupDTO.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> this.groupService.update(this.groupDTO));
        assertEquals("Group with id not found", exception.getMessage());
        verify(this.groupRepository).findById(this.groupDTO.getId());
        verify(this.groupRepository, never()).save(any(Group.class));
    }

    @Test
    void findOneExistingGroup() {
        doReturn(Optional.of(this.existingGroup)).when(this.groupRepository).findById(this.groupDTO.getId());
        when(this.groupMapper.toDto(any(Group.class))).thenReturn(this.existingGroupDto);

        GroupDTO result = this.groupService.findOne(this.groupDTO.getId()).orElse(null);

        assertNotNull(result);
        assertEquals("Original Description", result.getDescription());
        assertEquals("label", result.getLabel());
    }

    @Test
    void delete() {
        when(this.groupRepository.findById(this.existingGroup.getId())).thenReturn(Optional.of(this.existingGroup));

        this.groupService.delete(this.existingGroup.getId());

        verify(this.groupRepository).findById(this.existingGroup.getId());
    }

    @Test
    void deleteGroupNotFoundShouldThrowEntityNotFoundException() {
        when(this.groupRepository.findById(this.existingGroup.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> this.groupService.delete(this.existingGroup.getId()));
        assertEquals("Group with id  not found", exception.getMessage());

        verify(this.groupRepository).findById(this.existingGroup.getId());
        verify(this.groupRepository, never()).deleteById(this.existingGroup.getId());
    }

    @Test
    void getGroupsWhenNoGroupsShouldReturnEmptyList() {
        when(this.groupRepository.findAll()).thenReturn(Collections.emptyList());
        when(this.groupMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<GroupDTO> result = this.groupService.getGroups();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(this.groupRepository).findAll();
        verify(this.groupMapper).toDto(Collections.emptyList());
    }

    @Test
    void getGroupsWhenOneGroupExistsShouldReturnListWithOneGroup() {
        when(this.groupRepository.findAll()).thenReturn(List.of(this.existingGroup));
        when(this.groupMapper.toDto(List.of(this.existingGroup))).thenReturn(List.of(this.groupDTO));

        List<GroupDTO> result = this.groupService.getGroups();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(this.groupDTO, result.get(0));
        verify(this.groupRepository).findAll();
        verify(this.groupMapper).toDto(List.of(this.existingGroup));
    }

    @Test
    void getUserIdsByGroupIdWhenNoUserIdsShouldReturnEmptyList() {
        when(this.groupRepository.getUserIdsByGroupId(this.existingGroup.getId())).thenReturn(Collections.emptyList());

        List<UUID> result = this.groupService.getUserIdsByGroupId(this.existingGroup.getId());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(this.groupRepository).getUserIdsByGroupId(this.existingGroup.getId());
    }

    @Test
    void getUserIdsByGroupIdWhenOneUserIdExistsShouldReturnListWithOneUserId() {
        UUID userId = UUID.randomUUID();
        when(this.groupRepository.getUserIdsByGroupId(this.existingGroup.getId())).thenReturn(List.of(userId));

        List<UUID> result = this.groupService.getUserIdsByGroupId(this.existingGroup.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0));
        verify(this.groupRepository).getUserIdsByGroupId(this.existingGroup.getId());
    }

}
