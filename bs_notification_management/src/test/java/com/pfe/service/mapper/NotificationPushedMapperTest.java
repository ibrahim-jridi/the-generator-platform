package com.pfe.service.mapper;

import static com.pfe.domain.NotificationPushedAsserts.*;
import static com.pfe.domain.NotificationPushedTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationPushedMapperTest {

    private NotificationPushedMapper notificationPushedMapper;

    @BeforeEach
    void setUp() {
        notificationPushedMapper = new NotificationPushedMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationPushedSample1();
        var actual = notificationPushedMapper.toEntity(notificationPushedMapper.toDto(expected));
        assertNotificationPushedAllPropertiesEquals(expected, actual);
    }
}
