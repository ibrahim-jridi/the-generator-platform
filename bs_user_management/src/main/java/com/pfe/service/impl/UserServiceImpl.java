package com.pfe.service.impl;

import com.pfe.config.Constants;
import com.pfe.domain.*;
import com.pfe.domain.DesignationsList;
import com.pfe.domain.enumeration.Gender;
import com.pfe.domain.enumeration.Nationality;
import com.pfe.domain.enumeration.UserType;
import com.pfe.feignService.FileServices;
import com.pfe.feignService.NotificationService;
import com.pfe.repository.*;
import com.pfe.repository.DesignationsListRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.security.Jwt.JwtTokenUtil;
import com.pfe.service.DesignationsListService;
import com.pfe.service.KeycloakAuthService;
import com.pfe.service.UserService;
import com.pfe.service.dto.*;
import com.pfe.service.dto.request.*;
import com.pfe.service.dto.ActivityTypeDTO;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.dto.ProfileDTO;
import com.pfe.service.dto.ResetPasswordDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.dto.UserDenominationDTO;
import com.pfe.service.dto.request.AssignRoleApplication;
import com.pfe.service.dto.request.ChangePasswordRequestDTO;
import com.pfe.service.dto.request.CreateAccountRequest;
import com.pfe.service.dto.request.CreateCompanyAccountRequest;
import com.pfe.service.dto.request.CreateFolderRequest;
import com.pfe.service.dto.request.UpdateProfileRequest;
import com.pfe.service.dto.response.CheckEligibilityResponse;
import com.pfe.service.mapper.*;
import com.pfe.service.mapper.ActivityTypeMapper;
import com.pfe.validator.ICreateAccountRequestValidator;
import com.pfe.validator.ICreateCompanyAccountRequestValidator;
import com.pfe.validator.impl.UserValidator;
import com.pfe.web.rest.errors.ValidationException;
import com.pfe.domain.ActivityType;
import com.pfe.domain.Authority;
import com.pfe.domain.Group;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.repository.ActivityTypeRepository;
import com.pfe.repository.GroupRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.service.mapper.AuthorityMapper;
import com.pfe.service.mapper.GroupMapper;
import com.pfe.service.mapper.RoleMapper;
import com.pfe.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link User}.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final KeycloakAuthService keycloakAuthService;

    private final Keycloak keycloak;

    private final JwtTokenUtil jwtTokenUtil;

    private final NotificationService notificationService;
    private final DesignationsListService designationsListService;

    private final RoleMapper roleMapper;

    private final FileServices fileServices;

    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final UserValidator userValidator;
    private final ICreateCompanyAccountRequestValidator createCompanyAccountRequestValidator;
    private final ICreateAccountRequestValidator createAccountRequestValidator;

    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityTypeMapper activityTypeMapper;
    @Value("${bs-app.keycloak.realm}")
    private String realm;

    @Value(value = "${bs-app.endpoint.front-url}")
    private String frontUrl;

    @Value(value = "${bs-app.keycloak.resource}")
    private String clientIdName;

    @Value("${token.validity-duration}")
    private Long validityDuration;

    private static final String CACHE_NAME = "applicantCache";
    private final DesignationsListRepository designationsListRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           KeycloakAuthService keycloakAuthService, Keycloak keycloak, JwtTokenUtil jwtTokenUtil,
                           NotificationService notificationService, GroupMapper groupMapper, RoleMapper roleMapper,
                           AuthorityMapper authorityMapper, FileServices fileServices, RoleRepository roleRepository,
                           GroupRepository groupRepository,
                           UserValidator userValidator1,
                           ICreateCompanyAccountRequestValidator createCompanyAccountRequestValidator,
                           ICreateAccountRequestValidator createAccountRequestValidator,
                           ActivityTypeRepository activityTypeRepository, ActivityTypeMapper activityTypeMapper, DesignationsListService designationsListService, DesignationsListRepository designationsListRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakAuthService = keycloakAuthService;
        this.keycloak = keycloak;
        this.jwtTokenUtil = jwtTokenUtil;
        this.notificationService = notificationService;
        this.roleMapper = roleMapper;
        this.fileServices = fileServices;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.userValidator = userValidator1;
        this.createCompanyAccountRequestValidator = createCompanyAccountRequestValidator;
        this.createAccountRequestValidator = createAccountRequestValidator;
        this.activityTypeRepository = activityTypeRepository;
        this.activityTypeMapper = activityTypeMapper;
        this.designationsListService = designationsListService;
        this.designationsListRepository = designationsListRepository;
    }


    public static String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$&*-+?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        log.debug("Request to save User : {}", userDTO);
        User user = this.userMapper.toEntity(userDTO);
        user = this.userRepository.save(user);
        addActivityTypeForCompnay(userDTO, user);
        return this.userMapper.toDto(user);
    }

    private void addActivityTypeForCompnay(UserDTO userDTO, User user) {
        User finalUser = this.userRepository.findById(user.getId())
            .orElseThrow(
                () -> new IllegalArgumentException("User not found for ID: " + user.getId()));

        userDTO.getActivitiesType().forEach(activityDTO -> {
            activityDTO.setUserId(finalUser.getId());
        });

        List<ActivityType> activityTypes = this.activityTypeMapper.toEntity(
            userDTO.getActivitiesType().stream().toList()
        );

        activityTypes.forEach(activity -> {
            activity.setUser(user);
        });

        this.activityTypeRepository.saveAll(activityTypes);
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        log.debug("Request to update User : {}", userDTO);
        User user = this.userRepository.findById(userDTO.getId())
            .orElseThrow(null);
        this.userValidator.beforeUpdate(userDTO);
        if (user != null) {
            UsersResource usersResource = this.keycloak.realm(this.realm).users();
            UserResource userResource = usersResource.get(
                String.valueOf(userDTO.getKeycloakId()));
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userRepresentation.setEmail(userDTO.getEmail());

            if (user.getUserType().equals(UserType.COMPANY)) {
                List<ActivityType> userActivityTpeList = this.activityTypeRepository.findByUserAndDeletedFalse(
                    user);
                userActivityTpeList.forEach(activityType -> {
                    activityType.setDeleted(true);
                    this.activityTypeRepository.save(activityType);
                });

                addActivityTypeForCompnay(userDTO, user);
                userRepresentation.setFirstName(userDTO.getTaxRegistration());
                userRepresentation.setLastName(userDTO.getSocialReason());
            } else {
                userRepresentation.setFirstName(userDTO.getFirstName());
                userRepresentation.setLastName(userDTO.getLastName());
            }
            if (userDTO.getPassword() != null) {
                userRepresentation.setCredentials(
                    Collections.singletonList(createPasswordCredential(userDTO.getPassword())));
            }

            userResource.update(userRepresentation);
            this.userRepository.save(this.userMapper.toEntity(userDTO));
            updateUserAuthorityInKeycloak(this.keycloak, user.getKeycloakId(), user.getId());

            return this.userMapper.toDto(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Internal User with Email " + userDTO.getEmail() + " is  exist");
        }
    }

    @Override
    public Optional<UserDTO> partialUpdate(UserDTO userDTO) {
        log.debug("Request to partially update User : {}", userDTO);

        return this.userRepository.findById(userDTO.getId())
            .map(existingUser -> {
                this.userMapper.partialUpdate(existingUser, userDTO);

                return existingUser;
            }).map(this.userRepository::save).map(this.userMapper::toDto);
    }

    @Override
    public Optional<UserDTO> completeUserProfile(UserDTO userDTO, String id) {
        log.debug("Request to complete User : {}", userDTO);
        updateUserAttribute(id, "profile_completed", "true");
        return partialUpdate(userDTO);
    }


    @Override
    public List<UserDTO> getUsersByGroupId(UUID id) {
        return this.userRepository.findAllUsersByGroupId(id).stream().map(this.userMapper::toDto)
            .toList();
    }

    @Override
    public void reassignUsersToNewGroups(List<UserDTO> usersToReassign) {
        usersToReassign.forEach(userDTO -> {
            User user = this.userRepository.findById(userDTO.getId())
                .orElse(null);
            if (user != null) {
                Set<Group> newUserGroups = this.groupRepository.findAllByIdInAndDeletedFalse(
                    userDTO.getGroups().stream().map(GroupDTO::getId)
                        .collect(Collectors.toSet()));
                user.setGroups(newUserGroups);
                this.updateKeycloakAttributes(user.getKeycloakId(),
                    user.getGroups().stream().map(Group::getLabel).toList(), "groups");
                this.userRepository.save(user);
            }
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(UUID id) {
        log.debug("Request to get User : {}", id);
        User user = this.userRepository.findById(id).get();
        List<ActivityType> userActivityTypes = user.getActivitiesType().stream()
            .filter(activityType ->activityType.getDeleted() == null || !activityType.getDeleted())
            .sorted(
                Comparator.comparing(ActivityType::getCreatedDate)).collect(Collectors.toList());
        Set<ActivityTypeDTO> activityTypesDto = userActivityTypes.stream()
            .map(this.activityTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        UserDTO userDTO = this.userMapper.toDto(user);
        userDTO.setActivitiesType(activityTypesDto);
        return Optional.ofNullable(userDTO);
    }

    @Override
    public User findById(UUID id) {
        log.debug("Request to get User : {}", id);
        User user = this.userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User with id not found"));
        return user;
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete User : {}", id);
        User user = this.userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User with id not found"));
        user.setDeleted(true);
        user.getGroups().clear();
        user.getRoles().clear();
        this.removeKeycloakAttributes(user.getKeycloakId(), "groups");
        this.removeKeycloakAttributes(user.getKeycloakId(), "roles");
        this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Request to get User : {}", username);
        return this.userRepository.findByUsername(username);
    }

    @Override
    public void verifyEmail(String token) {
        UserDTO userDTO;
        try {
            log.debug("Calling getUserByToken...");
            userDTO = this.keycloakAuthService.getUserByToken(token);
            log.debug("getUserByToken called");
        } catch (Exception e) {
            log.error("Error decoding token", e);
            throw new IllegalArgumentException("Invalid token");
        }

        try {
            log.debug("Calling beforeSave...");
            this.userValidator.beforeSave(userDTO);
            log.debug("beforeSave called");
        } catch (Exception e) {
            log.error("Validation failed", e);
            throw new IllegalArgumentException("Invalid user data");
        }

        if (findByUsername(userDTO.getUsername()).isPresent()) {
            log.warn("User already exists with username {}", userDTO.getUsername());
            throw new IllegalArgumentException("User already exists");
        }

        try {
            log.debug("Calling setEmailVerified...");
            this.keycloakAuthService.setEmailVerified(userDTO.getId());
            log.debug("setEmailVerified done.");
            userDTO.setEmailVerified(true);
            log.debug("Saving user...");
            UserDTO user = save(userDTO);
            log.debug("User saved.");
            if (user.getUserType() == UserType.COMPANY) {
                log.debug("Updating designations...");
                this.designationsListService.updateDesignationWithUserPmId(user);
                log.debug("Designations updated.");
                this.designationsListService.saveDesignationsUsersAttributes(user.getUsername());
            }
            this.updateUserAuthorityInKeycloak(keycloak, userDTO.getKeycloakId(), userDTO.getId());
            log.debug("Add ID attribute...");
            addIdAttribute(userDTO.getKeycloakId());
            log.debug(" ID attribute added");

            CreateFolderRequest createFolderRequest = new CreateFolderRequest();
            createFolderRequest.setNewFolderName(userDTO.getUsername());
            createFolderRequest.setParentId(user.getId());
            this.fileServices.createFolder(createFolderRequest);
        } catch (Exception e) {
            log.error("Error while saving user or creating folder", e);
            throw new RuntimeException("Failed to finalize registration");
        }
    }


    public void addIdAttribute(UUID keycloackId) {
        try {
            User user = this.userRepository.findByKeycloakId(keycloackId);

            if (user == null) {
                log.warn("User not found in database for keycloakId: {}", keycloackId);
                return;
            }

            UsersResource usersResource = this.keycloak.realm(this.realm).users();
            UserResource userResource = usersResource.get(String.valueOf(keycloackId));
            UserRepresentation userRepresentation = userResource.toRepresentation();

            if (userRepresentation == null) {
                log.error("User representation not found in Keycloak for keycloakId: {}", keycloackId);
                return;
            }

            Map<String, List<String>> attributes = userRepresentation.getAttributes();
            if (attributes == null) {
                attributes = new HashMap<>();
            }

            attributes.put(Constants.USER_ID,
                Collections.singletonList(String.valueOf(user.getId())));
            userRepresentation.setAttributes(attributes);

            userResource.update(userRepresentation);

            log.debug("Successfully added USER_ID attribute to Keycloak user: {}", keycloackId);

        } catch (Exception e) {
            log.error("Error adding ID attribute for user {}: {}", keycloackId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public UserDTO createUser(CreateAccountRequest createAccountRequest, Boolean confirm) {
        if(!confirm) {
            List<User> userWithNationalIdExist = userRepository.findAllByNationalIdAndDeletedFalse(createAccountRequest.getNationalId());
            if(userWithNationalIdExist.size()>=2){
                throw new ValidationException("NATIONAL_ID_EXIST");
            }
            try {
                this.createAccountRequestValidator.beforeSave(createAccountRequest);

            }

            catch (ValidationException e) {
                boolean isNationalIdExist = "NATIONAL_ID_EXIST".equals(e.getMessage());

                if (!isNationalIdExist) {
                    throw e;
                } else {
                    boolean isInternalUser = userWithNationalIdExist.stream()
                        .anyMatch(user -> user.getUsername() != null &&
                            user.getUsername().toLowerCase().startsWith("int"));
                    if (isInternalUser) {
                        throw new ValidationException("NATIONAL_ID_EXIST");
                    } else {
                        User externalUser = userWithNationalIdExist.stream()
                            .filter(user -> user.getUsername() != null &&
                                user.getUsername().toLowerCase().startsWith("ext"))
                            .findFirst()
                            .orElseThrow(() -> e);

                        UserDTO existingUserDto = userMapper.toDto(externalUser);
                        return ResponseEntity.ok(existingUserDto).getBody();
                    }
                }

            }
        }
        String password = generatePassword();

        AccessTokenResponse tokenResponse = this.keycloak.tokenManager().getAccessToken();
        UUID connectedUserId = this.keycloakAuthService.getUserIdFromToken(tokenResponse);
        RealmResource realmResource = this.keycloak.realm(this.realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = initUserRepresentation(createAccountRequest, password,
            connectedUserId);

        try {
            Response response = usersResource.create(user);
            if (response.getStatus() == 201) {
                Set<RoleDTO> roleListDTO = createAccountRequest.getRoles();
                Set<GroupDTO> groupeListDTO = createAccountRequest.getGroups();
                return this.setUserDTO(response, usersResource,
                    groupeListDTO, roleListDTO, password);
            } else {
                throw new IllegalArgumentException(
                    "Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            }

        } catch (WebApplicationException e) {
            Response response = e.getResponse();
            String errorMessage = response.readEntity(String.class);
            throw new IllegalArgumentException("Failed to create user: " + errorMessage);
        }
    }

    @Override
    public UserDTO createCompanyUser(CreateCompanyAccountRequest createCompanyAccountRequest) {
        this.createCompanyAccountRequestValidator.beforeSave(createCompanyAccountRequest);
        String password = generatePassword();

        AccessTokenResponse tokenResponse = this.keycloak.tokenManager().getAccessToken();
        UUID connectedUserId = this.keycloakAuthService.getUserIdFromToken(tokenResponse);
        RealmResource realmResource = this.keycloak.realm(this.realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation user = initCompanyUserRepresentation(createCompanyAccountRequest,
            password,
            connectedUserId);

        try {
            Response response = usersResource.create(user);
            if (response.getStatus() == 201) {
                Set<RoleDTO> roleListDTO = createCompanyAccountRequest.getRoles();
                return this.setCompanyUserDTO(response, usersResource, password, roleListDTO);
            } else {
                throw new IllegalArgumentException(
                    "Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            }

        } catch (WebApplicationException e) {
            Response response = e.getResponse();
            String errorMessage = response.readEntity(String.class);
            throw new IllegalArgumentException("Failed to create user: " + errorMessage);
        }
    }

    @Override
    public void checkUserInformations(String gender, String nationalId, String phoneNumber,
        String email, String lastName, String nationality, String country, String firstName,
        String birthDate, String address) {

        String cleanedDateString = birthDate.trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(cleanedDateString, formatter);

        UserDTO userDTO = new UserDTO();
        userDTO.setAddress(address);
        userDTO.setBirthDate(localDate);
        userDTO.setNationality(Nationality.valueOf(nationality.toUpperCase()));
        userDTO.setCountry(country);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setNationalId(nationalId);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setEmail(email);
        userDTO.setGender(Gender.valueOf(gender.toUpperCase()));
        this.userValidator.checkUserInformations(userDTO);
    }

    @Override
    public UserDTO findUserByFiscalId(String fiscalId) {
        return this.userMapper.toDto(
            this.userRepository.findUserByTaxRegistrationAndDeletedFalse(fiscalId).orElse(null));

    }

    @Override
    public UserDTO findUserByUsername(String username) {
        log.debug("Request to get User by username : {}", username);
        return this.userMapper.toDto(
            this.userRepository.findByUsernameIgnoreCaseAndDeletedFalse(username).orElse(null));
    }

    @Override
    public void resetPasswordRequest(ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.debug("Request to change password for user by username : {}",
            changePasswordRequestDTO.getUsername());
        User user = this.userRepository.findByUsernameIgnoreCaseAndDeletedFalse(
                changePasswordRequestDTO.getUsername())
            .orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getEmail().equals(changePasswordRequestDTO.getEmail())) {
            throw new RuntimeException("Bad username or email");
        }
        this.sendResetPasswordEmail(user);
    }

    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO, String token) {
        User user = this.userRepository.findByUsernameIgnoreCaseAndDeletedFalse(
                resetPasswordDTO.getUsername())
            .orElseThrow(() -> new RuntimeException("User with username not found"));
        this.userValidator.beforePasswordChange(resetPasswordDTO, token);

        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(resetPasswordDTO.getNewPassword());
        newCredential.setTemporary(false);
        this.keycloak.realm(this.realm).users().get(user.getKeycloakId().toString())
            .resetPassword(newCredential);
    }

    @Override
    public void resetPasswordFirstConnection(ResetPasswordDTO resetPasswordDTO) {
        User user = this.userRepository.findByUsernameIgnoreCaseAndDeletedFalse(
                resetPasswordDTO.getUsername())
            .orElseThrow(() -> new RuntimeException("User with username not found"));

        UsersResource usersResource = this.keycloak.realm(this.realm).users();
        UserResource userResource = usersResource.get(String.valueOf(user.getKeycloakId()));
        UserRepresentation userRepresentation = userResource.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        attributes.remove("first_connection");
        attributes.put("first_connection", Collections.singletonList(String.valueOf(false)));
        userResource.update(userRepresentation);

        CredentialRepresentation newCredential = new CredentialRepresentation();
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(resetPasswordDTO.getNewPassword());
        newCredential.setTemporary(false);
        this.keycloak.realm(this.realm).users().get(user.getKeycloakId().toString())
            .resetPassword(newCredential);

        if (user.getUserType() == UserType.COMPANY) {
            log.debug("Updating designations...");
            this.designationsListService.updateDesignationWithUserPmId(userMapper.toDto(user));
            log.debug("Designations updated.");
            this.designationsListService.saveDesignationsUsersAttributes(user.getUsername());
        } else if (user.getUserType() == UserType.PHYSICAL) {
            List<DesignationsList> designationsLists = this.designationsListService.getListByUserId(user.getId());
            designationsLists.forEach(designationsList -> {
                log.debug("Updating designations...");
                this.designationsListService.updateDesignationWithUserPmId(userMapper.toDto(designationsList.getPmUser()));
                log.debug("Designations updated.");
                this.designationsListService.saveDesignationsUsersAttributes(designationsList.getPmUser().getUsername());
            });
        }
    }

    private void sendResetPasswordEmail(User user) {
        log.debug("Start searching for validity token");
        String validityToken = this.jwtTokenUtil.generateToken(
            UUID.fromString(String.valueOf(user.getId())), validityDuration, user.getUsername());
        String resetPasswordLink =
            this.frontUrl + "/auth/forgot-password?token=" + validityToken;
        this.notificationService.sendPasswordResetEmail(user.getEmail(), user.getUsername(),
            resetPasswordLink);
    }

    @Override
    public List<UserDTO> getUsersByRoleId(UUID id) {
        return this.userRepository.findAllUsersByRoleId(id).stream().map(this.userMapper::toDto)
            .toList();
    }

    @Override
    public void reassignUsersToNewRoles(List<UserDTO> usersToReassign) {
        usersToReassign.forEach(userDTO -> {
            User user = this.userRepository.findById(userDTO.getId())
                .orElse(null);
            if (user != null) {
                Set<Role> newUserRoles = this.roleRepository.findAllByIdInAndDeletedFalse(
                    userDTO.getRoles().stream().map(RoleDTO::getId)
                        .collect(Collectors.toSet()));
                user.setRoles(newUserRoles);
                this.updateUserAuthorityInKeycloak(this.keycloak, user.getKeycloakId(),
                    user.getId());
                this.updateKeycloakAttributes(user.getKeycloakId(),
                    user.getRoles().stream().map(Role::getLabel).toList(), "roles");
                this.userRepository.save(user);
            }
        });
    }

    @Override
    public List<UserDTO> getAllPhysicalUsers() {
        return this.userMapper.toDto(
            this.userRepository.findAllUsersByUserTypeAndDeletedFalse(UserType.PHYSICAL));
    }

    private UserRepresentation initUserRepresentation(CreateAccountRequest createAccountRequest,
        String password, UUID connectedUserId) {
        UserRepresentation user = new UserRepresentation();
        String username = "INT-" + generateUniqueNumber();
        user.setUsername(username);
        user.setEmail(createAccountRequest.getEmail());
        user.setFirstName(createAccountRequest.getFirstName());
        user.setLastName(createAccountRequest.getLastName());
        user.setEnabled(true);
        Set<RoleDTO> rolesLabelList = createAccountRequest.getRoles();
        List<String> rolesValues = new ArrayList<>();
        rolesValues.add(Constants.DEFAULT_ROLE);
        rolesLabelList.forEach(el -> rolesValues.add(el.getLabel()));
        this.keycloakAuthService.addCustomAttributeListToUser(user, "roles", rolesValues);
        this.keycloakAuthService.addCustomAttributeListToUser(user, "groups",
            createAccountRequest.getGroups().stream().map(GroupDTO::getLabel).toList());
        this.keycloakAuthService.addCustomAttributeToUser(user, "createdBy",
            String.valueOf(connectedUserId));
        this.keycloakAuthService.addCustomAttributeToUser(user, "address",
            createAccountRequest.getAddress());
        this.keycloakAuthService.addCustomAttributeToUser(user, "country",
            createAccountRequest.getCountry());
        this.keycloakAuthService.addCustomAttributeToUser(user, "gender",
            createAccountRequest.getGender());
        this.keycloakAuthService.addCustomAttributeToUser(user, "nationalId",
            createAccountRequest.getNationalId());
        this.keycloakAuthService.addCustomAttributeToUser(user, "birthDate",
            createAccountRequest.getBirthDate().toString());
        this.keycloakAuthService.addCustomAttributeToUser(user, "nationality",
            String.valueOf(createAccountRequest.getNationality()));
        this.keycloakAuthService.addCustomAttributeToUser(user, "phoneNumber",
            createAccountRequest.getPhoneNumber());
        this.keycloakAuthService.addCustomAttributeToUser(user, "nationalId",
            createAccountRequest.getNationalId());
        this.keycloakAuthService.addCustomAttributeToUser(user, "profile_completed",
            String.valueOf(false));
        this.keycloakAuthService.addCustomAttributeToUser(user, "first_connection",
            String.valueOf(true));

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    private UserRepresentation initCompanyUserRepresentation(
        CreateCompanyAccountRequest createCompanyAccountRequest,
        String password, UUID connectedUserId) {
        UserRepresentation user = new UserRepresentation();
        String username = "pm-ext-" + generateUniqueNumber();
        user.setUsername(username);
        user.setEmail(createCompanyAccountRequest.getEmail());
        user.setFirstName(createCompanyAccountRequest.getDenomination());
        user.setLastName(createCompanyAccountRequest.getSocialReason());
        user.setEnabled(true);
        Set<RoleDTO> rolesLabelList = createCompanyAccountRequest.getRoles();
        List<String> rolesValues = new ArrayList<>();
        rolesValues.add(Constants.DEFAULT_ROLE);
        rolesLabelList.forEach(el -> rolesValues.add(el.getLabel()));
        this.keycloakAuthService.addCustomAttributeListToUser(user, "roles", rolesValues);
        this.keycloakAuthService.addCustomAttributeToUser(user, "groups", "GLOBAL_GROUP");
        this.keycloakAuthService.addCustomAttributeToUser(user, "createdBy",
            String.valueOf(connectedUserId));
        this.keycloakAuthService.addCustomAttributeToUser(user, "address",
            createCompanyAccountRequest.getAddress());
        this.keycloakAuthService.addCustomAttributeToUser(user, "country",
            createCompanyAccountRequest.getCountry());
        this.keycloakAuthService.addCustomAttributeToUser(user, "nationality",
            String.valueOf(createCompanyAccountRequest.getNationality()));
       /*this.keycloakAuthService.addCustomAttributeToUser(user, "creationDate",
            createCompanyAccountRequest.getCreationDate().toString());*/
        this.keycloakAuthService.addCustomAttributeToUser(user, "phoneNumber",
            createCompanyAccountRequest.getPhoneNumber());
        this.keycloakAuthService.addCustomAttributeToUser(user, "profile_completed",
            String.valueOf(false));
        this.keycloakAuthService.addCustomAttributeToUser(user, "activityDomain",
            createCompanyAccountRequest.getActivityDomain());
        this.keycloakAuthService.addCustomAttributeToUser(user, "legalStatus",
            createCompanyAccountRequest.getLegalStatus());
        this.keycloakAuthService.addCustomAttributeToUser(user, "userType",
            UserType.COMPANY.toString());
        this.keycloakAuthService.addCustomAttributeToUser(user, "first_connection",
            String.valueOf(true));

        Set<ActivityTypeDTO> activitiesType = createCompanyAccountRequest.getActivitiesType();
        if (activitiesType != null && !activitiesType.isEmpty()) {
            List<String> activities = activitiesType.stream()
                .map(activity -> activity.getCode() + ":" + activity.getName())
                .collect(Collectors.toList());

            this.keycloakAuthService.addCustomAttributeListToUser(user, "activitiesType",
                activities);
        }
        this.keycloakAuthService.addCustomAttributeToUser(user, "fileStatus",
            createCompanyAccountRequest.getFileStatus());
        this.keycloakAuthService.addCustomAttributeToUser(user, "filePatent",
            createCompanyAccountRequest.getFilePatent());
        this.keycloakAuthService.addCustomAttributeToUser(user, "denomination",
            createCompanyAccountRequest.getDenomination());
        this.keycloakAuthService.addCustomAttributeToUser(user, "registryStatus",
            String.valueOf(createCompanyAccountRequest.getRegistryStatus()));
        this.keycloakAuthService.addCustomAttributeToUser(user, "taxRegistration",
            createCompanyAccountRequest.getTaxRegistration());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    public UserDTO setUserDTO(Response response, UsersResource usersResource,
        Set<GroupDTO> groupDTOs, Set<RoleDTO> roleListDTO, String password) {
        UUID userId = UUID.fromString(
            response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));
        UserRepresentation createdUser = usersResource.get(userId.toString()).toRepresentation();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(createdUser.getUsername());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setFirstName(createdUser.getFirstName());
        userDTO.setLastName(createdUser.getLastName());
        Set<GroupDTO> groupListToSave = getGroupDTOS(groupDTOs);
        Set<RoleDTO> roleListToSave = getRoleDTOSAndAuthorities(roleListDTO, userId);
        userDTO.setRoles(roleListToSave);
        userDTO.setGroups(groupListToSave);
        userDTO.setUserType(UserType.PHYSICAL);
        userDTO.setId(userId);
        userDTO.setPassword(password);
        userDTO.setProfileCompleted(false);
        userDTO.setPassword(password);
        sendVerificationEmail(String.valueOf(userId), createdUser.getUsername(),
            createdUser.getEmail(),
            password);
        return userDTO;
    }

    public UserDTO setCompanyUserDTO(Response response, UsersResource usersResource,
        String password, Set<RoleDTO> roleListDTO) {
        UUID userId = UUID.fromString(
            response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));
        UserRepresentation createdUser = usersResource.get(userId.toString()).toRepresentation();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(createdUser.getUsername());
        userDTO.setEmail(createdUser.getEmail());
        Set<RoleDTO> roleListToSave = getRoleDTOSAndAuthorities(roleListDTO, userId);
        userDTO.setRoles(roleListToSave);
        assignAuthorityToUser(this.keycloak, String.valueOf(userId), Constants.BS_DESIGNATIONS_USERS_AUTHORITY);
        userDTO.setTaxRegistration(createdUser.getFirstName());
        userDTO.setSocialReason(createdUser.getLastName());
        if (createdUser.getAttributes().containsKey("address")) {
            userDTO.setAddress(createdUser.getAttributes().get("address").get(0));
        }
        if (createdUser.getAttributes().containsKey("activityDomain")) {
            userDTO.setActivityDomain(createdUser.getAttributes().get("activityDomain").get(0));
        }
        if (createdUser.getAttributes().containsKey("legalStatus")) {
            userDTO.setLegalStatus(createdUser.getAttributes().get("legalStatus").get(0));
        }
        userDTO.setUserType(UserType.COMPANY);
        userDTO.setId(userId);
        userDTO.setPassword(password);
        userDTO.setProfileCompleted(false);
        userDTO.setPassword(password);
        sendVerificationEmail(String.valueOf(userId), createdUser.getUsername(),
            createdUser.getEmail(),
            password);
        return userDTO;
    }
    @Override
    public UUID getUserIdFromResponse(UUID userId) {
        return userId;
    }
    private Set<RoleDTO> getRoleDTOSAndAuthorities(Set<RoleDTO> roleListDTO, UUID userId) {
        Set<AuthorityDTO> allAuthorities = new HashSet<>();
        Set<RoleDTO> roleListToSave = getRoleDTOS(roleListDTO,
            allAuthorities);
        allAuthorities.forEach(authtority ->
            assignAuthorityToUser(this.keycloak, String.valueOf(userId), authtority.getLabel())
        );
        return roleListToSave;
    }

    private Set<RoleDTO> getRoleDTOS(Set<RoleDTO> roleListDTO, Set<AuthorityDTO> allAuthorities) {
        log.info("getRoleDTOS : {}", roleListDTO);
        Set<RoleDTO> roleListToSave = new HashSet<>();
        Set<RoleDTO> listRoles = new HashSet<>();
        roleListDTO.forEach(role -> {
            if (role.getLabel() == null || role.getLabel().equals("")) return;
            log.info("fetching role : {}", role);
            RoleDTO roleDTO = this.roleMapper.toDto(
                this.roleRepository.findByLabel(role.getLabel())
                    .orElseThrow(
                        () -> new EntityNotFoundException("Role with label does not exists")));
            listRoles.add(roleDTO);
        });
        listRoles.forEach(el -> {
            RoleDTO roleDto = new RoleDTO();
            roleDto.setId(el.getId());
            roleDto.setDescription(el.getDescription());
            roleDto.setLabel(el.getLabel());
            roleListToSave.add(roleDto);
            allAuthorities.addAll(el.getAuthorities());
        });
        return roleListToSave;
    }

    private static Set<GroupDTO> getGroupDTOS(Set<GroupDTO> groupDTOs) {
        Set<GroupDTO> groupListToSave = new HashSet<>();

        groupDTOs.forEach(gr -> {
            GroupDTO groupDTO = new GroupDTO();
            groupDTO.setParent(gr.getParent());
            groupDTO.setId(gr.getId());
            groupDTO.setLabel(gr.getLabel());
            groupDTO.setDescription(gr.getDescription());
            groupListToSave.add(groupDTO);
        });
        return groupListToSave;
    }

    private void assignAuthorityToUser(Keycloak keycloak, String userId, String roleName) {
        String clientId = this.getClientId();
        List<RoleRepresentation> clientRoles = keycloak.realm(this.realm).clients().get(clientId)
            .roles().list();

        RoleRepresentation roleToAssign = clientRoles.stream()
            .filter(role -> role.getName().equals(roleName)).findFirst().orElse(null);

        if (roleToAssign != null) {
            keycloak.realm(this.realm).users().get(userId).roles().clientLevel(clientId)
                .add(Collections.singletonList(roleToAssign));
        } else {
            log.info("no role to add");
        }
    }

    private void updateUserAuthorityInKeycloak(Keycloak keycloak, UUID userKeycloakId,
            UUID userId) {
            User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id not found"));
            Set<Authority> userAuthorities = user.getRoles()
                .stream()
                .flatMap(role -> role.getAuthorities().stream())
                .collect(Collectors.toSet());
            String clientId = this.getClientId();
        List<RoleRepresentation> keycloakRoles = keycloak.realm(this.realm).users()
            .get(String.valueOf(userKeycloakId)).roles().clientLevel(clientId).listAll();
        keycloakRoles.forEach(role ->
            keycloak.realm(this.realm).users().get(String.valueOf(userKeycloakId)).roles()
                .clientLevel(clientId).remove(Collections.singletonList(role))
        );


        List<RoleRepresentation> rolesToAssign = new ArrayList<>();
        for (Authority authority : userAuthorities) {

            List<RoleRepresentation> clientRoles = keycloak.realm(this.realm).clients()
                .get(clientId)
                .roles().list();

            RoleRepresentation roleToAssign = clientRoles.stream()
                .filter(role -> role.getName().equals(authority.getLabel())).findFirst()
                .orElse(null);

            if (roleToAssign != null) {
                rolesToAssign.add(roleToAssign);
            }
        }
        keycloak.realm(this.realm).users().get(String.valueOf(userKeycloakId)).roles()
            .clientLevel(clientId).add(rolesToAssign);
    }

    public String getClientId() {
        List<ClientRepresentation> clients = this.keycloak.realm(this.realm).clients()
            .findByClientId(this.clientIdName);
        if (clients.isEmpty()) {
            log.info("Client not found: {}", this.clientIdName);
            return null;
        }
        return clients.get(0).getId();
    }

    @Override
    public UserDTO deleteUser(UUID id) {
        log.debug("Request to delete User : {}", id);
        UserDTO userDTO = findOne(id).orElseThrow(
            () -> new EntityNotFoundException("Internal User with id : " + id + "not found"));
        userDTO.setDeleted(true);
        User user = this.userRepository.save(
            this.userMapper.toEntity(userDTO));
        return this.userMapper.toDto(user);
    }

    public int generateUniqueNumber() {
        long timestamp = Instant.now().toEpochMilli();
        int uniqueNumber = (int) (timestamp % 10000000);
        if (uniqueNumber < 1000000) {
            uniqueNumber += 1000000;
        }
        return uniqueNumber;
    }


    @Override
    public void sendVerificationEmail(String userId, String username, String userEmail,
        String password) {
        log.debug("Start searching for validity token");
        String validityToken = this.jwtTokenUtil.generateToken(UUID.fromString(userId), validityDuration,
            username);
        String activationLink = this.frontUrl + "/auth/activation-mail?token=" + validityToken;
        if (userEmail != null) {
            this.notificationService.sendEmailVerification(userEmail, username, activationLink,
                password);
        }
        log.debug("Validity token: {}", validityToken);
    }

    @Override
    public String getEmailById(UUID id) {
        return this.userRepository.getEmailById(id).orElse(null);
    }


    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        return credential;
    }


    @Override
    public List<UserDTO> getAllUsers() {
        return this.userMapper.toDto(this.userRepository.findAllByDeletedFalse());
    }

    @Override
    public List<UserDTO> findByIds(List<UUID> ids) {
        try {
            List<User> users = this.userRepository.findByIdIn(ids);
            if (!users.isEmpty()) {
                return this.userMapper.toDto(users);
            } else {
                throw new IllegalArgumentException("No users found with the provided IDs.");
            }

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Unable to retrieve users by IDs", e);
        }
    }

    @Override
    public String isProfileCompleted(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(null);
        return String.valueOf(user.getProfileCompleted());
    }

    @Override
    public void resendEmailVerification(String token) {
        log.debug("Start searching for validity token");
        UserDTO userDTO = this.keycloakAuthService.getUserByToken(token);
        String validityToken = this.jwtTokenUtil.generateToken(userDTO.getId(), validityDuration,
            userDTO.getUsername());
        String activationLink = this.frontUrl + "/auth/activation-mail?token=" + validityToken;
        this.notificationService.sendEmailVerification(userDTO.getEmail(),
            userDTO.getUsername(), activationLink, userDTO.getPassword());

        log.debug("Validity token: {}", validityToken);
    }

    @Override
    public Page<UserDTO> getUsersByGroupId(UUID id, Pageable pageable) {
        Page<User> Users = this.userRepository.findUsersByGroupId(id,
            pageable);
        return Users.map(this.userMapper::toDto);
    }

    public void syncAdminUser() {
        addAdminUserToKeycloack();
    }

    @Override
    public void syncDefaultUsers() {
        addDefaultUsersToKeycloack();
//        deleteUsersFromKeycloakIfDeletedInDb();
    }

    private void addAdminUserToKeycloack() {
        Optional<User> optionalAdminUser = this.userRepository.findByUsername("BS_ADMIN");

        if (optionalAdminUser.isEmpty()) {
            throw new IllegalArgumentException("user admin not found in database");
        }

        User adminUser = optionalAdminUser.get();

        if (!isUserExist(adminUser)) {
            Response response = responseCreateUserInKeycloack(adminUser);

            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath()
                    .replaceAll(".*/([^/]+)$", "$1");
                log.info("User created: {} ", userId);
                assignRoleToUser(this.keycloak, userId, "BS_ADMIN");

                adminUser.setKeycloakId(UUID.fromString(userId));
                this.userRepository.save(adminUser);
            } else if (response.getStatus() == 409) {
                log.warn("Admin user already exists in Keycloak (409 Conflict)");
            } else {
                log.error("Failed to create user: {}", response.getStatus());
                throw new IllegalArgumentException("failed to create user admin in keycloack");
            }
        } else {
            log.info("user admin exists in Keycloak, nothing to do");
        }
    }


    private void addDefaultUsersToKeycloack() {
        int startUserName = 1111111;
        int endUsername =   1199999;
        Role defaultRole = this.roleRepository.findByLabel(Constants.DEFAULT_ROLE).orElseThrow(null);
        Group defaultGroup = this.groupRepository.findFirstByLabel("GLOBAL_GROUP");
        Set<Role> roleToAdd = new HashSet<>();
        roleToAdd.add(defaultRole);
        Set<Group> groupToAdd = new HashSet<>();
        groupToAdd.add(defaultGroup);
        List<User> usersListToAddToKeycloack = this.userRepository.findAllByDeletedFalse();
        if (!usersListToAddToKeycloack.isEmpty()) {
            usersListToAddToKeycloack.forEach(user -> {
                if (user.getUsername().startsWith("BS") || user.getUsername().startsWith("int-")) {
                    log.info("Skipping user with username starting with 'BS' or 'ext': {}",
                        user.getUsername());
                    return;
                }
                int username = extractAndConvertUsernameToInt(user.getUsername());
                if (username >= startUserName && username < endUsername) {
                    if (!isUserExist(user)) {
                        System.out.println(user.getUsername());
                        Response response = responseCreateUserInKeycloack(user);
                        if (response.getStatus() == 201) {
                            String userId = response.getLocation().getPath()
                                .replaceAll(".*/([^/]+)$", "$1");
                            log.info("User created: {} ", userId);
                            roleToAdd.forEach(el -> {
                                Set<Authority> authList = el.getAuthorities();
                                authList.forEach(auth -> {
                                    assignRoleToUser(this.keycloak, userId, auth.getLabel());
                                });
                            });
                            user.setKeycloakId(UUID.fromString(userId));
                            user.addRoles(roleToAdd);
                            user.setGroups(groupToAdd);
                            this.userRepository.save(user);
                        }  else if (response.getStatus() == 409) {
                            log.warn("user {} already exists in Keycloak (409 Conflict)", user.getUsername());
                        } else {
                            log.error("Failed to create user: {}", response.getStatus());
                            throw new IllegalArgumentException(
                                "failed to create user in keycloack");
                        }
                    } else {
                        syncUserWithKeycloak(user, groupToAdd);
                        log.info("user exist nothing to do ");
                    }
                    user.getRoles().forEach(el -> {
                        Set<Authority> authList = el.getAuthorities();
                        authList.forEach(auth -> {
                            assignRoleToUser(this.keycloak, user.getKeycloakId().toString(), auth.getLabel());
                        });
                    });

                }

            });
        }
    }

    private int extractAndConvertUsernameToInt(String username) {
        if (username != null && username.startsWith("ext-")) {
            try {
                String numberStr = username.substring(4);
                return Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                log.error("Failed to convert username to int: {}", username, e);
                throw new IllegalArgumentException(
                    "Username does not contain a valid integer after 'int-'");
            }
        } else if (username != null && username.startsWith("pm-ext-")) {
            try {
                String numberStr = username.substring(7);
                return Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                log.error("Failed to convert username to int: {}", username, e);
                throw new IllegalArgumentException(
                    "Username does not contain a valid integer after 'int-'");
            }
        } else {
            throw new IllegalArgumentException("Username must start with 'int-' or 'pm-ext-' ");
        }
    }

    private boolean isUserExist(User adminUser) {
        try {
            UsersResource usersResource = this.keycloak.realm(this.realm).users();
            UserResource userResource = usersResource.get(
                String.valueOf(adminUser.getKeycloakId()));
            userResource.toRepresentation();
            return true;
        } catch (NotFoundException ex) {
            return false;
        }
    }

    private void deleteUsersFromKeycloakIfDeletedInDb() {
        List<User> deletedUsers = this.userRepository.findAllByDeletedTrue();

        if (deletedUsers.isEmpty()) {
            log.info("No users with deleted = true found in the database.");
            return;
        }

        UsersResource usersResource = this.keycloak.realm(this.realm).users();

        deletedUsers.forEach(user -> {
            try {
                if (user.getKeycloakId() != null) {
                    String keycloakId = user.getKeycloakId().toString();
                    UserResource userResource = usersResource.get(keycloakId);

                    UserRepresentation userRep = userResource.toRepresentation();
                    log.info("Found user in Keycloak with ID: {} and username: {}", keycloakId, userRep.getUsername());

                    usersResource.delete(keycloakId);
                    log.info("Successfully deleted user from Keycloak with ID: {}", keycloakId);

                    user.setKeycloakId(null);
                    this.userRepository.save(user);
                    log.info("Updated local user record for username: {}", user.getUsername());
                } else {
                    log.warn("User with username: {} has no Keycloak ID, skipping deletion.", user.getUsername());
                }
            } catch (NotFoundException ex) {
                log.warn("User with Keycloak ID: {} not found in Keycloak, skipping deletion.", user.getKeycloakId());
                user.setKeycloakId(null);
                this.userRepository.save(user);
            } catch (Exception ex) {
                log.error("Failed to delete user with Keycloak ID: {} from Keycloak.", user.getKeycloakId(), ex);
            }
        });
    }
    private Response responseCreateUserInKeycloack(User adminUser) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(adminUser.getEmail());
        user.setUsername(adminUser.getUsername());
        user.setFirstName(adminUser.getFirstName());
        user.setLastName(adminUser.getLastName());
        user.setEmailVerified(true);
        String password = "pass";
        user.setCredentials(Collections.singletonList(createPasswordCredential(password)));
        this.keycloakAuthService.addCustomAttributeToUser(user, "user_id",
            String.valueOf(adminUser.getId()));
        this.keycloakAuthService.addCustomAttributeToUser(user, "profile_completed",
            String.valueOf(adminUser.getProfileCompleted()));
        this.keycloakAuthService.addCustomAttributeToUser(user, "first_connection",
            String.valueOf(true));
        return this.keycloak.realm(this.realm).users().create(user);
    }

    private void assignRoleToUser(Keycloak keycloak, String userId, String roleName) {
        String clientId = getClientId();
        RoleRepresentation role = keycloak.realm(this.realm).clients().get(clientId).roles()
            .get(roleName)
            .toRepresentation();
        keycloak.realm(this.realm).users().get(userId).roles().clientLevel(clientId)
            .add(Collections.singletonList(role));
        log.info("Assigned role with name : {} , to user : {}", roleName, userId);
    }
    public void assignEctdRoleToIndustryAccount(){
        List<User> listIndustryUsersAndTheirDesignatedMembers = this.userRepository.findAllIndustryAndDesignatedUsersWithBsIndustrielRole(Constants.BS_ROLE_PM_INDUSTRIEL);
        Role ectdRole = this.roleRepository.findByLabelAndDeletedFalse(Constants.BS_ROLE_ECTD);
        if(listIndustryUsersAndTheirDesignatedMembers.isEmpty()){
            log.info(" No users found with role : {} ", Constants.BS_ROLE_PM_INDUSTRIEL);
            return ;
        }
        if (ectdRole == null) {
            throw new IllegalStateException("ECTD role not found or marked as deleted.");
        }
        listIndustryUsersAndTheirDesignatedMembers.forEach(user -> {
            if (!user.getRoles().contains(ectdRole)) {
                user.addRole(ectdRole);
                assignRoleToUser(this.keycloak, user.getKeycloakId().toString(), Constants.ECTD_AUTHORITY);
                log.info("ECTD role added successfully to user with username: {}", user.getUsername());
            } else {
                log.info("User with username: {} already has ECTD role", user.getUsername());
            }
        });
        userRepository.saveAll(listIndustryUsersAndTheirDesignatedMembers);
    }
    @Override
    public List<UUID> getGroupIdsByUserId(UUID userId) {
        return this.userRepository.getGroupIdsByUserId(userId);
    }

    @Override
    public List<UUID> getRoleIdsByUserId(UUID userId) {
        return this.userRepository.getRoleIdsByUserId(userId);
    }

    @Override
    public UUID getRootId() {
        User userRoot = this.userRepository.findByUsername(Constants.BS_ROOT).orElse(null);
        return userRoot.getId();
    }

    @Override
    public void removeRoleAssociation(UUID roleId) {
        List<User> roleUsers = this.userRepository.findAllUsersByRoleId(roleId);
        if (!roleUsers.isEmpty()) {
            roleUsers.stream()
                .map(user -> this.removeRoleFromUser(user, roleId))
                .forEach(this.userRepository::save);
        }
    }

    @Override
    public void deleteUserGroupAssociation(User user, Set<Group> groups) {
        Set<Group> userGroups = user.getGroups();
        userGroups.removeAll(groups);
        user.setGroups(userGroups);
        if (user.getGroups().isEmpty()) {
            user.setDeleted(true);
            user.setRoles(null);
            this.removeKeycloakAttributes(user.getKeycloakId(), "groups");
            this.removeKeycloakAttributes(user.getKeycloakId(), "roles");
            this.updateUserAuthorityInKeycloak(this.keycloak, user.getKeycloakId(), user.getId());
        }
        this.userRepository.save(user);
    }

    private User removeRoleFromUser(User user, UUID roleId) {
        user.getRoles().removeIf(role -> role.getId() == roleId);
        this.updateUserAuthorityInKeycloak(this.keycloak, user.getKeycloakId(), user.getId());
        this.updateKeycloakAttributes(user.getKeycloakId(),
            user.getRoles().stream().map(Role::getLabel).toList(), "roles");
        return user;
    }

    private void updateKeycloakAttributes(UUID userId, List<String> labels, String key) {
        UsersResource usersResource = this.keycloak.realm(this.realm).users();
        UserResource user = usersResource.get(String.valueOf(userId));
        UserRepresentation userRepresentation = user.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        attributes.remove(key);
        attributes.put(key, labels);
        user.update(userRepresentation);
    }

    private void removeKeycloakAttributes(UUID userId, String key) {
        UsersResource usersResource = this.keycloak.realm(this.realm).users();
        UserResource user = usersResource.get(String.valueOf(userId));
        UserRepresentation userRepresentation = user.toRepresentation();
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        attributes.remove(key);
        userRepresentation.setAttributes(attributes);
        user.update(userRepresentation);
    }

    public void updateUserAttribute(String userId, String attributeName, String attributeValue) {
        log.debug("Updating user {} attribute: {}", userId, attributeName);
        String keycloakUserId = userId;
        try {
            Optional<User> userEntity = userRepository.findUserById(UUID.fromString(userId));

            if (userEntity.isPresent()) {
                User user = userEntity.get();
                if (!user.getId().equals(user.getKeycloakId())) {
                    keycloakUserId = user.getKeycloakId().toString();
                    log.debug("User found in database, using Keycloak ID: {}", keycloakUserId);
                }
            } else {
                log.info("User not found in database, using provided ID for Keycloak: {}", userId);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid UUID format for userId: {}, using as-is for Keycloak", userId);
        }

        try {
            UserRepresentation kcUser = keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .toRepresentation();

            Map<String, List<String>> attributes = kcUser.getAttributes();
            if (attributes == null) {
                attributes = new HashMap<>();
            }
            attributes.put(attributeName, Collections.singletonList(attributeValue));
            kcUser.setAttributes(attributes);

            keycloak.realm(realm).users().get(keycloakUserId).update(kcUser);

            log.info("Successfully updated attribute {} for user {}", attributeName, keycloakUserId);

        } catch (Exception e) {
            log.error("Error updating attribute for user {} in Keycloak", keycloakUserId, e);
            throw new RuntimeException("Failed to update user attribute in Keycloak", e);
        }
    }
    @Override
    public UserDTO updateAndCompletedProfile(UpdateProfileRequest updateProfileRequest)  {
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();
            UUID userUuid = UUID.fromString(userId);
            List<String> usernames = userRepository.findUsernameByNationalId(updateProfileRequest.getUserDTO().getNationalId());
            boolean isInternationalUser = usernames.stream()
                .anyMatch(u -> u.toLowerCase().startsWith("int"));
            log.debug("Request to complete User : {}", updateProfileRequest.getUserDTO());
            updateUserAttribute(String.valueOf(userUuid), "profile_completed", "true");
            log.debug("Request to update User : {}", updateProfileRequest.getUserDTO());
            Map<String, String> processedData = parseProfileData(updateProfileRequest.getProfileDTO());

            User user = this.userRepository.findById(updateProfileRequest.getUserDTO().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (!isInternationalUser) {
               this.userValidator.beforeUpdate(updateProfileRequest.getUserDTO());
            }

            UsersResource usersResource = this.keycloak.realm(this.realm).users();
            UserResource userResource = usersResource.get(
                String.valueOf(updateProfileRequest.getUserDTO().getKeycloakId()));
            UserRepresentation userRepresentation = userResource.toRepresentation();

            userRepresentation.setEmail(updateProfileRequest.getUserDTO().getEmail());
            userRepresentation.setFirstName(updateProfileRequest.getUserDTO().getFirstName());
            userRepresentation.setLastName(updateProfileRequest.getUserDTO().getLastName());

            if (updateProfileRequest.getUserDTO().getPassword() != null) {
                userRepresentation.setCredentials(
                    Collections.singletonList(createPasswordCredential(updateProfileRequest.getUserDTO().getPassword())));
            }

            userResource.update(userRepresentation);
            updateProfileRequest.getUserDTO().getProfile().putAll(processedData);
            this.userRepository.save(this.userMapper.toEntity(updateProfileRequest.getUserDTO()));
            updateUserAuthorityInKeycloak(this.keycloak, user.getKeycloakId(), user.getId());

            return this.userMapper.toDto(user);
        } catch (EntityNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error updating user profile: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user profile", e);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<UserDTO> saveAndCompleteMyProfile(UUID userId, ProfileDTO profileDTO) {
        log.debug("save and complete profile for user {} and profile data : {}", userId, profileDTO);

        Map<String, String> processedData = parseProfileData(profileDTO);

        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.getProfile().putAll(processedData);
        user.setProfileCompleted(true);

        User updatedUser = this.userRepository.save(user);
        UserDTO userDTO = this.userMapper.toDto(updatedUser);

        log.debug("Update the profile_completed attribute in keycloak {} ", userId);
        updateUserAttribute(String.valueOf(userId), "profile_completed", "true");

        return ResponseEntity.ok(userDTO);
    }
    public ResponseEntity<UserDTO> saveDataPm(String keycloakId, ProfileDTO profileDTO) {
        log.debug("Saving Data PM for user {} with value: {}", keycloakId, profileDTO);

        if (keycloakId == null || keycloakId.isEmpty()) {
            log.error("PmKeycloakId is null or empty in delegateExecution");
            throw new RuntimeException("PmKeycloakId is not provided in delegateExecution");
        }

        Map<String, String> profileData = profileDTO.getProfileData();

        if (profileData != null && !profileData.isEmpty()) {
            profileData.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    updateUserAttribute(keycloakId, key, value);
                }
            });

            log.debug("Updated {} attributes in Keycloak for user {}", profileData.size(), keycloakId);
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setKeycloakId(UUID.fromString(keycloakId));
        userDTO.setProfile(profileData != null ? profileData : new HashMap<>());

        return ResponseEntity.ok(userDTO);
    }

    private Map<String, String> parseProfileData(ProfileDTO profileDTO) {
        if (profileDTO == null || profileDTO.getProfileData() == null || profileDTO.getProfileData().isEmpty()) {
            throw new IllegalArgumentException("Profile data is empty or null.");
        }

        Map<String, String> raw = profileDTO.getProfileData();
        Map<String, String> cleaned = new HashMap<>();

        for (Map.Entry<String, String> entry : raw.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key == null || value == null || value.trim().isEmpty()
                || key.equalsIgnoreCase("starter") || key.toLowerCase().startsWith("customdatagrid")) {
                continue;
            }

            cleaned.put(key.trim(), value.trim());
        }

        return cleaned;
    }


    @Override
    public void updateKeycloakUserDesignations(AssignRoleApplication assignRoleApplication) {
        UUID keycloakId = assignRoleApplication.getKeycloakId();
        String ectdRole = assignRoleApplication.getEctdRole();
        List<String> applications = assignRoleApplication.getApplicationsId();
        try {
            log.info("Starting Keycloak user update for user ID: {}", keycloakId);
            UsersResource usersResource = this.keycloak.realm(this.realm).users();
            log.debug("Retrieved UsersResource for realm: {}", this.realm);

            UserResource userResource = usersResource.get(String.valueOf(keycloakId));
            UserRepresentation userRepresentation = userResource.toRepresentation();

            if (userRepresentation == null) {
                log.error("User representation not found for ID: {}", keycloakId);
                throw new RuntimeException("User with ID " + keycloakId + " not found in Keycloak");
            }

            log.debug("Retrieved user representation for ID: {}", keycloakId);
            if (ectdRole != null && !ectdRole.equals("")) {
                String roleId = ectdRole;

                log.info("Updating role_ectd with single role '{}' for user ID: {}", roleId, keycloakId);
                this.keycloakAuthService.addOrUpdateCustomAttributeToUser(userRepresentation, "role_ectd", roleId);
            }


            if (applications != null && !applications.isEmpty()) {
                String applicationsIdList = String.join(",", applications);

                log.info("Updating applications_Id with {} applications for user ID: {}",
                    applications.size(), keycloakId);
                log.debug("applications_Id being set: {}", applicationsIdList);

                this.keycloakAuthService.addOrUpdateCustomAttributeToUser(userRepresentation, "applications_Id", applicationsIdList);
            }

            userResource.update(userRepresentation);
            log.info("Successfully updated Keycloak user with ID: {}", keycloakId);

        } catch (Exception e) {
            log.error("Failed to update Keycloak user with ID: {}. Error: {}",
                keycloakId, e.getMessage(), e);
            throw new RuntimeException("Failed to update Keycloak user: " + e.getMessage(), e);
        }
    }

    @Override
    public void reassignUserToNewRoles(User usersToReassign) {

        if (usersToReassign != null) {
            this.updateUserAuthorityInKeycloak(this.keycloak, usersToReassign.getKeycloakId(),
                usersToReassign.getId());
            this.updateKeycloakAttributes(usersToReassign.getKeycloakId(),
                usersToReassign.getRoles().stream().map(Role::getLabel).toList(), "roles");
        }

    }

    @Override
    public User findByEmail(String email) {
        log.debug("Request to find User by email : {}", email);
        User user = this.userRepository.findByEmail(email)
            .orElseThrow(null);
        return user;
    }
    public CheckEligibilityResponse checkUsernameEligibility(String username, String roleLabel) {
        log.info("Checking eligibility for username: {}", username);
        CheckEligibilityResponse checkUsernameEligibility = new CheckEligibilityResponse();
        HttpHeaders headers = new HttpHeaders();
        Boolean status = true;
        if (username == null || username.trim().isEmpty()) {
            log.warn("Invalid username provided");
            headers.add("X-Warning", "INVALID_USERNAME");
            status =status && false;
        }
         if (username.toLowerCase().contains("int") ||username.toLowerCase().startsWith("INT") ) {
            log.error("User '{}' does not have type INT.", username);
            headers.add("X-Warning", "INVALID_TYPE");
            status =status && false;
        }

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            log.warn("User not found with username: {}", username);
            headers.add("X-Warning", "USER_NOT_FOUND");
            status =status && false;

        }

        User user = userOpt.get();
        UserType userType = user.getUserType();

        if (userType == null) {
            log.error("User '{}' does not have a user type defined.", username);
            headers.add("X-Warning", "NO_USER_TYPE");
            status =status && false;
        }
        if (user.getIsActive() != true) {
            log.error("User '{}' does not have an active account.", username);
            headers.add("X-Warning", "INACTIVE_ACCOUNT");
            status = status && false;
        }

        if (userType.equals(UserType.PHYSICAL)) {
            log.debug("User '{}' is of type PHYSICAL. Checking physical user eligibility.", username);
            status =status && checkPhysicalUser(user, headers);
        } else if (userType.equals(UserType.COMPANY)) {
            log.debug("User '{}' is of type COMPANY. Checking company user eligibility with role '{}'.", username, roleLabel);
            status =status && checkCompanyUser(user, roleLabel, headers);
        } else {
            log.error("User '{}' has unsupported user type: {}", username, userType);
            headers.add("X-Warning", "UNSUPPORTED_USER_TYPE");
            status =status && false;
        }
        checkUsernameEligibility.setStatus(status);
        checkUsernameEligibility.setHeaders(headers);
        return checkUsernameEligibility ;
    }

    private boolean checkPhysicalUser(User user, HttpHeaders headers) {
        String username = user.getUsername();
        boolean isCompleted = userRepository.existsByUsernameAndIsProfileCompleted(username, true);
        log.debug("Physical user '{}' profile completion status: {}", username, isCompleted);
        if (!isCompleted) {
            headers.add("X-Warning", "PROFILE_INCOMPLETE");
            return false;
        }
        return true;
    }

    private boolean checkCompanyUser(User user, String roleLabel, HttpHeaders headers) {
        String username = user.getUsername();
        boolean isProfileCompleted = userRepository.existsByUsernameAndIsProfileCompleted(username, null);

        if (roleLabel == null || roleLabel.trim().isEmpty()) {
            log.warn("Invalid role label provided for user '{}'", username);
            headers.add("X-Warning", "INVALID_ROLE_LABEL");
            return false;
        }

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            log.debug("Company user '{}' has no roles assigned.", username);
            headers.add("X-Warning", "NO_ROLES_ASSIGNED");
            return false;
        }

        boolean hasRequiredRole = roles.stream()
            .filter(role -> role != null && role.getLabel() != null)
            .anyMatch(role -> roleLabel.equals(role.getLabel()));

        if (!hasRequiredRole) {
            log.debug("Company user '{}' does not have required role '{}'.", username, roleLabel);
            headers.add("X-Warning", "MISSING_ROLE");
            return false;
        }

        log.debug("Company user '{}' has required role '{}'.", username, roleLabel);
        return true;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.debug("Request to save User : {}", userDTO);
        User user = this.userMapper.toEntity(userDTO);
        user = this.userRepository.save(user);
        return this.userMapper.toDto(user);
    }
    @Override
    public boolean doesProfileKeyExist(UUID userId, String key) {
        log.debug("Checking if profile key '{}' exists for user with ID: {}", key, userId);
        return userRepository.existsProfileKey(userId, key);
    }

    @Override
    public Optional<String> getProfileValue(UUID userId, String key) {
        log.debug("Fetching profile value for key '{}' and user ID: {}", key, userId);
        return userRepository.findProfileValue(userId, key);
    }

    @Override
    public boolean existsUserByRole(UUID userId, UUID roleId) {
        log.debug("Checking if user with ID '{}' has role ID: {}", userId, roleId);
        return userRepository.existsUserByRole(userId, roleId);
    }

    @Override
    public boolean existPhoneNumber(String phoneNumber) {
        log.debug("Checking if phone Number exists : {}", phoneNumber );
        boolean exist = false;
        User userWithPhoneNumberExist = this.userRepository.findByPhoneNumberAndDeletedFalse(
            phoneNumber);
        if (userWithPhoneNumberExist != null) {
           exist = true;
              }
        return exist;
    }
    @Override
    public List<UserDenominationDTO> getUsernamesAndDenominationsByRole(String roleLabel) {
        log.debug("Request to get usernames and denominations of users with role: {}", roleLabel);
        List<User> users = userRepository.findUsersByRole(roleLabel);
        return users.stream()
            .map(user -> new UserDenominationDTO(user.getUsername(), user.getDenomination()))
            .collect(Collectors.toList());
    }
    @Override
    public String getUsernameById(UUID id) {
        return userRepository.findById(id)
            .map(User::getUsername)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public void fixKeycloakIdsByUsername() {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<User> users = userRepository.findAll();

        users.forEach(user -> {
            try {
                List<UserRepresentation> results = usersResource.search(user.getUsername(), 0, 1);

                if (!results.isEmpty()) {
                    String realKeycloakId = results.get(0).getId();

                    if (!realKeycloakId.equals(String.valueOf(user.getKeycloakId()))) {
                        user.setKeycloakId(UUID.fromString(realKeycloakId));
                        userRepository.save(user);
                        log.info("Updated KeycloakId for username={} -> {}",
                            user.getUsername(), realKeycloakId);
                    }
                } else {
                    log.warn("No Keycloak user found for username={}", user.getUsername());
                }
            } catch (Exception e) {
                log.error("Error fixing KeycloakId for username={}", user.getUsername(), e);
            }
        });
    }

    @Override
    public Optional<UserDTO> findByKeycloakId(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId);
        return Optional.ofNullable(userMapper.toDto(user));
    }


    public void updateProfileCompletedKeycloak() {
        List<User> users = this.userRepository.findAll();
        users.stream()
            .filter(user -> {
                String username = user.getUsername();
                return username != null
                    && username.compareTo("ext-1111111") >= 0
                    && username.compareTo("ext-1111329") <= 0;
            })
            .forEach(user -> updateUserAttribute(
                String.valueOf(user.getKeycloakId()),
                "profile_completed",
                "false"
            ));
    }
    public List<UserDTO> getUserByNationalId(String nationalId) {
        List<User> users = userRepository.findAllByNationalIdAndDeletedFalse(nationalId);
        return users.stream()
            .map(user -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setAddress(user.getAddress());
                userDTO.setEmail(user.getEmail());
                userDTO.setPhoneNumber(user.getPhoneNumber());
                userDTO.setGender(user.getGender());
                userDTO.setBirthDate(user.getBirthDate());
                return userDTO;
            })
            .collect(Collectors.toList());

    }
    public boolean checkUsernameStartsWithInt(String nationalId) {
        List<User> users = userRepository.findAllByNationalIdAndDeletedFalse(nationalId);
        return !users.isEmpty() &&
            users.get(0).getUsername().startsWith("int-");
    }

    public boolean checkUsernameMatches(String nationalId, String username) {
        List<User> users = userRepository.findAllByNationalIdAndDeletedFalse(nationalId);
        return !users.isEmpty() &&
            users.get(0).getUsername().equals(username);
    }
    public void syncUserWithKeycloak(User user, Set<Group> groupToAdd) {
        try {
            List<UserRepresentation> keycloakUsers = this.keycloak.realm(this.realm)
                .users()
                .search(user.getUsername(), true);

            if (keycloakUsers.isEmpty()) {
                log.warn("User {} not found in Keycloak during sync", user.getUsername());
                return;
            }

            UserRepresentation keycloakUser = keycloakUsers.get(0);
            performBidirectionalSync(user, keycloakUser, groupToAdd);

        } catch (Exception e) {
            log.error("Error syncing user {} with Keycloak: {}",
                user.getUsername(), e.getMessage(), e);
        }
    }

    private void performBidirectionalSync(User user, UserRepresentation keycloakUser, Set<Group> groupToAdd) {
        boolean needsLocalUpdate = false;
        boolean needsKeycloakUpdate = false;
        String keycloakUserId = keycloakUser.getId();

        // Sync Keycloak ID → DB
        if (user.getKeycloakId() == null || !user.getKeycloakId().toString().equals(keycloakUserId)) {
            log.info("Syncing Keycloak ID for user {}: {} -> {}",
                user.getUsername(), user.getKeycloakId(), keycloakUserId);
            user.setKeycloakId(UUID.fromString(keycloakUserId));
            needsLocalUpdate = true;
        }

        // Sync DB user ID → Keycloak attribute
        Map<String, List<String>> attrs = keycloakUser.getAttributes();
        String keycloakStoredUserId = attrs != null && attrs.containsKey("user_id")
            ? attrs.get("user_id").get(0)
            : null;
        String dbUserId = user.getId().toString();

        if (!dbUserId.equals(keycloakStoredUserId)) {
            log.info("User ID mismatch for {}: updating Keycloak with '{}'",
                user.getUsername(), dbUserId);
            if (attrs == null) {
                attrs = new HashMap<>();
                keycloakUser.setAttributes(attrs);
            }
            attrs.put("user_id", Collections.singletonList(dbUserId));
            needsKeycloakUpdate = true;
        }

        // ADD GLOBAL GROUP — now for ALL users if they have no groups
        if (user.getGroups() == null || user.getGroups().isEmpty()) {
            log.info("Adding default groups to user {}", user.getUsername());
            groupToAdd.forEach(user::addGroup);
            needsLocalUpdate = true;
        }

        if (needsLocalUpdate) {
            this.userRepository.save(user);
        }

        if (needsKeycloakUpdate) {
            this.keycloak.realm(this.realm)
                .users()
                .get(keycloakUserId)
                .update(keycloakUser);
        }
    }
   @Override
   @Cacheable(cacheNames = CACHE_NAME)
    public List<UserRepresentation> getAllUsersFromKeycloak() {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        return usersResource.list();
    }

    public void removeEctdCompletely(UserDTO user) {
        log.info("Removing ECTD role & attributes for user {}", user.getId());

        List<String> attributes = List.of("applicant", "submitter", "applicationsId", "role_ectd");
        for (String attr : attributes) {
            this.removeKeycloakAttributes(user.getKeycloakId(), attr);
        }

        Optional<RoleDTO> ectdRoleOpt = user.getRoles().stream()
            .filter(r -> Constants.BS_ROLE_ECTD.equals(r.getLabel()))
            .findFirst();

        if (ectdRoleOpt.isPresent()) {
            UUID ectdRoleId = ectdRoleOpt.get().getId();

            User userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + user.getId()));

            this.removeRoleFromUser(userEntity, ectdRoleId);
        } else {
            log.info("User {} does not have ECTD role", user.getId());
        }

        log.info("ECTD role and attributes successfully removed for user {}", user.getId());
    }

    public void updateEctdUserAttributes(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<DesignationsList> activeDesignations = designationsListRepository
            .findByDesignatedUserIdAndDeletedFalse(user.getId());

        //  applicant
        String applicant = activeDesignations.stream()
            .map(DesignationsList::getLaboratoryUser)
            .filter(Objects::nonNull)
            .filter(lab -> lab.getUsername() != null && lab.getDenomination() != null)
            .map(lab -> lab.getUsername() + ":" + lab.getDenomination())
            .distinct()
            .collect(Collectors.joining(","));

        //  submitter
        String submitter = activeDesignations.stream()
            .map(DesignationsList::getPmUser)
            .filter(Objects::nonNull)
            .filter(pm -> pm.getUsername() != null && pm.getDenomination() != null)
            .map(pm -> pm.getUsername() + ":" + pm.getDenomination())
            .distinct()
            .collect(Collectors.joining(","));
        // Update Keycloak attributes
        String keycloakId = user.getKeycloakId().toString();
        updateUserAttribute(keycloakId, "applicant", applicant);
        updateUserAttribute(keycloakId, "submitter", submitter);

        log.info("Updated ECTD attributes for {}: applicant=[{}], submitter=[{}]",
            username, applicant, submitter);
    }
}
