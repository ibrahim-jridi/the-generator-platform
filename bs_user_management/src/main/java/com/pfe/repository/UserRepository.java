package com.pfe.repository;

import com.pfe.domain.User;
import com.pfe.domain.enumeration.UserType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the User entity.
 * <p>
 * When extending this class, extend UserRepositoryWithBagRelationships too. For more information
 * refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface UserRepository extends BaseRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findByIdIn(@Param("ids") List<UUID> ids);

    List<User> findAllByDeletedFalse();

    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    Optional<String> getEmailById(@Param("id") UUID id);

    @Query(value = "select ug.group_id from bs_rel_user_group ug where ug.user_id=:userId", nativeQuery = true)
    List<UUID> getGroupIdsByUserId(@Param("userId") UUID userId);

    @Query(value = "select ug.role_id from bs_rel_user_role ug where ug.user_id=:userId", nativeQuery = true)
    List<UUID> getRoleIdsByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT u.* FROM bs_user u " +
        "LEFT JOIN bs_rel_user_group rug ON u.id = rug.user_id " +
        "WHERE rug.group_id = ?1", nativeQuery = true)
    Page<User> findUsersByGroupId(UUID groupId, Pageable pageable);

    User findByKeycloakId(UUID keycloackId);

    User findByNationalIdAndDeletedFalse(String nationalId);


    User findByPhoneNumberAndDeletedFalse(String phoneNumber);

    User findByTaxRegistrationAndDeletedFalse(String taxRegistration);

    @Query("SELECT u FROM User u JOIN u.groups g WHERE g.id = :groupId AND u.deleted = false")
    List<User> findAllUsersByGroupId(UUID groupId);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id = :roleId AND u.deleted = false")
    List<User> findAllUsersByRoleId(UUID roleId);

    Optional<User> findUserByTaxRegistrationAndDeletedFalse(String fiscalId);

    Optional<User> findByUsernameIgnoreCaseAndDeletedFalse(String username);


    Boolean existsByTaxRegistrationAndDeletedFalse(String taxRegistration);

    List<User> findAllUsersByUserTypeAndDeletedFalse(UserType userType);

    List<User> findAllByDeletedTrue();
    Optional<User> findByEmail(String email);

    Boolean existsByUsernameAndIsProfileCompleted(String username, Boolean isProfileCompleted);

    @Query("SELECT COUNT(p) > 0 FROM User u JOIN u.profile p WHERE u.id = :userId AND KEY(p) = :key")
    boolean existsProfileKey(@Param("userId") UUID userId, @Param("key") String key);

    @Query("SELECT VALUE(p) FROM User u JOIN u.profile p WHERE u.id = :userId AND KEY(p) = :key")
    Optional<String> findProfileValue(@Param("userId") UUID userId, @Param("key") String key);

    @Query("SELECT COUNT(r) > 0 FROM User u JOIN u.roles r  WHERE u.id = :userId AND r.id = :roleId")
    boolean existsUserByRole(@Param("userId") UUID userId, @Param("roleId") UUID roleId);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.username ILIKE 'pm-ext-%' AND r.label= :roleIndustriel")
    List<User> findAllCompanyUserByRole(String roleIndustriel);

    @Query(value = """
        SELECT DISTINCT u.*
        FROM bs_user u
        LEFT JOIN bs_rel_user_role ur ON u.id = ur.user_id
        LEFT JOIN bs_role r ON ur.role_id = r.id
        WHERE r.label =  :roleIndustriel
        UNION
        SELECT DISTINCT du.*
        FROM bs_user du
        LEFT JOIN bs_designations_list d ON du.id = d.designated_user_id
        LEFT JOIN bs_user pmu ON d.pm_user_id = pmu.id
        LEFT JOIN bs_rel_user_role ur ON pmu.id = ur.user_id
        LEFT JOIN bs_role r ON ur.role_id = r.id
        WHERE r.label =  :roleIndustriel
        """, nativeQuery = true)
    List<User> findAllIndustryAndDesignatedUsersWithBsIndustrielRole(String roleIndustriel);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.label = :roleLabel AND u.deleted = false")
    List<User> findUsersByRole(@Param("roleLabel") String roleLabel);

    List<User> findAllByNationalIdAndDeletedFalse(@Param("nationalId") String nationalId);
    @Query(value = "select u from User u where u.id =:id")
    Optional<User>findUserById(@Param("id") UUID id);

    @Query("SELECT u.username FROM User u WHERE u.nationalId = :nationalId AND u.deleted = false")
    List<String> findUsernameByNationalId(@Param("nationalId") String nationalId);

    boolean existsByEmailAndDeletedFalse(String email);


}
