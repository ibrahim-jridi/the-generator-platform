package com.pfe.domain;

import static com.pfe.domain.ConfigurationReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigurationReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigurationReport.class);
        ConfigurationReport configurationReport1 = getConfigurationReportSample1();
        ConfigurationReport configurationReport2 = new ConfigurationReport();
        assertThat(configurationReport1).isNotEqualTo(configurationReport2);

        configurationReport2.setId(configurationReport1.getId());
        assertThat(configurationReport1).isEqualTo(configurationReport2);

        configurationReport2 = getConfigurationReportSample2();
        assertThat(configurationReport1).isNotEqualTo(configurationReport2);
    }
}
