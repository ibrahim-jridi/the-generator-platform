package com.pfe.validator;

import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.web.rest.errors.Reason;

import java.util.List;

public interface IConfigurationReportValidator extends IGenericValidator<ConfigurationReportDTO, ConfigurationReportDTO> {
    void beforeSave(ConfigurationReportDTO configurationReportDTO);

    void validateCompanyAddress(List<Reason> reasons, String address);

    void validateCompanyName(List<Reason> reasons, String name);

    void validateEmail(List<Reason> reasons, String email);

    void validatePostalCode(List<Reason> reasons, String postalCode);

    void validateFax(List<Reason> reasons, String fax);

    void validatePhone(List<Reason> reasons, String phone);
}
