package com.pfe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FolderDto {
    private UUID id;
    private String name;
    private String path;
    private Date creationDate;
    private Long recurrence;
    private UUID userId;
    private List<FileDto> files;
    private List<FolderDto> folders;
    private FolderDto parent;

    public FolderDto() {
    }

    public FolderDto(UUID id, String name, String path, Date creationDate, Long recurrence, UUID userId, List<FileDto> files, List<FolderDto> folders, FolderDto parent) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.creationDate = creationDate;
        this.recurrence = recurrence;
        this.userId = userId;
        this.files = files;
        this.folders = folders;
        this.parent = parent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Long recurrence) {
        this.recurrence = recurrence;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<FileDto> getFiles() {
        return files;
    }

    public void setFiles(List<FileDto> files) {
        this.files = files;
    }

    public List<FolderDto> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderDto> folders) {
        this.folders = folders;
    }

    public FolderDto getParent() {
        return parent;
    }

    public void setParent(FolderDto parent) {
        this.parent = parent;
    }
}
