package com.pfe.validator.impl;

import com.pfe.domain.Group;
import com.pfe.repository.GroupRepository;
import com.pfe.service.dto.GroupDTO;
import com.pfe.validator.IGroupValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("groupValidator")
public class GroupValidator implements IGroupValidator {

  private final Pattern labelPattern;
  private final GroupRepository groupRepository;

  private static final String LABEL_REGEX_PATTERN = "^[a-zA-Z_-]+$";

  private static final String ERROR_MESSAGE_INVALID_PATTERN = "Invalid Pattern";
  private static final String ERROR_MESSAGE_MAX_SIZE = "Size doesn't pass 25 characters";
  private static final String ERROR_MESSAGE_REQUIRED_LABEL = "Label required";
  private static final String GROUP_WITH_LABEL_ALREADY_EXISTS = "Group with label already exists";
  private static final String DESCRIPTION_MAX_SIZE_ERROR = "Description size is over 150 characters";
  private static final String DESCRIPTION_REQUIRED_ERROR = "Description is required";
  private static final String CANNOT_UPDATE_LABEL = "Group label cannot be changed";

  public GroupValidator(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
    this.labelPattern = Pattern.compile(LABEL_REGEX_PATTERN);
  }

  @Override
  public void beforeUpdate(GroupDTO groupDTO) {
    List<Reason> fieldErrors = new ArrayList<>();

    Group group = this.groupRepository.findById(groupDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException("Group with id  not found"));

    if (groupDTO.getLabel() != null) {
      Matcher labelMatcher = this.labelPattern.matcher(groupDTO.getLabel());

      if (group.getLabel().equals(groupDTO.getLabel()) && !group.getId().equals(groupDTO.getId())) {
        fieldErrors.add(new Reason("LABEL_ALREADY_EXISTS", GROUP_WITH_LABEL_ALREADY_EXISTS));
      }

      if (!labelMatcher.matches()) {
        fieldErrors.add(new Reason("INVALID_LABEL_PATTERN", ERROR_MESSAGE_INVALID_PATTERN));
      }

      if (groupDTO.getLabel().length() > 25) {
        fieldErrors.add(new Reason("LABEL_MAX_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
      }

      if (groupDTO.getLabel().isEmpty() || groupDTO.getLabel()
          .isBlank()) {
        fieldErrors.add(new Reason("LABEL_REQUIRED", ERROR_MESSAGE_REQUIRED_LABEL));
      }
    } else {
      fieldErrors.add(new Reason("LABEL_REQUIRED", ERROR_MESSAGE_REQUIRED_LABEL));
    }

    if (groupDTO.getDescription() != null) {
      if (groupDTO.getDescription().length() > 150) {
        fieldErrors.add(new Reason("DESCRIPTION_MAX_SIZE_ERROR", DESCRIPTION_MAX_SIZE_ERROR));
      }

      if (groupDTO.getDescription().isBlank() || groupDTO.getDescription().isEmpty()
          || groupDTO.getDescription() == null) {
        fieldErrors.add(new Reason("DESCRIPTION_REQUIRED_ERROR", DESCRIPTION_REQUIRED_ERROR));
      }
    } else {
      fieldErrors.add(new Reason("DESCRIPTION_REQUIRED_ERROR", DESCRIPTION_REQUIRED_ERROR));
    }

    if (!fieldErrors.isEmpty()) {
      throw new ValidationException(fieldErrors);
    }

  }

  @Override
  public void beforeSave(GroupDTO groupDTO) {
    List<Reason> fieldErrors = new ArrayList<>();

    if (groupDTO.getLabel() != null) {
      Matcher labelMatcher = this.labelPattern.matcher(groupDTO.getLabel());

      if (!labelMatcher.matches()) {
        fieldErrors.add(new Reason("INVALID_LABEL_PATTERN", ERROR_MESSAGE_INVALID_PATTERN));
      }

      if (groupDTO.getLabel().length() > 25) {
        fieldErrors.add(new Reason("LABEL_MAX_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
      }

      if (Boolean.TRUE.equals(
          this.groupRepository.existsByLabelAndDeletedFalse(groupDTO.getLabel()))) {
        fieldErrors.add(new Reason("LABEL_ALREADY_EXISTS", GROUP_WITH_LABEL_ALREADY_EXISTS));
      }

      if (groupDTO.getLabel().isEmpty() || groupDTO.getLabel()
          .isBlank()) {
        fieldErrors.add(new Reason("LABEL_REQUIRED", ERROR_MESSAGE_REQUIRED_LABEL));
      }
    } else {
      fieldErrors.add(new Reason("LABEL_REQUIRED", ERROR_MESSAGE_REQUIRED_LABEL));
    }

    if (groupDTO.getDescription() != null) {
      if (groupDTO.getDescription().length() > 150) {
        fieldErrors.add(new Reason("DESCRIPTION_MAX_SIZE_ERROR", DESCRIPTION_MAX_SIZE_ERROR));
      }

      if (groupDTO.getDescription().isBlank() || groupDTO.getDescription().isEmpty()
          || groupDTO.getDescription() == null) {
        fieldErrors.add(new Reason("DESCRIPTION_REQUIRED_ERROR", DESCRIPTION_REQUIRED_ERROR));
      }
    } else {
      fieldErrors.add(new Reason("DESCRIPTION_REQUIRED_ERROR", DESCRIPTION_REQUIRED_ERROR));
    }

    if (!fieldErrors.isEmpty()) {
      throw new ValidationException(fieldErrors);
    }

  }


}
