package com.pfe.service;

public interface TraceActionService {

    void insertTraceActionJPA(Object savedEntity, Object id);

    void updateTraceActionJPA(Object savedEntity, Object id, Object[] currentState,
                              Object[] previousState, String[] propertyNames);

    void deleteTraceActionJPA(Object savedEntity, Object id, String details);

}
