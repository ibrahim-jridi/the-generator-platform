package com.pfe.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A Folder.
 */
@Entity
@Table(name = "bs_folder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Folder extends AbstractAuditingEntity<UUID> implements Serializable {

  private static final long serialVersionUID = 3004633536184740060L;

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true)
  private UUID id;
  @NotNull
  @Column(name = "folder_minio_id", nullable = false)
  private UUID folderMinioId;
  @NotNull
  @Column(name = "name", nullable = false, length = 255)
  @Size(min = 1, max = 255)
  private String name;
  @NotNull
  @Column(name = "path", nullable = false, length = 255)
  @Size(min = 1, max = 255)
  private String path;

  @Column(name = "recurrence")
  private Long recurrence;

  @Column(name = "user_id")
  private UUID userId;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "folder")
  @JsonIgnoreProperties(value = {"folder"}, allowSetters = true)
  private Set<File> files = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = {"files", "parent"}, allowSetters = true)
  private Folder parent;

  @Override
  public UUID getId() {
    return this.id;
  }

  public Folder id(UUID id) {
    this.setId(id);
    return this;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public Folder name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return this.path;
  }

  public Folder path(String path) {
    this.setPath(path);
    return this;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Long getRecurrence() {
    return this.recurrence;
  }

  public Folder recurrence(Long recurrence) {
    this.setRecurrence(recurrence);
    return this;
  }

  public void setRecurrence(Long recurrence) {
    this.recurrence = recurrence;
  }

  public UUID getUserId() {
    return this.userId;
  }

  public Folder userId(UUID userId) {
    this.setUserId(userId);
    return this;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public Set<File> getFiles() {
    return this.files;
  }

  public void setFiles(Set<File> files) {
    if (this.files != null) {
      this.files.forEach(i -> i.setFolder(null));
    }
    if (files != null) {
      files.forEach(i -> i.setFolder(this));
    }
    this.files = files;
  }

  public Folder files(Set<File> files) {
    this.setFiles(files);
    return this;
  }

  public Folder addFiles(File file) {
    this.files.add(file);
    file.setFolder(this);
    return this;
  }

  public Folder removeFiles(File file) {
    this.files.remove(file);
    file.setFolder(null);
    return this;
  }

  public Folder getParent() {
    return this.parent;
  }

  public void setParent(Folder folder) {
    this.parent = folder;
  }

  public Folder parent(Folder folder) {
    this.setParent(folder);
    return this;
  }
  public UUID getFolderMinioId() {
    return this.folderMinioId;
  }

  public void setFolderMinioId(UUID folderMinioId) {
    this.folderMinioId = folderMinioId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Folder)) {
      return false;
    }

    Folder folder = (Folder) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, folder.id);
  }

  @Override
  public int hashCode() {
    return this.id != null ? this.id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Folder{" +
        "id=" + getId() +
        ", name='" + getName() + "'" +
        ", path='" + getPath() + "'" +
        ", recurrence=" + getRecurrence() +
        ", userId='" + getUserId() + "'" +
        ", folderMinioId='" + getFolderMinioId() + "'" +
        "}";
  }
}
