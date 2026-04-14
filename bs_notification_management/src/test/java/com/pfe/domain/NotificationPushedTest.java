package com.pfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationPushedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationPushed.class);
        NotificationPushed notificationPushed1 = NotificationPushedTestSamples.getNotificationPushedSample1();
        NotificationPushed notificationPushed2 = new NotificationPushed();
        assertThat(notificationPushed1).isNotEqualTo(notificationPushed2);

        notificationPushed2.setId(notificationPushed1.getId());
        assertThat(notificationPushed1).isEqualTo(notificationPushed2);

        notificationPushed2 = NotificationPushedTestSamples.getNotificationPushedSample2();
        assertThat(notificationPushed1).isNotEqualTo(notificationPushed2);
    }
}
