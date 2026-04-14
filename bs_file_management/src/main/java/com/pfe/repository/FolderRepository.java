package com.pfe.repository;

import com.pfe.domain.Folder;
import com.pfe.dto.FolderDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Folder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FolderRepository extends BaseRepository<Folder, UUID> {

  FolderDto findByPath(String path);

  Optional<Folder> findByName(String name);

  Optional<Folder> findByFolderMinioId(UUID folderId);

  Optional<Folder> findByUserId(UUID userId);

  List<Folder> findByParentFolderMinioId(UUID id);

  @Query("SELECT f.folderMinioId FROM Folder f WHERE f.userId = :userId")
  UUID findFolderMinioIdByUserId(@Param("userId") UUID userId);


}
