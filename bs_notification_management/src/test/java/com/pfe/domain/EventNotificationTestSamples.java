package com.pfe.domain;

import java.util.UUID;

public class EventNotificationTestSamples {

    public static EventNotification getEventNotificationSample1() {
        return new EventNotification().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).taskId("taskId1");
    }

    public static EventNotification getEventNotificationSample2() {
        return new EventNotification().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).taskId("taskId2");
    }

    public static EventNotification getEventNotificationRandomSampleGenerator() {
        return new EventNotification().id(UUID.randomUUID()).taskId(UUID.randomUUID().toString());
    }
}
