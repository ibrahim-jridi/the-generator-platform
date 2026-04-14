package com.pfe.service.event;

import com.pfe.service.dto.UserDTO;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class DesignationDeletedEvent extends ApplicationEvent {
    private final UserDTO designatedUser;
    private final UUID designationId;

    public DesignationDeletedEvent(Object source, UserDTO designatedUser, UUID designationId) {
        super(source);
        this.designatedUser = designatedUser;
        this.designationId = designationId;
    }

    public UserDTO getDesignatedUser() {
        return designatedUser;
    }

    public UUID getDesignationId() {
        return designationId;
    }
}
