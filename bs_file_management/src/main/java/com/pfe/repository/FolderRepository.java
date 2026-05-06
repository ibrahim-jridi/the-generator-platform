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

  Optional<Folder> findByPath(String path);

  // If you still need a method that returns FolderDto, use this approach:
  @Query("SELECT new com.pfe.dto.FolderDto(f.id, f.folderMinioId, f.name, f.path, f.recurrence, f.userId, f.parent, f.createdBy, f.createdDate, f.lastModifiedBy, f.lastModifiedDate, f.version, f.deleted) FROM Folder f WHERE f.path = :path")
  Optional<FolderDto> findFolderDtoByPath(@Param("path") String path);

  Optional<Folder> findByName(String name);

  Optional<Folder> findByFolderMinioId(UUID folderId);

  Optional<Folder> findByUserId(UUID userId);

  List<Folder> findByParentFolderMinioId(UUID id);

  @Query("SELECT f.folderMinioId FROM Folder f WHERE f.userId = :userId")
  UUID findFolderMinioIdByUserId(@Param("userId") UUID userId);

  List<Folder> findByParent(Folder parent);

}
