package com.pfe.validator;

public interface IGenericValidator<C,U> {
    void beforeSave(C createRequest);
}
