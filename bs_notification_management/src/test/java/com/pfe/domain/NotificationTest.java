package com.pfe.domain;

import static com.pfe.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void hashCodeVerifier() throws Exception {
        Notification notification = new Notification();
        assertThat(notification.hashCode()).isZero();

        Notification notification1 = getNotificationSample1();
        notification.setId(notification1.getId());
        assertThat(notification).hasSameHashCodeAs(notification1);
    }
}
