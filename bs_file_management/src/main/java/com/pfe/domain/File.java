package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A File.
 */
@Entity
@Table(name = "bs_file")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File extends AbstractAuditingEntity<UUID> implements Serializable {

  private static final long serialVersionUID = -8500924087847653291L;

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;
  @NotNull
  @Column(name = "file_minio_id", nullable = false)
  private UUID fileMinioId;
  @NotNull
  @Column(name = "name", nullable = false, length = 255)
  @Size(min = 1, max = 255)
  private String name;

  @NotNull
  @Column(name = "path", nullable = false, length = 255)
  @Size(min = 1, max = 255)
  private String path;

  @NotNull
  @Column(name = "extension", nullable = false, length = 25)
  @Size(min = 1, max = 25)
  private String extension;

  @NotNull
  @Column(name = "type", nullable = false, length = 25)
  @Size(min = 1, max = 25)
  private String type;

  @NotNull
  @Column(name = "size", nullable = false, length = 25)
  @Size(min = 1, max = 25)
  private String size;

  @NotNull
  @Column(name = "in_workflow", nullable = false)
  private Boolean inWorkflow;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = {"files", "parent"}, allowSetters = true)
  private Folder folder;

  @Override
  public UUID getId() {
    return this.id;
  }

  public File id(UUID id) {
    this.setId(id);
    return this;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public File name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return this.path;
  }

  public File path(String path) {
    this.setPath(path);
    return this;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getExtension() {
    return this.extension;
  }

  public File extension(String extension) {
    this.setExtension(extension);
    return this;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public String getType() {
    return this.type;
  }

  public File type(String type) {
    this.setType(type);
    return this;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSize() {
    return this.size;
  }

  public File size(String size) {
    this.setSize(size);
    return this;
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

  public Folder getFolder() {
    return this.folder;
  }

  public void setFolder(Folder folder) {
    this.folder = folder;
  }

  public File folder(Folder folder) {
    this.setFolder(folder);
    return this;
  }

  public UUID getFileMinioId() {
    return this.fileMinioId;
  }

  public void setFileMinioId(UUID fileMinioId) {
    this.fileMinioId = fileMinioId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    File file = (File) o;
    return Objects.equals(this.id, file.id);
  }

  @Override
  public int hashCode() {
    return this.id != null ? this.id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "File{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", path='" + getPath() + "'" +
        ", extension='" + getExtension() + "'" +
        ", type='" + getType() + "'" +
        ", size='" + getSize() + "'" +
        ", inWorkflow='" + getInWorkflow() + "'" +
        ", fileMinioId='" + getFileMinioId() + "'" +
        "}";
  }
}
