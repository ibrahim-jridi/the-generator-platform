package com.pfe.domain;

import java.util.UUID;

public class NotificationPushedTestSamples {

    public static NotificationPushed getNotificationPushedSample1() {
        return new NotificationPushed()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .message("message1")
            .recipientId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static NotificationPushed getNotificationPushedSample2() {
        return new NotificationPushed()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .message("message2")
            .recipientId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static NotificationPushed getNotificationPushedRandomSampleGenerator() {
        return new NotificationPushed()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString())
            .recipientId(UUID.randomUUID());
    }
}
