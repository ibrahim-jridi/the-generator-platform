package com.pfe.domain;

import java.util.UUID;

public class EventTestSamples {

    public static Event getEventSample1() {
        return new Event()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .assign("assign1")
            .display("display1")
            .eventType("eventType1")
            .message("message1")
            .notificationType("notificationType1")
            .subject("subject1")
            .recipiantId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Event getEventSample2() {
        return new Event()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .assign("assign2")
            .display("display2")
            .eventType("eventType2")
            .message("message2")
            .notificationType("notificationType2")
            .subject("subject2")
            .recipiantId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(UUID.randomUUID())
            .assign(UUID.randomUUID().toString())
            .display(UUID.randomUUID().toString())
            .eventType(UUID.randomUUID().toString())
            .message(UUID.randomUUID().toString())
            .notificationType(UUID.randomUUID().toString())
            .subject(UUID.randomUUID().toString())
            .recipiantId(UUID.randomUUID());
    }
}
