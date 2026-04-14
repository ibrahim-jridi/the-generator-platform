package com.pfe.Validator;

public interface IGenericValidator<C,U> {
    void beforeUpdate(U updateRequest);
    void beforeSave(C createRequest);
}
