package com.pfe.repository;

import com.pfe.domain.WaitingList;
import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
import com.pfe.service.dto.response.WaitingListWithNames;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WaitingListRepository extends BaseRepository<WaitingList, UUID>{

    @Query("SELECT w " +
        "FROM WaitingList  w " +
        "WHERE w.governorate = :governorate" +
        "  AND w.category = :category" +
        "  AND w.status = :status" +
        "  AND (" +    "       (:delegation IS NOT NULL AND w.delegation = :delegation)" +
        "       OR (:municipality IS NOT NULL AND w.municipality = :municipality)" +    "  )" +
        "ORDER BY w.createdDate ASC")

    List<WaitingList> findByCriteriaSortedByCreatedDate(
        @Param("governorate") Governorate governorate,
        @Param("category") Category category,
        @Param("delegation") String delegation,
        @Param("municipality") String municipality,
        @Param("status") StatusWaitingList status
    );

    boolean existsByIdUserAndStatus(UUID idUser, StatusWaitingList status);

    Optional<WaitingList> findByIdUser(UUID idUser);


    @Query("SELECT w as waitingList, u.firstName as firstName, u.lastName as lastName " +
        "FROM WaitingList w JOIN User u ON w.idUser = u.id")
    List<WaitingListWithNames> findAllWithUserNames();

    @Query("SELECT w FROM WaitingList w WHERE w.category = :category AND w.governorate = :governorate AND w.delegation = :delegation AND w.rank > :rank ORDER BY w.rank ASC")
    List<WaitingList> findAllWithRankGreaterThanByCategoryAndGovernorateAndDelegation(
        @Param("category") Category category,
        @Param("governorate") Governorate governorate,
        @Param("delegation") String delegation,
        @Param("rank") int rank);

    @Query("SELECT w FROM WaitingList w WHERE w.category = :category AND w.governorate = :governorate AND w.municipality = :municipality AND w.rank > :rank ORDER BY w.rank ASC")
    List<WaitingList> findAllWithRankGreaterThanByCategoryAndGovernorateAndMunicipality(
        @Param("category") Category category,
        @Param("governorate") Governorate governorate,
        @Param("municipality") String municipality,
        @Param("rank") int rank);


    @Query("SELECT w FROM WaitingList w WHERE w.idUser = :idUser AND w.status = :status")
    Optional<WaitingList> findByIdUserAndStatus(@Param("idUser") UUID idUser, @Param("status") StatusWaitingList status);


}
