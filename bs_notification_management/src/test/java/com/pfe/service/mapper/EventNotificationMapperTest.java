package com.pfe.service.mapper;

import static com.pfe.domain.EventNotificationAsserts.*;
import static com.pfe.domain.EventNotificationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventNotificationMapperTest {

    private EventNotificationMapper eventNotificationMapper;

    @BeforeEach
    void setUp() {
        eventNotificationMapper = new EventNotificationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEventNotificationSample1();
        var actual = eventNotificationMapper.toEntity(eventNotificationMapper.toDto(expected));
        assertEventNotificationAllPropertiesEquals(expected, actual);
    }
}
