package com.pfe.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pfe.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReportTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReportTemplateDTO.class);
        ReportTemplateDTO reportTemplateDTO1 = new ReportTemplateDTO();
        reportTemplateDTO1.setId(UUID.randomUUID());
        ReportTemplateDTO reportTemplateDTO2 = new ReportTemplateDTO();
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
        reportTemplateDTO2.setId(reportTemplateDTO1.getId());
        assertThat(reportTemplateDTO1).isEqualTo(reportTemplateDTO2);
        reportTemplateDTO2.setId(UUID.randomUUID());
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
        reportTemplateDTO1.setId(null);
        assertThat(reportTemplateDTO1).isNotEqualTo(reportTemplateDTO2);
    }
}
