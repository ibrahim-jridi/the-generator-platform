package com.pfe.repository;

import com.pfe.domain.DesignationsList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface DesignationsListRepository extends BaseRepository<DesignationsList, UUID> {


    Page<DesignationsList> findByPmUserId(UUID pmUser, Pageable pageable);
    List<DesignationsList> findByKeycloackId(UUID keycloakId);
    boolean existsDesignationsListByDesignatedUserId(UUID designatedUserId);
    Page<DesignationsList> findAll(Specification<DesignationsList> spec, Pageable pageable);
    List<DesignationsList> findByPmUserId(UUID pmUser);
    List<DesignationsList> findByDesignatedUserId(UUID designatedUserId);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE from bs_designations_list as des where des.id =:id",nativeQuery = true)
    void deleteDesignationsListById(@Param("id") UUID id);
    @Query("SELECT d FROM DesignationsList d WHERE d.designatedUser.id = :userId AND d.deleted = false")
    List<DesignationsList> findByDesignatedUserIdAndDeletedFalse(@Param("userId") UUID userId);


}
