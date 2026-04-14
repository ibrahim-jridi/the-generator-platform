package com.pfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventNotification.class);
        EventNotification eventNotification1 = EventNotificationTestSamples.getEventNotificationSample1();
        EventNotification eventNotification2 = new EventNotification();
        assertThat(eventNotification1).isNotEqualTo(eventNotification2);

        eventNotification2.setId(eventNotification1.getId());
        assertThat(eventNotification1).isEqualTo(eventNotification2);

        eventNotification2 = EventNotificationTestSamples.getEventNotificationSample2();
        assertThat(eventNotification1).isNotEqualTo(eventNotification2);
    }

    @Test
    void notificationTest() throws Exception {
        EventNotification eventNotification = EventNotificationTestSamples.getEventNotificationRandomSampleGenerator();
        Notification notificationBack = NotificationTestSamples.getNotificationRandomSampleGenerator();

        eventNotification.setNotification(notificationBack);
        assertThat(eventNotification.getNotification()).isEqualTo(notificationBack);

        eventNotification.notification(null);
        assertThat(eventNotification.getNotification()).isNull();
    }
}
