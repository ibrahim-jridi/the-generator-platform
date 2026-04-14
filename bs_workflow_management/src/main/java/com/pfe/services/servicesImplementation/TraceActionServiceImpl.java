package com.pfe.services.servicesImplementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.domain.AbstractAuditingEntity;
import com.pfe.domain.enumeration.OperationType;
import com.pfe.dto.ActivityAuditDTO;
import com.pfe.security.SpringSecurityAuditorAware;
import com.pfe.services.TraceActionService;
import jakarta.persistence.Table;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service
public class TraceActionServiceImpl implements TraceActionService {

    private final Logger log = LoggerFactory.getLogger(TraceActionServiceImpl.class);
    private final ObjectMapper objectMapper;
    private static final String DISPLAY_STRING_PATTERN = "Attribute: %s, Old: %s, New: %s";
    private static final String NEW_LINE = "\n";
    private static final String ID_SUFFIX = "_id";
    private static final String GET_ID = "getId";

    private static final List<Class<?>> ENTITIES_TO_IGNORE_FOR_ACTIONS = List.of();

    private final SpringSecurityAuditorAware springSecurityAuditorAware;

    public TraceActionServiceImpl(ObjectMapper objectMapper, SpringSecurityAuditorAware springSecurityAuditorAware) {
        this.objectMapper = objectMapper;
        this.springSecurityAuditorAware = springSecurityAuditorAware;
    }


    @Override
    public void insertTraceActionJPA(Object savedEntity, Object id) {
        log.debug("Request to insert TraceAction in JPA : {}", savedEntity.toString());
        saveTrace(savedEntity, id, OperationType.CREATE, null);
    }


    @Override
    public void updateTraceActionJPA(Object savedEntity, Object id, Object[] currentState,
                                     Object[] previousState, String[] propertyNames) {
        log.debug("Request to update TraceAction in JPA : {}", savedEntity.toString());
        var details = filterOutAuditFields(currentState, previousState, propertyNames);
        saveTrace(savedEntity, id, OperationType.UPDATE, details);

    }


    @Override
    public void deleteTraceActionJPA(Object savedEntity, Object id, String details) {
        if(savedEntity != null) {
            log.debug("Request to delete TraceAction in JPA : {}", savedEntity);
            saveTrace(savedEntity, id, OperationType.DELETE, details);
        }
    }


    private void processInnerEntitiyDifferences(
        List<String> listDifferences,
        Object current,
        Object previous) {

        if (current != null) {
            try {
                var tableName = getTableName(current);

                var getIdMethodPrevious = previous.getClass().getMethod(GET_ID);
                var idValuePrevious = getIdMethodPrevious.invoke(previous);

                var getIdMethodCurrent = current.getClass().getMethod(GET_ID);
                var idValueCurrent = getIdMethodCurrent.invoke(current);

                listDifferences.add(
                    getDisplayString(tableName + ID_SUFFIX, idValuePrevious, idValueCurrent));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void processAttributeDifferences(
        String propertyName,
        List<String> auditFields,
        List<String> listDifferences,
        Object current,
        Object previous) {

        if (!auditFields.contains(propertyName)) {
            listDifferences.add(getDisplayString(propertyName, previous, current));
        }
    }


    private String getDisplayString(String propertyName, Object previous, Object current) {
        try {
            String previousJson = (previous != null) ? objectMapper.writeValueAsString(previous) : "null";
            String currentJson = (current != null) ? objectMapper.writeValueAsString(current) : "null";

            return String.format(DISPLAY_STRING_PATTERN, propertyName, previousJson, currentJson);
        } catch (Exception e) {
            throw new RuntimeException("Error formatting display string: " + e.getMessage(), e);
        }
    }


    private List<String> getAuditFieldNames() {

        return FieldUtils.getAllFieldsList(AbstractAuditingEntity.class)
            .stream()
            .map(Field::getName)
            .toList();
    }

    private ActivityAuditDTO traceActionJPABuilder(Object savedEntity, Object id,
                                                   OperationType traceActionEnum, String details) {
        var entity = getTableName(savedEntity);
        ActivityAuditDTO activityAudit = new ActivityAuditDTO();
        activityAudit.setAction(traceActionEnum.name());
        activityAudit.setEntity(entity);
        activityAudit.setEntityId((UUID) id);
        activityAudit.setDetails(details);
        activityAudit.setSubmittedBy(this.getCurrentUserId());
        activityAudit.setSubmissionDate(Instant.now());
        return activityAudit;
    }


    private boolean shouldTraceAction(Object savedEntity) {
        return ENTITIES_TO_IGNORE_FOR_ACTIONS.stream()
            .noneMatch(aClass -> aClass.equals(savedEntity.getClass()));
    }

    private String getTableName(Object entity) {
        if (entity == null) {
            return null;
        }
        Class<?> entityClass = entity.getClass();

        if (HibernateProxy.class.isAssignableFrom(entityClass)) {
            entityClass = entityClass.getSuperclass();
        }
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        } else {
            new IllegalArgumentException("Entity " + entityClass.getName() + " does not have @Table annotation");
            return null;
        }
    }


    private void saveTrace(Object savedEntity, Object id, OperationType traceActionEnum,
                           String details) {

        if (shouldTraceAction(savedEntity)) {
            log.info("traceAction send successfully : {}", traceActionJPABuilder(savedEntity, id, traceActionEnum, details));
        }
    }

    private String filterOutAuditFields(
        Object[] currentState,
        Object[] previousState,
        String[] propertyNames) {

        var auditFields = getAuditFieldNames();
        var listDifferences = new ArrayList<String>();

        for (int i = 0; i < currentState.length; i++) {
            var propertyName = propertyNames[i];
            var current = currentState[i];
            var previous = previousState[i];

            if (current instanceof AbstractAuditingEntity<?>) {
                processInnerEntitiyDifferences(listDifferences, current, previous);
            } else {
                processAttributeDifferences(propertyName, auditFields, listDifferences, current,
                    previous);
            }
        }
        return String.join(NEW_LINE, listDifferences);
    }

    public UUID getCurrentUserId() {
        return UUID.fromString(springSecurityAuditorAware.getCurrentAuditor().orElseThrow(
            () -> new RuntimeException("No auditor (user ID) found")));
    }
}
