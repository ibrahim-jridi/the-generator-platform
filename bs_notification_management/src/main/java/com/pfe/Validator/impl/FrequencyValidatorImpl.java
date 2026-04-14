package com.pfe.Validator.impl;

import com.pfe.Validator.IFrequencyValidator;
import com.pfe.service.dto.FrequencyDTO;
import org.springframework.stereotype.Component;

@Component("frequencyValidator")
public class FrequencyValidatorImpl implements IFrequencyValidator {
    @Override
    public void beforeUpdate(FrequencyDTO updateRequest) {
    }

    @Override
    public void beforeSave(FrequencyDTO createRequest) {
    }


}
