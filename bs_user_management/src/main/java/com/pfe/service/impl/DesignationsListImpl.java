package com.pfe.service.impl;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.pfe.config.Constants;
import com.pfe.repository.UserRepository;
import com.pfe.service.KeycloakAuthService;
import com.pfe.service.RoleService;
import com.pfe.service.UserService;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.dto.request.ApplicantListUserResponse;
import com.pfe.service.dto.request.ApplicationUsersResponse;
import com.pfe.domain.DesignationsList;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.domain.enumeration.UserType;
import com.pfe.feignService.ApplicationConnector;
import com.pfe.repository.DesignationsListRepository;
import com.pfe.service.DesignationsListService;
import com.pfe.service.dto.DesignationsListDTO;
import com.pfe.service.event.DesignationDeletedEvent;
import com.pfe.service.mapper.DesignationsListMapper;
import com.pfe.service.mapper.RoleMapper;
import com.pfe.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DesignationsListImpl implements DesignationsListService {

  private static final Logger log = LoggerFactory.getLogger(DesignationsListImpl.class);

  private final DesignationsListMapper designationsListMapper;
  private final DesignationsListRepository designationsListRepository;
  private final UserService userService;
  private final RoleService roleService;
  private final RoleMapper roleMapper;
  private final UserMapper userMapper;
  private final ApplicationConnector applicationConnector;
  private final KeycloakAuthService keycloakAuthService;
  private final Keycloak keycloak;
  private final UserRepository userRepository;
  @Value("${bs-app.keycloak.realm}")
  private String realm;
    private static final String CACHE_NAME = "applicantCache";

    private static final String PERFIX_USERNAME="ext-";
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

  public DesignationsListImpl(DesignationsListMapper designationsListMapper,
      DesignationsListRepository designationsListRepository, UserMapper userMapper,
      ApplicationConnector applicationConnector, @Lazy UserService userService,
      @Lazy RoleService roleService, RoleMapper roleMapper, KeycloakAuthService keycloakAuthService,
      Keycloak keycloak, UserRepository userRepository) {
    this.designationsListMapper = designationsListMapper;
    this.designationsListRepository = designationsListRepository;
    this.applicationConnector = applicationConnector;
    this.userService = userService;
    this.roleService = roleService;
    this.roleMapper = roleMapper;
    this.userMapper = userMapper;
    this.keycloakAuthService = keycloakAuthService;
    this.keycloak = keycloak;
    this.userRepository = userRepository;

  }

  @Override
  public DesignationsListDTO save(DesignationsListDTO dto) {
    log.debug("Attempting to save DesignationsListDTO: {}", dto);

    if (dto.getDesignatedUser() == null || dto.getRole() == null) {
      log.error("Invalid input: pmUserId, designatedUserId, or roleId is null in DTO: {}", dto);
      throw new IllegalArgumentException("pmUserId, designatedUserId, and roleId must not be null");
    }
    User pmUser = new User();
    if (dto.getPmUser() != null) {
      log.debug("Fetching PM User with ID: {}", dto.getPmUser());
      Optional<UserDTO> userDTO = this.userService.findOne(dto.getPmUser().getId());
      if (userDTO.isEmpty()) {
        log.error("PM User not found with ID: {}", dto.getPmUser());
        throw new EntityNotFoundException("PM User not found with ID: " + dto.getPmUser());
      }
      pmUser = this.userMapper.toEntity(userDTO.get());
      if (!pmUser.getUserType().equals(UserType.COMPANY)) {
        log.error("PM User must be of type COMPANY, found: {}", pmUser.getUserType());
        throw new IllegalArgumentException("PM User must be of type COMPANY");
      }
    }

    log.debug("Fetching Designated User with ID: {}", dto.getDesignatedUser());
    Optional<UserDTO> designatedUserDto = this.userService.findOne(dto.getDesignatedUser().getId());
    if (designatedUserDto.isEmpty()) {
      log.error("Designated User not found with ID: {}", dto.getDesignatedUser());
      throw new EntityNotFoundException(
          "Designated User not found with ID: " + dto.getDesignatedUser());
    }
    User designatedUser = this.userMapper.toEntity(designatedUserDto.get());
    if (!designatedUser.getUserType().equals(UserType.PHYSICAL)) {
      log.error("Designated User must be of type PHYSICAL, found: {}",
          designatedUser.getUserType());
      throw new IllegalArgumentException("Designated User must be of type PHYSICAL");
    }
    log.debug("Fetching Role with ID: {}", dto.getRole());
    RoleDTO role = roleService.findOne(dto.getRole().getId()).orElseThrow(() -> {
      log.error("Role not found with ID: {}", dto.getRole());
      return new EntityNotFoundException("Role not found with ID: " + dto.getRole());
    });
    Role roleEntity = this.roleMapper.toEntity(role);
    log.debug("Mapping DTO to entity and setting relationships");
    DesignationsList entity = designationsListMapper.toEntity(dto);
    entity.setPmUser(pmUser);
    entity.setDesignatedUser(designatedUser);
    entity.setRole(roleEntity);

    log.debug("Saving DesignationsList entity to repository");
    entity = designationsListRepository.save(entity);

    DesignationsListDTO savedDto = designationsListMapper.toDto(entity);
    log.info("Successfully saved DesignationsList with ID: {}", savedDto.getId());
    return savedDto;
  }


  @Override
  public List<DesignationsList> getListByPmUserId(UUID pmUserId, Pageable pageable) {
    log.debug("Fetching designations list for pmUserId: {} with page: {}, size: {}", pmUserId,
        pageable.getPageNumber(), pageable.getPageSize());

    Page<DesignationsList> page = designationsListRepository.findByPmUserId(pmUserId, pageable);

    log.info("Retrieved {} designations for pmUserId: {}", page.getTotalElements(), pmUserId);

    return page.getContent();
  }

  @Override
    public List<DesignationsList> getListByUserId(UUID userId) {
    log.debug("Fetching designations list for UserId: {} ", userId);

    List<DesignationsList> designationsLists = designationsListRepository.findByDesignatedUserId(userId);

    log.info("Retrieved {} designations for userId: {}", designationsLists, userId);

    return designationsLists;
  }


  public ResponseEntity<ApplicationUsersResponse> getApplicationNames() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (!(authentication instanceof BearerTokenAuthentication)) {
        log.error("Authentication is not BearerTokenAuthentication");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApplicationUsersResponse());
      }

      String token = ((BearerTokenAuthentication) authentication).getToken().getTokenValue();

      JWT jwt = JWTParser.parse(token);
      String keycloakUserId = jwt.getJWTClaimsSet().getSubject();

      UserRepresentation userRep = keycloak.realm(realm).users().get(keycloakUserId)
          .toRepresentation();
      String currentUsername = userRep.getUsername();

      RoleDTO roleEctdDTO = this.roleService.getRoleByLabel(Constants.BS_ROLE_ECTD);
      Role bsEctdRole = this.roleMapper.toEntity(roleEctdDTO);

      log.info("Fetched user for userId: {}, username: {}", token, currentUsername);

      if (bsEctdRole == null) {
        log.warn("User '{}' does not have required ECTD role", currentUsername);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApplicationUsersResponse());
      }

      List<UserRepresentation> allUsers = keycloak.realm(realm).users().list();
      for (UserRepresentation otherUser : allUsers) {

        Map<String, List<String>> attributes = otherUser.getAttributes();
        List<String> applicantList = attributes != null ? attributes.get("applicant") : null;
        boolean foundInAnyApplicantList = applicantList != null;

        if (!foundInAnyApplicantList) {
          log.info("User '{}' is not in any applicant list. Or in submitter.", currentUsername);
        }
        if (foundInAnyApplicantList && applicantList.contains(currentUsername)) {
          log.info("User '{}' is not in any applicant list. Acting as submitter.", currentUsername);
          ResponseEntity<List<String>> applicantResponse = applicationConnector.getApplications(
              currentUsername);
          if (applicantResponse.getStatusCode() != HttpStatus.OK
              || applicantResponse.getBody() == null) {
            log.error("Failed to fetch submitter applications for user '{}': invalid response",
                currentUsername);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApplicationUsersResponse());
          }
          List<String> names = applicantResponse.getBody();
          ApplicationUsersResponse response = new ApplicationUsersResponse();
          response.setNames(names);
          return ResponseEntity.ok(response);
        }

        if (foundInAnyApplicantList && !applicantList.contains(currentUsername)) {
          log.info("User '{}' is not in any applicant list. Acting as submitter.", currentUsername);
          ResponseEntity<List<String>> submitterResponse = applicationConnector.getApplicationsBySubmitter(
              currentUsername);
          if (submitterResponse.getStatusCode() != HttpStatus.OK
              || submitterResponse.getBody() == null) {
            log.error("Failed to fetch submitter applications for user '{}': invalid response",
                currentUsername);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new ApplicationUsersResponse());
          }
          List<String> names = submitterResponse.getBody();
          ApplicationUsersResponse response = new ApplicationUsersResponse();
          response.setNames(names);
          return ResponseEntity.ok(response);

        }
      }
      return ResponseEntity.ok(new ApplicationUsersResponse());
    } catch (Exception e) {
      log.error("Failed to retrieve application names: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ApplicationUsersResponse());
    }
  }

  @Override
  public DesignationsListDTO saveDesignation(String userPm, String designee, String role, UUID pmUserId, String laboratoryUser) {
    DesignationsListDTO designationsListDTO = new DesignationsListDTO();

    designee = designee.replaceAll("[\\[\\]]", "");
    Optional<User> designated;
    Optional<User> laboratoryId;
    if (isValidEmail(designee)) {
      designated = Optional.ofNullable(this.userService.findByEmail(designee));
    } else {
      designated = this.userService.findByUsername(designee);
    }

      if (laboratoryUser != null && !laboratoryUser.isEmpty()) {
          laboratoryUser = laboratoryUser.replaceAll("[\\[\\]]", "");
          if (isValidEmail(laboratoryUser)) {
              laboratoryId = Optional.ofNullable(this.userService.findByEmail(laboratoryUser));
          } else {
              laboratoryId = this.userService.findByUsername(laboratoryUser);
          }

          if (laboratoryId.isPresent()) {
              designationsListDTO.setLaboratoryUser(laboratoryId.get());
              log.info("Laboratory user SET: ID={}, Username={}", laboratoryId);
          } else {
              designationsListDTO.setLaboratoryUser(null);
          }
      }
    RoleDTO roleDto = this.roleService.getRoleByLabel(role);
    Role roleEntity = this.roleMapper.toEntity(roleDto);

    designationsListDTO.setKeycloackId(UUID.fromString(userPm));
    designationsListDTO.setDesignatedUser(designated.get());
    designationsListDTO.setRole(roleEntity);
    if(pmUserId!=null){
        Optional<UserDTO> userPmAddDesignation = this.userService.findOne(pmUserId);
      designationsListDTO.setPmUser(userMapper.toEntity(userPmAddDesignation.get()));

        if (Boolean.TRUE.equals(userPmAddDesignation.get().getEmailVerified())) {
            this.saveDesignationsUsersAttributes(userPmAddDesignation.get().getUsername());
            log.info("Save Attribute Designations Keycloak");
        }
    }
    DesignationsList designationsList = designationsListRepository.save(
        designationsListMapper.toEntity(designationsListDTO));

      DesignationsListDTO savedDto = designationsListMapper.toDto(designationsList);
      if (List.of(Constants.BS_ROLE_PM_PHARMA_RESP_TECH, Constants.BS_ROLE_PM_RESP_CTRL_QA,
          Constants.BS_ROLE_PM_PHARMA_RESP_PROD, Constants.BS_ROLE_PM_RESP_REG_AFF,
          Constants.BS_ROLE_PM_LABO_ETRAN,
          Constants.BS_ROLE_PM_REPRI_LABO,
          Constants.BS_ROLE_PM_LABO_LOC).contains(role)) {
          UserDTO userDTO = this.userMapper.toDto(designated.get());

      Set<RoleDTO> updatedRoles = new HashSet<>(userDTO.getRoles());
      boolean hasEctdRole = updatedRoles.stream()
          .anyMatch(roleECTD -> Constants.BS_ROLE_ECTD.equals(roleECTD.getLabel()));
      boolean hasServiceConsultingRole = updatedRoles.stream().anyMatch(
          roleECTD -> Constants.BS_ROLE_PM_SOCIETE_CONSULTING.equals(roleECTD.getLabel()));

      if (!hasEctdRole) {
        RoleDTO ectdRole = new RoleDTO();
        ectdRole.setId(roleService.getRoleByLabel(Constants.BS_ROLE_ECTD).getId());
        ectdRole.setLabel(Constants.BS_ROLE_ECTD);
        updatedRoles.add(ectdRole);
        userDTO.setRoles(updatedRoles);
        this.userService.update(userDTO);
        log.info("Role ECTD is added successfully");
        if (hasServiceConsultingRole) {
          this.assignECTDForSubMember(this.userMapper.toDto(designated.get()));
        }
      } else {
        log.info("Already Have ECTD");
      }

    }
    return savedDto;
  }

  public void assignECTDForSubMember(UserDTO designated) {

    List<DesignationsList> designations = designationsListRepository.findByPmUserId(
        designated.getId());
    if (!designations.isEmpty()) {
      for (DesignationsList designation : designations) {
        UserDTO userToUpdate = this.userMapper.toDto(designation.getDesignatedUser());
        Set<RoleDTO> updatedRoles = new HashSet<>(userToUpdate.getRoles());
        boolean hasEctdRole = updatedRoles.stream()
            .anyMatch(roleECTD -> Constants.BS_ROLE_ECTD.equals(roleECTD.getLabel()));
        if (!hasEctdRole) {
          RoleDTO ectdRoleToadd = new RoleDTO();
          ectdRoleToadd.setId(roleService.getRoleByLabel(Constants.BS_ROLE_ECTD).getId());
          ectdRoleToadd.setLabel(Constants.BS_ROLE_ECTD);
          updatedRoles.add(ectdRoleToadd);
          userToUpdate.setRoles(updatedRoles);
          this.userService.update(userToUpdate);
          log.info("Role ECTD is added successfully");

        } else {
          log.info("Already Have ECTD");
        }

      }
    }
  }

  private boolean isValidEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return email != null && email.matches(emailRegex);
  }

  @Override
  public void updateDesignationWithUserPmId(UserDTO userDto) {
    log.debug("Update userPM for the designationList with keycloakId : {}",
        userDto.getKeycloakId());

    List<DesignationsList> designationsLists = this.designationsListRepository.findByKeycloackId(
        userDto.getKeycloakId());

    if (designationsLists.isEmpty()) {
      log.debug("Designation not found with Keycloak ID: {}", userDto.getKeycloakId());
      return;
    }
    Optional<UserDTO> userPmDto = this.userService.findOne(userDto.getId());
    if (userPmDto.isEmpty()) {
      log.error("userPmDto  not found with ID: {}", userDto.getId());
      throw new EntityNotFoundException("Designated User not found with ID: " + userDto.getId());
    }
    User userPm = userService.findById(userDto.getId());
    for (DesignationsList designationsList : designationsLists) {
      designationsList.setPmUser(userPm);
      designationsListRepository.save(designationsList);
    }
    userService.saveUser(userPmDto.get());

  }

  public void findDesignationsByKeycloackId(User userPm, List<DesignationsList> designationsLists) {
    log.debug("Fetching designations list for user: {}", userPm.getId());

    Set<String> validRoleLabels = Stream.of(Constants.BS_ROLE_PM_LABO_ETRAN,
            Constants.BS_ROLE_PM_LABO_LOC).map(roleService::getRoleByLabel).filter(Objects::nonNull)
        .map(RoleDTO::getLabel).collect(Collectors.toSet());

    Set<String> userRoleLabels = userPm.getRoles().stream().map(Role::getLabel)
        .collect(Collectors.toSet());

    Set<Role> userRoles = userPm.getRoles();

    boolean hasValidRole = userRoleLabels.stream().anyMatch(validRoleLabels::contains);
    if (!hasValidRole) {
      log.info("User {} does not have valid roles to receive BS_ROLE_ECTD role. No action taken.");
    } else {
      RoleDTO roleEctdDTO = this.roleService.getRoleByLabel(Constants.BS_ROLE_ECTD);
      Role bsEctdRole = this.roleMapper.toEntity(roleEctdDTO);

      if (bsEctdRole == null) {
        throw new EntityNotFoundException("Role BS_ECTD not found");
      }

      RoleDTO rolePromotionDTO = this.roleService.getRoleByLabel(
          Constants.BS_ROLE_PM_AGENCE_PROMOTION);
      Role pmAgencePromotionRole = this.roleMapper.toEntity(rolePromotionDTO);
      if (pmAgencePromotionRole == null) {
        throw new EntityNotFoundException("Role AGENCE_PROMOTION not found");
      }

      RoleDTO roleIndustrielDTO = this.roleService.getRoleByLabel(Constants.BS_ROLE_PM_INDUSTRIEL);
      Role bsPmIndustrielRole = this.roleMapper.toEntity(roleIndustrielDTO);
      if (bsPmIndustrielRole == null) {
        throw new EntityNotFoundException("Role BS_PM_INDUSTRIEL not found");
      }
      boolean hasPmRole =
          userRoles.contains(pmAgencePromotionRole) || userRoles.contains(bsPmIndustrielRole);

      if (hasPmRole) {
        userRoles.add(bsEctdRole);
        userPm.setRoles(userRoles);
        userService.saveUser(this.userMapper.toDto(userPm));
        this.userService.reassignUserToNewRoles(userPm);
      }

      for (DesignationsList designationsList : designationsLists) {
        log.info("Processing designation with Keycloak ID: {}", designationsList.getKeycloackId());

        UserDTO designatedUser = userService.findOne(designationsList.getDesignatedUser().getId())
            .orElse(null);
        User designatedEntity = this.userMapper.toEntity(designatedUser);

        assignRoleRecursively(designatedEntity, bsEctdRole);


      }
    }
    processDesignatedUsersRoles(designationsLists);

    log.info("Updated roles for user {}: {}", userPm.getId(), userRoles);
  }

  private void assignRoleRecursively(User designatedUser, Role bsEctdRole) {
    if (designatedUser == null) {
      return;
    }

    Set<Role> userRole = designatedUser.getRoles();
    if (designatedUser.getUserType() == UserType.PHYSICAL) {
      userRole.add(bsEctdRole);
      designatedUser.setRoles(userRole);
      userService.saveUser(this.userMapper.toDto(designatedUser));
      this.userService.reassignUserToNewRoles(designatedUser);

    } else if (designatedUser.getUserType() == UserType.COMPANY) {

      RoleDTO roleIndustrielDTO = this.roleService.getRoleByLabel(Constants.BS_ROLE_PM_INDUSTRIEL);
      RoleDTO rolePromotionDTO = this.roleService.getRoleByLabel(
          Constants.BS_ROLE_PM_AGENCE_PROMOTION);

      if (userRole.contains(roleIndustrielDTO) || userRole.contains(rolePromotionDTO)) {
        userRole.add(bsEctdRole);
        designatedUser.setRoles(userRole);
        userService.saveUser(this.userMapper.toDto(designatedUser));
        this.userService.reassignUserToNewRoles(designatedUser);

      }

      List<DesignationsList> companyDesignations = designationsListRepository.findByKeycloackId(
          designatedUser.getKeycloakId());

      for (DesignationsList companyDesignation : companyDesignations) {
        UserDTO companyUser = userService.findOne(companyDesignation.getDesignatedUser().getId())
            .orElse(null);
        User CompanyEntity = this.userMapper.toEntity(companyUser);
        assignRoleRecursively(CompanyEntity, bsEctdRole);
      }
    }

  }
    public Map<String, String> getDesignatedUserIdsByPmId(UUID pmKeycloakId) {
        User pmUser = userRepository.findByKeycloakId(pmKeycloakId);
        if (pmUser == null) {
            log.warn("PM user not found for Keycloak ID: {}", pmKeycloakId);
            return Collections.emptyMap();
        }
        RoleDTO laboRole = this.roleService.getRoleByLabel(Constants.BS_ROLE_PM_REPRI_LABO);
        if (laboRole == null) {
            log.warn("Role BS_ROLE_PM_LABO not found");
            return Collections.emptyMap();
        }
        String validRoleLabel = laboRole.getLabel();

        return designationsListRepository.findByPmUserId(pmUser.getId()).stream()
            .filter(d -> d.getRole().getLabel().equals(validRoleLabel)
            )
            .collect(Collectors.groupingBy(
                d -> d.getDesignatedUser().getUsername(),
                Collectors.mapping(
                    d -> {
                        User laboUser = d.getLaboratoryUser();
                        return laboUser.getUsername() + ":" + laboUser.getDenomination();
                    },
                    Collectors.joining(",")
                )
            ));
    }

    @CacheEvict(cacheNames = CACHE_NAME , allEntries = true)
    public void saveDesignationsUsersAttributes(String username) {
    log.info("Starting attribute saving process for user: {}", username);

    Optional<User> optionalUser = userService.findByUsername(username);
    if (optionalUser.isEmpty()) {
      log.error("User not found in DB for username: {}", username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    User pmUser = optionalUser.get();
    UUID pmKeycloakId = pmUser.getKeycloakId();
    String pmUsername = pmUser.getUsername();
    log.debug("Retrieved user: {}, Keycloak ID: {}", pmUsername, pmKeycloakId);

    Set<Role> userRoles = pmUser.getRoles();
    Set<String> userRoleLabels = userRoles.stream().map(Role::getLabel).collect(Collectors.toSet());
    log.debug("User roles for {}: {}", pmUsername, userRoleLabels);

    boolean pmAgencePromotionRole = userRoleLabels.contains(Constants.BS_ROLE_PM_AGENCE_PROMOTION);
    boolean pmIndustryRole = userRoleLabels.contains(Constants.BS_ROLE_PM_INDUSTRIEL);

    if (pmUsername == null) {
      log.error("Username is null for PM Keycloak ID: {}", pmKeycloakId);
      throw new IllegalArgumentException(
          "Username cannot be null for PM Keycloak ID: " + pmKeycloakId);
    }

    boolean pmEtrRole = userRoleLabels.contains(Constants.BS_ROLE_PM_AGENCE_PROMOTION);
    boolean pmLocRole = userRoleLabels.contains(Constants.BS_ROLE_PM_INDUSTRIEL);

    if (pmAgencePromotionRole && (pmEtrRole || pmLocRole)) {
      log.info("PM user {} has promotion role. Proceeding with designation handling.", pmUsername);

      UsersResource usersResource = keycloak.realm(realm).users();
      UserResource userResource = usersResource.get(String.valueOf(pmKeycloakId));
      UserRepresentation userRep = userResource.toRepresentation();

      if (userRep == null) {
        log.error("User representation not found in Keycloak for ID: {}", pmKeycloakId);
        throw new UsernameNotFoundException("User not found in Keycloak for ID: " + pmKeycloakId);
      }

        Map<String, String> applicantListLabo = getDesignatedUserIdsByPmId(pmKeycloakId);
        log.debug("Applicant list for PM {}: {}", pmUsername, applicantListLabo);

      List<DesignationsList> designations = designationsListRepository.findByPmUserId(
          pmUser.getId());

      Set<User> ppDesignated = new HashSet<>();
      Set<User> pmDesignated = new HashSet<>();

      designations.forEach(designation -> {
        String designatedUsername = designation.getDesignatedUser().getUsername().toLowerCase();
        User designatedUser = designation.getDesignatedUser();
          boolean hasEctdRole = designatedUser.getRoles().stream()
              .anyMatch(role -> role.getLabel().equalsIgnoreCase("BS_ROLE_ECTD"));

          if (!hasEctdRole) {
              return;
          }
        if (designatedUsername.startsWith("ext")) {
          ppDesignated.add(designatedUser);
        } else if (designatedUsername.startsWith("pm-ext")) {
          pmDesignated.add(designatedUser);
        }
      });

      log.debug("Directly designated PMs: {}",
          pmDesignated.stream().map(User::getUsername).collect(Collectors.toSet()));
      log.debug("Directly designated PPs: {}",
          ppDesignated.stream().map(User::getUsername).collect(Collectors.toSet()));

      Map<String, Set<User>> ppDesignationDesignatedRecursively = pmDesignated.stream().collect(
          Collectors.toMap(pm -> pm.getUsername(),
              pm -> designationsListRepository.findByPmUserId(pm.getId()).stream()
                  .map(DesignationsList::getDesignatedUser).collect(Collectors.toSet())));

      Set<User> ppDesignatedRecursively = ppDesignationDesignatedRecursively.values().stream()
          .flatMap(Set::stream).collect(Collectors.toSet());


        List<String> allLabos = applicantListLabo.values().stream()
            .flatMap(labos -> Arrays.stream(labos.split(",")))
            .distinct()
            .collect(Collectors.toList());

        if (!allLabos.isEmpty()) {
            String applicantStr = String.join(",", allLabos);
            log.info("Saving 'applicant' attribute for PM {}: {}", pmUsername, applicantStr);
            keycloakAuthService.addCustomAttributeToUser(userRep, "applicant", applicantStr);

            ppDesignatedRecursively.forEach(el -> {
                String specificLabos = applicantListLabo.get(el.getUsername());
                if (specificLabos != null) {
                    log.debug("Adding 'applicant' attribute to recursively designated user {}: {}",
                        el.getUsername(), specificLabos);
                    addAttributeToDesignatedUser(el, "applicant", specificLabos);
                }
            });

            pmDesignated.forEach(el -> {
                String specificLabos = applicantListLabo.get(el.getUsername());
                if (specificLabos != null) {
                    log.debug("Adding 'applicant' attribute to directly designated PM {}: {}",
                        el.getUsername(), specificLabos);
                    addAttributeToDesignatedUser(el, "applicant", specificLabos);
                }
            });

            ppDesignated.forEach(el -> {
                String specificLabos = applicantListLabo.get(el.getUsername());
                if (specificLabos != null) {
                    log.debug("Adding 'applicant' attribute to directly designated PP {}: {}",
                        el.getUsername(), specificLabos);
                    addAttributeToDesignatedUser(el, "applicant", specificLabos);
                }
            });

      } else {
        log.warn("No designated applicants found for PM Keycloak ID: {}", pmKeycloakId);
      }

      if (keycloakAuthService.userExistsByUsername(pmUsername)) {
        String submitterStr = pmUsername + ":" + pmUser.getDenomination();
        log.info("Saving 'submitter' attribute for PM: {}", submitterStr);
        keycloakAuthService.addCustomAttributeToUser(userRep, "submitter", submitterStr);

        ppDesignated.forEach(el -> {
          log.debug("Adding 'submitter' attribute to directly designated PP: {}", el.getUsername());
          addAttributeToDesignatedUser(el, "submitter", submitterStr);
        });
      } else {
        log.warn("PM username '{}' not found in Keycloak", pmUsername);
      }

      if (!pmDesignated.isEmpty()) {
        pmDesignated.forEach(el -> {
          log.debug("Adding 'submitter' attribute to PM Designated user: {}", el.getUsername());
          addAttributeToDesignatedUser(el, "submitter", el.getUsername());
        });

        ppDesignationDesignatedRecursively.forEach((pm, pp) -> {
          if (!pp.isEmpty()) {
            pp.forEach(el -> {
              log.debug("Adding 'submitter' attribute '{}' to recursively designated PP: {}", pm,
                  el.getUsername());
              addAttributeToDesignatedUser(el, "submitter", pm);
            });
          }
        });
      }

      userResource.update(userRep);
      log.info("Completed updating Keycloak attributes for user: {}", pmUsername);

    } else if (pmIndustryRole) {
      log.info("User {} has industry role. Delegating to industrial handler.", pmUsername);
      saveDesignationsIndustrialUsersAttributes(username);
    } else {
      log.warn("No matching role found for user: {}", pmUsername);
    }
  }


  private void addAttributeToDesignatedUser(User user, String attributeKey, String attributeValue) {
    UsersResource usersResource = keycloak.realm(realm).users();
    UserResource userResource = usersResource.get(String.valueOf(user.getKeycloakId()));
    UserRepresentation userRep = userResource.toRepresentation();

    List<String> oldAttributes = keycloakAuthService.getAttributeFromOfUser(userRep, attributeKey);
    if (oldAttributes != null && !oldAttributes.isEmpty()) {
      oldAttributes.forEach(oldVal -> {
        if (!oldVal.contains(attributeValue)) {
          String newVal = oldVal + ',' + attributeValue;
          log.debug("Updating existing '{}' attribute for user {}: {}", attributeKey,
              user.getUsername(), newVal);
          keycloakAuthService.addOrUpdateCustomAttributeToUser(userRep, attributeKey, newVal);
        }
      });
    } else {
      log.debug("Setting new '{}' attribute for user {}: {}", attributeKey, user.getUsername(),
          attributeValue);
      keycloakAuthService.addOrUpdateCustomAttributeToUser(userRep, attributeKey, attributeValue);
    }
  }

  @Override
  public boolean existsDesignationsListByDesignatedUserId(UUID designatedUserId) {
    return designationsListRepository.existsDesignationsListByDesignatedUserId(designatedUserId);
  }

  public void processDesignatedUsersRoles(List<DesignationsList> designationsLists) {
    if (designationsLists == null || designationsLists.isEmpty()) {
      log.info("No designations provided, skipping role assignment.");
      return;
    }
    RoleDTO bsEctdRole = this.roleService.getRoleByLabel(Constants.BS_ROLE_ECTD);
    Role bsEctd = this.roleMapper.toEntity(bsEctdRole);

    RoleDTO bsLocRole = this.roleService.getRoleByLabel(Constants.BS_ROLE_PM_LABO_LOC);
    Role bsLoc = this.roleMapper.toEntity(bsLocRole);

    for (DesignationsList designation : designationsLists) {
      UUID userId = designation.getDesignatedUser().getId();
      if (userId == null) {
        log.warn("Designated user ID is null for designation with Keycloak ID: {}, skipping",
            designation.getKeycloackId());
        continue;
      }

      UserDTO userDto = userService.findOne(userId).orElse(null);
      if (userDto == null) {
        log.warn("User with ID {} not found, skipping", userId);
        continue;
      }

      User designatedUser = userMapper.toEntity(userDto);
      Set<Role> userRoles = designatedUser.getRoles();
      if (userRoles == null) {
        userRoles = new HashSet<>();
      }

      Set<String> userRoleLabels = userRoles.stream().map(Role::getLabel)
          .collect(Collectors.toSet());

      boolean hasIndustryOrPromotion =
          userRoleLabels.contains(Constants.BS_ROLE_PM_INDUSTRIEL) || userRoleLabels.contains(
              Constants.BS_ROLE_PM_AGENCE_PROMOTION);

      if (!hasIndustryOrPromotion) {
        log.info("User {} does not have industry or promotion role, skipping", userId);
        continue;
      }

      boolean updated = false;

      if ((userRoleLabels.contains(Constants.BS_ROLE_PM_LABO_LOC)) && !userRoleLabels.contains(
          Constants.BS_ROLE_ECTD)) {
        userRoles.add(bsEctd);
        userRoles.add(bsLoc);
        log.info("Assigned BS_ROLE_ECTD to user {} based on LOC/ETRAN role", userId);
        updated = true;
      }
      if (userRoleLabels.contains(Constants.BS_ROLE_PM_LABO_ETRAN) && !userRoleLabels.contains(
          Constants.BS_ROLE_ECTD)) {
        userRoles.add(bsEctd);
        log.info("Assigned BS_ROLE_PM_LABO_LOC to user {} based on ECTD role", userId);
        updated = true;
      }

      if (userRoleLabels.contains(Constants.BS_ROLE_ECTD) && !userRoleLabels.contains(
          Constants.BS_ROLE_PM_LABO_LOC)) {
        userRoles.add(bsLoc);
        log.info("Assigned BS_ROLE_PM_LABO_LOC to user {} based on ECTD role", userId);
        updated = true;
      }
      if (updated) {
        designatedUser.setRoles(userRoles);
        userService.saveUser(userMapper.toDto(designatedUser));
        userService.reassignUserToNewRoles(designatedUser);
      }
    }
  }

  public List<DesignationsList> getDesignationListOfIndustryUsers(UUID pmKeycloakId) {
    User pmUser = userRepository.findByKeycloakId(pmKeycloakId);

    if (pmUser == null) {
      log.warn("PM user not found for Keycloak ID: {}", pmKeycloakId);
      return Collections.emptyList();
    }

    List<DesignationsList> designations = designationsListRepository.findByPmUserId(pmUser.getId());

    if (designations.isEmpty()) {
      log.warn("No designations found for PM Keycloak ID: {}", pmKeycloakId);
      return Collections.emptyList();
    }

    return designations;
  }
    @CacheEvict(cacheNames = CACHE_NAME , allEntries = true)
  public void saveDesignationsIndustrialUsersAttributes(String username) {

    Optional<User> optionalUser = userService.findByUsername(username);
    if (optionalUser.isEmpty()) {
      log.error("User not found in DB for username: {}", username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }

    User pmUser = optionalUser.get();
    UUID pmKeycloakId = pmUser.getKeycloakId();
    String pmUsername = pmUser.getUsername();

    List<DesignationsList> designations = getDesignationListOfIndustryUsers(pmKeycloakId);

    if (designations.isEmpty()) {
      return;
    }

    String pmSubmitterInfo = pmUsername + ":" + pmUser.getDenomination();

    for (DesignationsList designation : designations) {
      User designatedUser = designation.getDesignatedUser();
      if (designatedUser == null) {
        log.warn("Designated user is null in designation");
        continue;
      }

      UUID designatedUserKeycloakId = designatedUser.getKeycloakId();
      String designatedUsername = designatedUser.getUsername();

      if (designatedUserKeycloakId == null || designatedUsername == null) {
        log.warn("Designated user has null Keycloak ID or username: {}", designatedUser.getId());
        continue;
      }

      try {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource designatedUserResource = usersResource.get(
            String.valueOf(designatedUserKeycloakId));
        UserRepresentation designatedUserRep = designatedUserResource.toRepresentation();

        if (designatedUserRep != null) {
          log.info("Saving 'applicant' attribute for designated user {}: {}", designatedUsername,
              pmSubmitterInfo);
          keycloakAuthService.addCustomAttributeToUser(designatedUserRep, "applicant",
              pmSubmitterInfo);

          log.info("Saving 'submitter' attribute for designated user {}: {}", designatedUsername,
              pmSubmitterInfo);
          keycloakAuthService.addCustomAttributeToUser(designatedUserRep, "submitter",
              pmSubmitterInfo);

          designatedUserResource.update(designatedUserRep);

          processSecondLayerDesignations(designatedUser, pmSubmitterInfo);
        }
      } catch (Exception e) {
        log.error("Error updating attributes for designated user {}: {}", designatedUsername,
            e.getMessage());
      }
    }
  }
    @Transactional
    @Override
    public void deleteDesignation(UUID id) {
        DesignationsList designation = designationsListRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Designation not found with id: " + id));

        //  Get the designated user
        User designatedUser = designation.getDesignatedUser();
        UserDTO designatedUserDto = userMapper.toDto(designatedUser);

        // Delete the designation
        this.designationsListRepository.deleteDesignationsListById(id);

        applicationEventPublisher.publishEvent(
            new DesignationDeletedEvent(this, designatedUserDto, id)
        );

        log.info("Designation {} deleted and ECTD attributes updated for user {}",
            id, designatedUser.getUsername());    }

    private void processSecondLayerDesignations(User pmUser, String originalPmInfo) {
    List<DesignationsList> secondLayerDesignations = designationsListRepository.findByPmUserId(
        pmUser.getId());

    String currentPmSubmitterInfo = pmUser.getUsername() + ":" + pmUser.getDenomination();

    for (DesignationsList designation : secondLayerDesignations) {
      User designatedUser = designation.getDesignatedUser();
      if (designatedUser == null) {
        continue;
      }

      UUID designatedUserKeycloakId = designatedUser.getKeycloakId();
      String designatedUsername = designatedUser.getUsername();

      if (designatedUserKeycloakId == null || designatedUsername == null) {
        continue;
      }

      try {
        UsersResource usersResource = keycloak.realm(realm).users();
        UserResource designatedUserResource = usersResource.get(
            String.valueOf(designatedUserKeycloakId));
        UserRepresentation designatedUserRep = designatedUserResource.toRepresentation();

        if (designatedUserRep != null) {
          log.info("Saving 'applicant' attribute for second layer user {}: {}", designatedUsername,
              originalPmInfo);
          keycloakAuthService.addCustomAttributeToUser(designatedUserRep, "applicant",
              originalPmInfo);

          log.info("Saving 'submitter' attribute for second layer user {}: {}", designatedUsername,
              currentPmSubmitterInfo);
          keycloakAuthService.addCustomAttributeToUser(designatedUserRep, "submitter",
              currentPmSubmitterInfo);

          designatedUserResource.update(designatedUserRep);
        }
      } catch (Exception e) {
        log.error("Error updating attributes for second layer user {}: {}", designatedUsername,
            e.getMessage());
      }
    }
  }
    public boolean pmHasEctdByDesignatedUser(UUID designatedUserId) {

        List<DesignationsList> designations = designationsListRepository.findByDesignatedUserId(designatedUserId);

        if (designations == null || designations.isEmpty()) {
            return false;
        }

        return designations.stream()
            .map(DesignationsList::getPmUser)
            .filter(Objects::nonNull)
            .map(User::getRoles)
            .filter(Objects::nonNull)
            .anyMatch(roles -> roles.stream()
                .anyMatch(r -> Constants.BS_ROLE_ECTD.equals(r.getLabel()))
            );
  }
    public List<UserRepresentation> getAllPPUsers() {

        log.debug("Fetching all users from Keycloak cache");
        List<UserRepresentation> allUsers = userService.getAllUsersFromKeycloak();

        if (allUsers == null || allUsers.isEmpty()) {
            log.warn("No users returned from Keycloak");
            return Collections.emptyList();
        }

        List<UserRepresentation> physicalUsers = allUsers.stream()
            .filter(user -> {
                String username = user.getUsername();
                return username != null && username.startsWith(PERFIX_USERNAME);
            })
            .toList();

        log.info("Physical users (username starts with 'ext-') count={}", physicalUsers.size());
        return physicalUsers;
    }


    public ResponseEntity<List<ApplicantListUserResponse>> getApplicantsAttributes() {
        try {
            log.info("Starting getApplicantsAttributes");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof BearerTokenAuthentication)) {
                log.warn("Unauthorized access: authentication is not BearerTokenAuthentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.emptyList());
            }

            // Get only PHYSICAL users
            List<UserRepresentation> physicalUsers = this.getAllPPUsers();

            if (physicalUsers.isEmpty()) {
                log.info("No physical users found");
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<ApplicantListUserResponse> responses = physicalUsers.stream()
                .filter(user -> {
                    Map<String, List<String>> attributes = user.getAttributes();
                    return attributes != null
                        && attributes.containsKey("applicant")
                        && attributes.get("applicant") != null
                        && !attributes.get("applicant").isEmpty();
                })
                .map(user -> {
                    ApplicantListUserResponse response = new ApplicantListUserResponse();
                    response.setUsername(user.getUsername());
                    response.setApplicants(user.getAttributes().get("applicant").toString());
                    return response;
                })
                .toList();

            log.info("Successfully retrieved applicant attributes count={}", responses.size());
            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            log.error("Failed to retrieve applicant attributes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.emptyList());
        }
    }



}
