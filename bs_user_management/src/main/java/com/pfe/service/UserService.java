package com.pfe.service;

import com.pfe.domain.Group;
import com.pfe.domain.User;
import com.pfe.service.dto.ResetPasswordDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.dto.UserDenominationDTO;
import com.pfe.service.dto.request.AssignRoleApplication;
import com.pfe.service.dto.request.ChangePasswordRequestDTO;
import com.pfe.service.dto.request.CreateAccountRequest;
import com.pfe.service.dto.request.CreateCompanyAccountRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.pfe.service.dto.ProfileDTO;
import com.pfe.service.dto.request.UpdateProfileRequest;
import com.pfe.service.dto.response.CheckEligibilityResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


/**
 * Service Interface for managing {@link User}.
 */
public interface UserService {

    /**
     * Save a User.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    UserDTO save(UserDTO userDTO);
    UserDTO saveUser(UserDTO userDTO);

    /**
     * Updates a User.
     *
     * @param userDTO the entity to update.
     * @return the persisted entity.
     */
    UserDTO update(UserDTO userDTO);

    /**
     * Partially updates a User.
     *
     * @param userDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserDTO> partialUpdate(UserDTO userDTO);

    /**
     * Complete a User Profile.
     *
     * @param userDTO the entity to update partially.
     * @param id      the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserDTO> completeUserProfile(UserDTO userDTO, String id);

    /**
     * Get the "id" User.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserDTO> findOne(UUID id);
    User findById(UUID id);

    /**
     * Delete the "id" User.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    UserDTO deleteUser(UUID id);

    Page<UserDTO> getUsersByGroupId(UUID id, Pageable pageable);

    /**
     * Get the "username" User.
     *
     * @param username the username of the entity.
     * @return the entity.
     */
    Optional<User> findByUsername(String username);


    void verifyEmail(String token);

    UserDTO createUser(CreateAccountRequest createAccountRequest, Boolean confirm);

    List<UserDTO> getAllUsers();

    List<UserDTO> findByIds(List<UUID> ids);

    void sendVerificationEmail(String userId, String username, String userEmail, String password);

    String getEmailById(UUID id);

    String isProfileCompleted(String username);

    void resendEmailVerification(String token);

    List<UUID> getGroupIdsByUserId(UUID userId);

    List<UUID> getRoleIdsByUserId(UUID userId);

    UUID getRootId();

    void removeRoleAssociation(UUID roleId);

    void deleteUserGroupAssociation(User user, Set<Group> groups);

    List<UserDTO> getUsersByGroupId(UUID id);

    void reassignUsersToNewGroups(List<UserDTO> usersToReassign);

    List<UserDTO> getUsersByRoleId(UUID id);

    void reassignUsersToNewRoles(List<UserDTO> usersToReassign);
    void reassignUserToNewRoles(User usersToReassign);

    UserDTO createCompanyUser(CreateCompanyAccountRequest createCompanyAccountRequest);

    UserDTO findUserByFiscalId(String fiscalId);

    UserDTO findUserByUsername(String username);

    void resetPasswordRequest(ChangePasswordRequestDTO changePasswordRequestDTO);

    void resetPassword(ResetPasswordDTO resetPasswordDTO, String token);

    void resetPasswordFirstConnection(ResetPasswordDTO resetPasswordDTO);

    void checkUserInformations(String gender, String nationalId, String phoneNumber, String email,
        String lastName, String nationality, String country, String firstName, String birthDate,
        String address);

    List<UserDTO> getAllPhysicalUsers();


    ResponseEntity<UserDTO> saveAndCompleteMyProfile(UUID userId, ProfileDTO profileDTO);

    void syncDefaultUsers();
     UserDTO updateAndCompletedProfile(UpdateProfileRequest updateProfileRequest);

    UUID getUserIdFromResponse(UUID userId);
    User findByEmail(String email);
    CheckEligibilityResponse checkUsernameEligibility(String username, String roleLabel) ;

    boolean doesProfileKeyExist(UUID userId, String key);
    Optional<String> getProfileValue(UUID userId, String key);
    boolean existsUserByRole( UUID userId, UUID roleId);
    boolean existPhoneNumber(String phoneNumber);
    List<UserDenominationDTO> getUsernamesAndDenominationsByRole(String roleLabel);
     String getUsernameById(UUID id);
    ResponseEntity<UserDTO> saveDataPm(String keycloackId, ProfileDTO profileDTO);
    void fixKeycloakIdsByUsername();
    Optional<UserDTO> findByKeycloakId(UUID keycloackId);
    void updateKeycloakUserDesignations(AssignRoleApplication assignRoleApplication);

    void updateProfileCompletedKeycloak();
     List<UserDTO> getUserByNationalId(String nationalId) ;
     boolean checkUsernameStartsWithInt(String nationalId);

     boolean checkUsernameMatches(String nationalId, String username) ;
     void syncUserWithKeycloak(User user, Set<Group> groupToAdd) ;
     List<UserRepresentation> getAllUsersFromKeycloak();
     void removeEctdCompletely(UserDTO user) ;
     void updateEctdUserAttributes(String username) ;
    boolean existsByCin(String nationalId);
    boolean existsByEmail(String email);
    }
