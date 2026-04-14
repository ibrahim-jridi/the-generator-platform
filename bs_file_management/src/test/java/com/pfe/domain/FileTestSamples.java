package com.pfe.domain;

import java.util.UUID;

public class FileTestSamples {

    public static File getFileSample1() {
        return new File()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .path("path1")
            .extension("extension1")
            .type("type1")
            .size("size1");
    }

    public static File getFileSample2() {
        return new File()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .path("path2")
            .extension("extension2")
            .type("type2")
            .size("size2");
    }

    public static File getFileRandomSampleGenerator() {
        return new File()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .extension(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .size(UUID.randomUUID().toString());
    }
}
