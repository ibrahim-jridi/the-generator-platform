package com.pfe.repository;

import com.pfe.domain.File;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the File entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileRepository extends BaseRepository<File, UUID> {

}
