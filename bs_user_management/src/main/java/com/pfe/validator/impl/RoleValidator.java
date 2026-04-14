package com.pfe.validator.impl;

import com.pfe.domain.Role;
import com.pfe.repository.RoleRepository;
import com.pfe.service.dto.RoleDTO;
import com.pfe.validator.IRoleValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component("roleValidator")
public class RoleValidator implements IRoleValidator {

  private final Pattern labelPattern;
  private final RoleRepository roleRepository;

  private static final String LABEL_REGEX_PATTERN = "^[a-zA-Z_-]+$";

  private static final String ERROR_MESSAGE_INVALID_PATTERN = "Invalid Pattern";
  private static final String ERROR_MESSAGE_MAX_SIZE = "Size doesn't pass 25 characters";
  private static final String ERROR_MESSAGE_REQUIRED_LABEL = "Label required";
  private static final String ROLE_WITH_LABEL_ALREADY_EXISTS = "Role with label already exists";
  private static final String AUTHORITIES_REQUIRED = "Role must contain authorities but no authorities were provided";
  private static final String DESCRIPTION_MAX_SIZE_ERROR = "Description size is over 150 characters";
  private static final String DESCRIPTION_REQUIRED_ERROR = "Description required";
  private static final String CANNOT_UPDATE_LABEL = "Role label cannot be changed";

  public RoleValidator(RoleRepository roleRepository) {
    this.labelPattern = Pattern.compile(LABEL_REGEX_PATTERN);
    this.roleRepository = roleRepository;
  }

  @Override
  public void beforeUpdate(RoleDTO roleDTO) {
    List<Reason> fieldErrors = new ArrayList<>();

    Role role = this.roleRepository.findById(roleDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException("Role with id  not found"));

    if (roleDTO.getLabel() != null) {

      Matcher labelMatcher = this.labelPattern.matcher(roleDTO.getLabel());

      if (role.getLabel().equals(roleDTO.getLabel()) && !role.getId().equals(roleDTO.getId())) {
        fieldErrors.add(new Reason("LABEL_ALREADY_EXISTS", ROLE_WITH_LABEL_ALREADY_EXISTS));
      }

      if (!labelMatcher.matches()) {
        fieldErrors.add(new Reason("INVALID_LABEL_PATTERN", ERROR_MESSAGE_INVALID_PATTERN));
      }

      if (!role.getLabel().equals(roleDTO.getLabel())) {
        fieldErrors.add(new Reason("CANNOT_UPDATE_LABEL", CANNOT_UPDATE_LABEL));
      }
    }

    if (roleDTO.getAuthorities() == null || roleDTO.getAuthorities().isEmpty()) {
      fieldErrors.add(new Reason("AUTHORITIES_REQUIRED", AUTHORITIES_REQUIRED));
    }

    if (roleDTO.getDescription() != null) {
      if (roleDTO.getDescription().length() > 150 || roleDTO.getDescription().length() < 2) {
        fieldErrors.add(new Reason("DESCRIPTION_MAX_SIZE_ERROR", DESCRIPTION_MAX_SIZE_ERROR));
      }
    } else {
      fieldErrors.add(new Reason("DESCRIPTION_REQUIRED", DESCRIPTION_REQUIRED_ERROR));
    }

    if (!fieldErrors.isEmpty()) {
      throw new ValidationException(fieldErrors);
    }

  }

  @Override
  public void beforeSave(RoleDTO roleDTO) {
    List<Reason> fieldErrors = new ArrayList<>();

    if (roleDTO.getLabel() != null) {

      Matcher labelMatcher = this.labelPattern.matcher(roleDTO.getLabel());

      if (Boolean.TRUE.equals(
          this.roleRepository.existsByLabelAndDeletedFalse(roleDTO.getLabel()))) {
        fieldErrors.add(new Reason("LABEL_ALREADY_EXISTS", ROLE_WITH_LABEL_ALREADY_EXISTS));
      }

      if (roleDTO.getLabel().length() > 25) {
        fieldErrors.add(new Reason("LABEL_MAX_SIZE_ERROR", ERROR_MESSAGE_MAX_SIZE));
      }

      if (!labelMatcher.matches()) {
        fieldErrors.add(new Reason("INVALID_LABEL_PATTERN", ERROR_MESSAGE_INVALID_PATTERN));
      }

      if (roleDTO.getLabel() == null || roleDTO.getLabel().isEmpty() || roleDTO.getLabel()
          .isBlank()) {
        fieldErrors.add(new Reason("LABEL_REQUIRED", ERROR_MESSAGE_REQUIRED_LABEL));
      }

    } else {
      fieldErrors.add(new Reason("LABEL_REQUIRED", ERROR_MESSAGE_REQUIRED_LABEL));
    }

    if (roleDTO.getAuthorities() == null || roleDTO.getAuthorities().isEmpty()) {
      fieldErrors.add(new Reason("AUTHORITIES_REQUIRED", AUTHORITIES_REQUIRED));
    }

    if (roleDTO.getDescription() != null) {
      if (roleDTO.getDescription().length() > 150 || roleDTO.getDescription().length() < 2) {
        fieldErrors.add(new Reason("DESCRIPTION_MAX_SIZE_ERROR", DESCRIPTION_MAX_SIZE_ERROR));
      }
    } else {
      fieldErrors.add(new Reason("DESCRIPTION_REQUIRED", DESCRIPTION_REQUIRED_ERROR));
    }

    if (!fieldErrors.isEmpty()) {
      throw new ValidationException(fieldErrors);
    }

  }


}
