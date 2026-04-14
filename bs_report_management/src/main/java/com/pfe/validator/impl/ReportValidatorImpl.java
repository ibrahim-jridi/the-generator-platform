package com.pfe.validator.impl;

import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.validator.IReportValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("reportValidator")
public class ReportValidatorImpl implements IReportValidator {

    private final String INVALID_EXTENSION = "Le fichier doit avoir une extension .jrxml";

    private final String TYPE_REQUIRED = "Le nom de template est obligatoire";


    @Override
    public void beforeSave(MultipartFile file, String type) {
        List<Reason> reasons = new ArrayList<>();
        validateBucketName(reasons, type);
        validateFileExtension(reasons, file);
        if (!reasons.isEmpty()) {
            throw new ValidationException(
                reasons.stream()
                    .map(reason -> String.valueOf(reason.code))
                    .collect(Collectors.joining(", "))
            );
        }

    }

    public void validateBucketName(List<Reason> reasons, String type) {
        if (type == null) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.TYPE_REQUIRED, TYPE_REQUIRED));
        }
    }

    public void validateFileExtension(List<Reason> reasons, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jrxml")) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.INVALID_EXTENSION, INVALID_EXTENSION));
        }
    }

    @Override
    public void beforeSave(ReportTemplateDTO createRequest) {

    }
}
