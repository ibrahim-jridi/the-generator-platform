package com.pfe.aop.logging;

import com.pfe.service.TraceActionService;
import jakarta.validation.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.collection.spi.PersistentBag;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.IntStream;


@Aspect
@Component
public class JpaActionsAspect {
    @Autowired
    private TraceActionService traceActionService;

    private final Environment env;

    public JpaActionsAspect(Environment env) {
        this.env = env;
    }


    @Before(value = "execution(*  com.pfe.interceptors.CustomPostInsertEventListener.onPostInsert(..))")
    public void beforeSaveInterupt(JoinPoint joinPoint) {
        PostInsertEvent postInsertEvent = (PostInsertEvent) joinPoint.getArgs()[0];

        traceActionService.insertTraceActionJPA(postInsertEvent.getEntity(),
            postInsertEvent.getId());
    }

    @Before(value = "execution(* com.pfe.interceptors.CustomPostUpdateEventListener.onPostUpdate(..))")
    public void beforeUpdateInterupt(JoinPoint joinPoint) {

        PostUpdateEvent postUpdateEvent = (PostUpdateEvent) joinPoint.getArgs()[0];

        var currentState = filterArrayByIndexes(postUpdateEvent.getState(),
            postUpdateEvent.getDirtyProperties());

        var previousState = filterArrayByIndexes(postUpdateEvent.getOldState(),
            postUpdateEvent.getDirtyProperties());

        var propertyNames = filterArrayByIndexes(postUpdateEvent.getPersister().getPropertyNames(),
            postUpdateEvent.getDirtyProperties());

        var previousPersistentBagIndexes = findNPersistentBagsFromArray(previousState);

        currentState = filterArrayByIndexes(currentState,
            previousPersistentBagIndexes);

        previousState = filterArrayByIndexes(previousState,
            previousPersistentBagIndexes);

        propertyNames = filterArrayByIndexes(propertyNames,
            previousPersistentBagIndexes);

        var currentPersistentBagIndexes = findNPersistentBagsFromArray(currentState);

        currentState = filterArrayByIndexes(currentState,
            currentPersistentBagIndexes);

        previousState = filterArrayByIndexes(previousState,
            currentPersistentBagIndexes);

        propertyNames = filterArrayByIndexes(propertyNames,
            currentPersistentBagIndexes);

        traceActionService.updateTraceActionJPA(
            postUpdateEvent.getEntity(),
            postUpdateEvent.getId(),
            currentState,
            previousState,
            (String[]) propertyNames);
    }


    @Before(value = "execution(*  com.pfe.repository.BaseRepository.deleteById(..))")
    public void beforeDeleteInterupt(JoinPoint joinPoint) {
        // Retrieve the argument safely
        Object argument = joinPoint.getArgs()[0];
        Object target = joinPoint.getTarget();
        if (argument instanceof UUID && target instanceof JpaRepository<?,?>) {
            UUID entityId = (UUID) argument;
            JpaRepository repository = (JpaRepository) target;
            Object entity = repository.findById(entityId).orElse(null);
            traceActionService.deleteTraceActionJPA(entity
                , entityId, null);

        } else {
            // Handle unexpected argument type or log it
            System.err.println("Unexpected argument type for update: " + argument.getClass().getName());
        }
    }




    @AfterThrowing(
        pointcut = "execution(* com.pfe.service.impl.TraceActionServiceImpl.*(..))",
        throwing = "ex"
    )
    public void handleException(Throwable ex) {
        throw new ValidationException(
            "bs trace exception occurred: " + ex.getMessage());
    }

    private Object[] filterArrayByIndexes(Object[] array, int[] indexes) {
        return Arrays.stream(indexes)
            .filter(index -> index >= 0 && index < array.length)
            .mapToObj(index -> array[index])
            .toArray(size -> Arrays.copyOf(array, size));
    }

    private int[] findNPersistentBagsFromArray(Object[] array) {
        return IntStream.range(0, array.length)
            .filter(i -> !(array[i] instanceof PersistentBag<?>))
            .toArray();
    }

}
