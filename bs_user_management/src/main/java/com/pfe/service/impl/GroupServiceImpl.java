package com.pfe.service.impl;

import com.pfe.domain.Group;
import com.pfe.domain.User;
import com.pfe.repository.GroupRepository;
import com.pfe.service.GroupService;
import com.pfe.service.UserService;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.mapper.GroupMapper;
import com.pfe.validator.impl.GroupValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Group}.
 */
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

  private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;
  private final GroupValidator groupValidator;
  private final UserService userService;

  public GroupServiceImpl(
      GroupRepository groupRepository,
      GroupMapper groupMapper, GroupValidator groupValidator, UserService userService) {
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
    this.groupValidator = groupValidator;
    this.userService = userService;
  }

  @Override
  public GroupDTO save(GroupDTO groupDTO) {
    log.debug("Request to save Group : {}", groupDTO);
    this.groupValidator.beforeSave(groupDTO);
    Group parentGroup;
    if (groupDTO.getParent() != null) {
      parentGroup = this.groupRepository.findFirstByLabel(groupDTO.getParent().getLabel());
    } else {
      parentGroup = null;
    }
    Group groupToSave = new Group();
    groupToSave.setParent(parentGroup);
    groupToSave.setLabel(groupDTO.getLabel());
    groupToSave.setDescription(groupDTO.getDescription());
    groupToSave.setIsActive(true);
    Group groupSaved = this.groupRepository.save(groupToSave);
    return this.groupMapper.toDto(groupSaved);

  }

  @Override
  public GroupDTO update(GroupDTO groupDTO) {
    log.debug("Request to update Group : {}", groupDTO);
    this.groupValidator.beforeUpdate(groupDTO);
    Group groupToUpdate = this.groupRepository.findById(groupDTO.getId())
        .orElseThrow(() -> new RuntimeException("Group with id not found"));
    groupToUpdate.setDescription(groupDTO.getDescription());
    return this.groupMapper.toDto(this.groupRepository.save(groupToUpdate));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<GroupDTO> findOne(UUID id) {
    log.debug("Request to get Group : {}", id);
    return this.groupRepository.findById(id).map(this.groupMapper::toDto)
        .map(this::countGroupUsers);
  }

  @Override
  public void delete(UUID id) {
    log.debug("Request to delete Group : {}", id);
    Group group = this.groupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Group with id  not found"));
    this.recursiveDeleteGroup(id);
    this.deleteGroup(group);
  }

  @Override
  public List<GroupDTO> getGroups() {
    return this.groupMapper.toDto(this.groupRepository.findAll());
  }

  @Override
  public List<UUID> getUserIdsByGroupId(UUID groupId) {
    return this.groupRepository.getUserIdsByGroupId(groupId);
  }

  @Override
  public String activateOrDeactivateGroupById(UUID id) {
    Group group = this.groupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found"));

    boolean isActive = group.getIsActive();
    String action = isActive ? "deactivated" : "activated";
    log.debug("{} group with id {}", action, id);

    group.setIsActive(!isActive);
    this.groupRepository.save(group);

    return "Group " + action + " successfully";
  }

  @Override
  public void permanentlyDeleteGroup(UUID id) {
    Group group = this.groupRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Group with id not found"));
    this.deleteGroupPermanentlyIncludingUsersAndSubGroups(getGroupTree(group.getId()));
  }

  public Set<Group> getGroupTree(UUID groupId) {
    Set<Group> groupTree = new HashSet<>();
    buildGroupTree(groupId, groupTree);
    return groupTree;
  }

  private void buildGroupTree(UUID groupId, Set<Group> groupTree) {
    Group group = this.groupRepository.findById(groupId)
        .orElseThrow(() -> new RuntimeException("Group not found with ID: " + groupId));
    groupTree.add(group);
    this.groupRepository.findAllByParentIdAndDeletedFalse(groupId)
        .forEach(subGroup ->
            buildGroupTree(subGroup.getId(), groupTree));
  }

  private void deleteGroupPermanentlyIncludingUsersAndSubGroups(Set<Group> groups) {
    Set<User> groupUsers = groups.stream()
        .flatMap(groupStream -> groupStream.getUsers().stream())
        .collect(Collectors.toSet());
    this.removeUserAndDeleteAssociation(groups, groupUsers);
    groups.forEach(this::deleteGroup);
  }

  private void removeUserAndDeleteAssociation(Set<Group> groups, Set<User> groupUsers) {
    for (User user : groupUsers) {
      this.userService.deleteUserGroupAssociation(user, groups);
    }
  }

  private List<Group> findChildrenGroupsByParentGroupId(UUID parentGroupId) {
    return this.groupRepository.findAllByParentIdAndDeletedFalse(parentGroupId);
  }


  private void recursiveDeleteGroup(UUID parentGroupId) {
    List<Group> children = this.findChildrenGroupsByParentGroupId(parentGroupId);
    if (!children.isEmpty()) {
      children.forEach(group -> {
        this.recursiveDeleteGroup(group.getId());
        this.deleteGroup(group);
      });
    }
  }

  private void deleteGroup(Group group) {
    group.setDeleted(true);
    group.setUsers(new HashSet<>());
    this.groupRepository.save(group);
  }

  private GroupDTO countGroupUsers(GroupDTO groupDTO) {
    groupDTO.setNumberOfUsers(this.groupRepository.getUserIdsByGroupId(groupDTO.getId()).size());
    return groupDTO;
  }


}
