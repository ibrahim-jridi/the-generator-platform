package com.pfe.repository;

import com.pfe.domain.Frequency;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Frequency entity.
 */
@Repository
public interface IFrequencyRepository extends BaseRepository<Frequency, UUID>{
    List<Frequency> findByNotificationIdAndDeletedFalse(UUID id);

    @Modifying
    @Query("DELETE FROM Frequency f WHERE f.id IN :oldFrequencies")
    void deleteOldNotificationFrequenciesByID(@Param("oldFrequencies") Set<UUID> oldFrequencies);
}
