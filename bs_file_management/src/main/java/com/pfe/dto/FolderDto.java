package com.pfe.dto;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FolderDto implements Serializable {

    private static final long serialVersionUID = -1925690075190013870L;
    private UUID id;
    private UUID folderMinioId;
    @Size(min = 1, max = 255)
    private String name;
    @Size(min = 1, max = 255)
    private String path;
    private Long recurrence;
    private UUID userId;
    private List<FileDto> files;
    private FolderDto parent;
    private List<FolderDto> folders;
    private UUID createdBy;
    private Instant createdDate;
    private UUID lastModifiedBy;
    private Instant lastModifiedDate;
    private int version;
    private Boolean deleted;

    public FolderDto(UUID id, UUID folderMinioId, String name, String path, Long recurrence,
                     UUID userId, List<FileDto> files, FolderDto parent, List<FolderDto> folders, UUID createdBy,
                     Instant createdDate, UUID lastModifiedBy, Instant lastModifiedDate, int version,
                     Boolean deleted) {
        this.id = id;
        this.folderMinioId = folderMinioId;
        this.name = name;
        this.path = path;
        this.recurrence = recurrence;
        this.userId = userId;
        this.files = files;
        this.parent = parent;
        this.folders = folders;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedBy = lastModifiedBy;

        this.version = version;
        this.deleted = deleted;
    }

    public FolderDto() {
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFolderMinioId() {
        return this.folderMinioId;
    }

    public void setFolderMinioId(UUID folderMinioId) {
        this.folderMinioId = folderMinioId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getRecurrence() {
        return this.recurrence;
    }

    public void setRecurrence(Long recurrence) {
        this.recurrence = recurrence;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }


    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<FileDto> getFiles() {
        return this.files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }

    public FolderDto getParent() {
        return this.parent;
    }

    public void setParent(FolderDto parent) {
        this.parent = parent;
    }

    public List<FolderDto> getFolders() {
        return this.folders;
    }

    public void setFolders(List<FolderDto> folders) {
        this.folders = folders;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public UUID getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FolderDto)) {
            return false;
        }

        FolderDto folderDto = (FolderDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, folderDto.id);
    }

    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

}
