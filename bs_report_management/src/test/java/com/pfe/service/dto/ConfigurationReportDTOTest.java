package com.pfe.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConfigurationReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationReportDTO.class);
        ConfigurationReportDTO configurationReportDTO1 = new ConfigurationReportDTO();
        configurationReportDTO1.setId(UUID.randomUUID());
        ConfigurationReportDTO configurationReportDTO2 = new ConfigurationReportDTO();
        assertThat(configurationReportDTO1).isNotEqualTo(configurationReportDTO2);
        configurationReportDTO2.setId(configurationReportDTO1.getId());
        assertThat(configurationReportDTO1).isEqualTo(configurationReportDTO2);
        configurationReportDTO2.setId(UUID.randomUUID());
        assertThat(configurationReportDTO1).isNotEqualTo(configurationReportDTO2);
        configurationReportDTO1.setId(null);
        assertThat(configurationReportDTO1).isNotEqualTo(configurationReportDTO2);
    }
}
