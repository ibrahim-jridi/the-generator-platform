package com.pfe.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FolderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Folder getFolderSample1() {
        return new Folder()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .path("path1")
            .recurrence(1L)
            .userId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Folder getFolderSample2() {
        return new Folder()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .path("path2")
            .recurrence(2L)
            .userId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Folder getFolderRandomSampleGenerator() {
        return new Folder()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .recurrence(longCount.incrementAndGet())
            .userId(UUID.randomUUID());
    }
}
