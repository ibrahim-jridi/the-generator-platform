package com.pfe.service.mapper;

import static com.pfe.domain.ConfigurationReportAsserts.*;
import static com.pfe.domain.ConfigurationReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationReportMapperTest {

    private ConfigurationReportMapper configurationReportMapper;

    @BeforeEach
    void setUp() {
        configurationReportMapper = new ConfigurationReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConfigurationReportSample1();
        var actual = configurationReportMapper.toEntity(configurationReportMapper.toDto(expected));
        assertConfigurationReportAllPropertiesEquals(expected, actual);
    }
}
