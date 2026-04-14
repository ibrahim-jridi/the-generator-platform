package com.pfe.repository;

import com.pfe.domain.Authority;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityRepository extends BaseRepository<Authority, UUID> {

    @Query(value = "SELECT * FROM bs_authority WHERE first_insert = '1'", nativeQuery = true)
    List<Authority> findAuthoritiesWithFirstInsert();

    @Modifying
    @Query(value = "UPDATE bs_authority SET first_insert = CAST(:firstInsert AS BIT) WHERE id = :id", nativeQuery = true)
    void updateFirstInsert(@Param("id") UUID id, @Param("firstInsert") boolean firstInsert);

    Set<Authority> findAllByIdInAndDeletedFalse(Set<UUID> collect);
}
