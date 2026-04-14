package com.pfe.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class FileDto {
    private UUID id;
    private String name;
    private String path;
    private String createdBy;
    private Date creationDate;
    private String version;
    private Boolean active;
    private String extension;
    private String type;
    private String size;
    private Boolean inWorkflow;
    private UUID userId;
    private FolderDto folder;

    public FileDto() {
    }

    public FileDto(UUID id, String name, String path, String createdBy, Date creationDate, String version, Boolean active, String extension, String type, String size, Boolean inWorkflow, UUID userId, FolderDto folder) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.version = version;
        this.active = active;
        this.extension = extension;
        this.type = type;
        this.size = size;
        this.inWorkflow = inWorkflow;
        this.userId = userId;
        this.folder = folder;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getInWorkflow() {
        return inWorkflow;
    }

    public void setInWorkflow(Boolean inWorkflow) {
        this.inWorkflow = inWorkflow;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public FolderDto getFolder() {
        return folder;
    }

    public void setFolder(FolderDto folder) {
        this.folder = folder;
    }
}
