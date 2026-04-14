package com.pfe.service.dto.request;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ApplicationUsersResponse implements Serializable {
    private static final long serialVersionUID = -5630183928012566549L;

    private List<String> names;


    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationUsersResponse that = (ApplicationUsersResponse) o;
        return Objects.equals(names, that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hash(names);
    }

    @Override
    public String toString() {
        return "ApplicationUsersResponse{" +
            ", names=" + names +
            '}';
    }


}

