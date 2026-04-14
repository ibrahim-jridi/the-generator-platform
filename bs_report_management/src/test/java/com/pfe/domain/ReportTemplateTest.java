package com.pfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReportTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportTemplate.class);
        ReportTemplate reportTemplate1 = ReportTemplateTestSamples.getReportTemplateSample1();
        ReportTemplate reportTemplate2 = new ReportTemplate();
        assertThat(reportTemplate1).isNotEqualTo(reportTemplate2);

        reportTemplate2.setId(reportTemplate1.getId());
        assertThat(reportTemplate1).isEqualTo(reportTemplate2);

        reportTemplate2 = ReportTemplateTestSamples.getReportTemplateSample2();
        assertThat(reportTemplate1).isNotEqualTo(reportTemplate2);
    }
}
