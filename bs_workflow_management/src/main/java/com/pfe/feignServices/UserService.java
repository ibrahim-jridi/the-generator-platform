package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.dto.ProfileDTO;
import com.pfe.dto.DesignationsListDTO;
import com.pfe.dto.UserDTO;
import com.pfe.dto.request.CreateAccountRequest;
import com.pfe.dto.request.CreateCompanyAccountRequest;
import com.pfe.dto.request.SaveDesignationRequest;
import com.pfe.dto.request.UpdateProfileRequest;
import com.pfe.services.criteria.UserCriteria;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${bs-app.endpoint.gateway}"
    + "/user-management/api/v1", configuration = FeignClientConfig.class)

public interface UserService {
    @GetMapping("/user")
     ResponseEntity<List<UserDTO>> getAllusers(
        UserCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    );

    @GetMapping("/user/by-ids")
     ResponseEntity<List<UserDTO>> getUsersByIds(@RequestParam List<UUID> ids);

    @PutMapping("/user/completeProfile/{id}")
    ResponseEntity<UserDTO> completeUserProfile(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody UserDTO userDTO
    ) throws URISyntaxException;


  @GetMapping("/open-api/getEmailById")
  ResponseEntity<String> getEmailById(@RequestParam UUID id);

  @GetMapping("/user/{id}")
  ResponseEntity<UserDTO> getuser(@PathVariable("id") UUID id);

  @GetMapping("/user/getGroupIdsByUserId")
  ResponseEntity<List<UUID>> getGroupIdsByUserId(@RequestParam UUID id);

  @GetMapping("/open-api/getRootId")
  UUID getRootId();
  @PostMapping("/user")
     ResponseEntity<UserDTO> createUser(
        @RequestBody CreateAccountRequest createAccountRequest);

    @PostMapping("/user/company-user")
    ResponseEntity<UserDTO> createCompanyUser(@RequestBody CreateCompanyAccountRequest request);
    @GetMapping("/user/getRoleIdsByUserId")
    ResponseEntity<List<UUID>> getRoleIdsByUserId(@RequestParam UUID id);

    @PostMapping ("/user/profile")
    ResponseEntity<UserDTO> saveAndCompleteMyProfile(
        @RequestBody ProfileDTO profileDTO
    );
  @PostMapping("/designations/save-designation")
  ResponseEntity<DesignationsListDTO> saveDesignation(@RequestBody SaveDesignationRequest saveDesignationRequest);

    @PutMapping("/user/update-user")
     ResponseEntity<UserDTO> updateAndCompletedProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest);

    @GetMapping("/user/profile/exists")
     ResponseEntity<Boolean> doesProfileKeyExist(@RequestParam UUID userId, @RequestParam String key);

    @GetMapping("/user/profile/value")
     ResponseEntity<String> getProfileValue(@RequestParam UUID userId, @RequestParam String key);

    @GetMapping("/user/user-by-role/exists")
     ResponseEntity<Boolean> existsUserByRole(@RequestParam UUID userId, @RequestParam UUID roleId);

    @GetMapping("/designations/designated-user/exists")
     ResponseEntity<Boolean> existsDesignationsListByDesignatedUserId(@RequestParam UUID userId);

    @GetMapping("/open-api/find-user-by-user-name/{username}")
     ResponseEntity<UserDTO> findUserByUserName(@PathVariable String username);
    @PutMapping("/designations/update-designations-users_attributes")
    ResponseEntity<String> saveDesignationsUsersAttributes(@RequestParam("username") String username);

    @GetMapping("/user/get-username-by-id/{id}")
     ResponseEntity<String> getUsernameById(@PathVariable UUID id);

    @PostMapping("/user/profile/pm")
     ResponseEntity<UserDTO> saveDataPm(@RequestParam String keycloackId, @RequestBody ProfileDTO profileDTO);

    @GetMapping("/user/get-by-keycloackId/{keycloackId}")
    Optional<UserDTO> findByKeycloakId(@PathVariable("keycloackId") UUID keycloackId);

    @GetMapping("/user/get-optional-user/{id}")
    Optional<UserDTO> getOptionalUser(@PathVariable("id") UUID id);

    @GetMapping("/designations/{designatedUserId}/pm-has-ectd")
     ResponseEntity<Boolean> doesPmHaveEctd(@PathVariable UUID designatedUserId) ;

    }
