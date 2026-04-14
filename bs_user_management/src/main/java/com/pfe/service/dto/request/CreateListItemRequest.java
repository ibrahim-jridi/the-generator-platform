package com.pfe.service.dto.request;

import java.io.Serializable;
import java.util.Objects;

public class CreateListItemRequest implements Serializable {
    private static final long serialVersionUID = -3240807310511392277L;


    private String id;
    private String label;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateListItemRequest listItem = (CreateListItemRequest) o;
        return Objects.equals(id, listItem.id)
            && Objects.equals(id, listItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
    @Override
    public String toString() {
        return "CreateListItemRequest{" +
            "id='" + id + '\'' +
            ", label='" + label + '\'' +
            '}';
    }
}
