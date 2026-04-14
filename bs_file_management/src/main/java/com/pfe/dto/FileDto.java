package com.pfe.dto;

import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto implements Serializable {

  private static final long serialVersionUID = -2548284035856965421L;
  private UUID id;
  private UUID fileMinioId;
  @Size(min = 1, max = 255)
  private String name;
  @Size(min = 1, max = 255)
  private String path;
  @Size(min = 1, max = 25)
  private String extension;
  @Size(min = 1, max = 25)
  private String type;
  @Size(min = 1, max = 25)
  private String size;
  private Boolean inWorkflow;
  private FolderDto folder;
  private UUID createdBy;
  private Instant createdDate;
  private UUID lastModifiedBy;
  private Instant lastModifiedDate;
  private int version;
  private Boolean deleted;

  public FileDto() {
  }

  public FileDto(UUID id, UUID fileMinioId, String name, String path, String extension, String type,
      String size, Boolean inWorkflow, FolderDto folder, UUID createdBy, Instant createdDate,
      UUID lastModifiedBy, Instant lastModifiedDate, int version, Boolean deleted) {
    this.id = id;
    this.fileMinioId = fileMinioId;
    this.name = name;
    this.path = path;
    this.extension = extension;
    this.type = type;
    this.size = size;
    this.inWorkflow = inWorkflow;
    this.folder = folder;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.lastModifiedBy = lastModifiedBy;
    this.lastModifiedDate = lastModifiedDate;
    this.version = version;
    this.deleted = deleted;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public UUID getFileMinioId() {
    return this.fileMinioId;
  }

  public void setFileMinioId(UUID fileMinioId) {
    this.fileMinioId = fileMinioId;
  }

  public String getExtension() {
    return this.extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSize() {
    return this.size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public Boolean getInWorkflow() {
    return this.inWorkflow;
  }

  public void setInWorkflow(Boolean inWorkflow) {
    this.inWorkflow = inWorkflow;
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

  public UUID getLastModifiedBy() {
    return this.lastModifiedBy;
  }

  public void setLastModifiedBy(UUID lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Instant getLastModifiedDate() {
    return this.lastModifiedDate;
  }

  public void setLastModifiedDate(Instant lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public FolderDto getFolder() {
    return this.folder;
  }

  public void setFolder(FolderDto folder) {
    this.folder = folder;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FileDto fileDto = (FileDto) o;

    return Objects.equals(this.id, fileDto.id);
  }

  @Override
  public int hashCode() {
    return this.id != null ? this.id.hashCode() : 0;
  }

}
