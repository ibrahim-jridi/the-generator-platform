package com.pfe.validator;


import com.pfe.service.dto.ReportTemplateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IReportValidator extends IGenericValidator<ReportTemplateDTO, ReportTemplateDTO> {
    public void beforeSave(MultipartFile file, String type);
}
