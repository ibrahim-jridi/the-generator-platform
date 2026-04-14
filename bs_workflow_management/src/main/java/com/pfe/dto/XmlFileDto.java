package com.pfe.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class XmlFileDto implements Serializable {

    private static final long serialVersionUID = 1262045920508594220L;
    private UUID id;
    private String name;
    private String xml;

    public XmlFileDto() {
    }

    public XmlFileDto(UUID id, String name, String xml) {
        this.id = id;
        this.name = name;
        this.xml = xml;
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

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XmlFileDto that = (XmlFileDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "XmlFileDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", xml='" + xml + '\'' +
            '}';
    }
}
