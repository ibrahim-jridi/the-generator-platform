package com.pfe.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EventNotificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventNotificationDTO.class);
        EventNotificationDTO eventNotificationDTO1 = new EventNotificationDTO();
        eventNotificationDTO1.setId(UUID.randomUUID());
        EventNotificationDTO eventNotificationDTO2 = new EventNotificationDTO();
        assertThat(eventNotificationDTO1).isNotEqualTo(eventNotificationDTO2);
        eventNotificationDTO2.setId(eventNotificationDTO1.getId());
        assertThat(eventNotificationDTO1).isEqualTo(eventNotificationDTO2);
        eventNotificationDTO2.setId(UUID.randomUUID());
        assertThat(eventNotificationDTO1).isNotEqualTo(eventNotificationDTO2);
        eventNotificationDTO1.setId(null);
        assertThat(eventNotificationDTO1).isNotEqualTo(eventNotificationDTO2);
    }
}
