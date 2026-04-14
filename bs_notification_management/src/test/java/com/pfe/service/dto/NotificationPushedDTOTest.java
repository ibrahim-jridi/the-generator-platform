package com.pfe.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class NotificationPushedDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationPushedDTO.class);
        NotificationPushedDTO notificationPushedDTO1 = new NotificationPushedDTO();
        notificationPushedDTO1.setId(UUID.randomUUID());
        NotificationPushedDTO notificationPushedDTO2 = new NotificationPushedDTO();
        assertThat(notificationPushedDTO1).isNotEqualTo(notificationPushedDTO2);
        notificationPushedDTO2.setId(notificationPushedDTO1.getId());
        assertThat(notificationPushedDTO1).isEqualTo(notificationPushedDTO2);
        notificationPushedDTO2.setId(UUID.randomUUID());
        assertThat(notificationPushedDTO1).isNotEqualTo(notificationPushedDTO2);
        notificationPushedDTO1.setId(null);
        assertThat(notificationPushedDTO1).isNotEqualTo(notificationPushedDTO2);
    }
}
