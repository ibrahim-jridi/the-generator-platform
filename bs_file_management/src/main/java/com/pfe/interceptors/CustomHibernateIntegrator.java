package com.pfe.interceptors;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.stereotype.Component;


@Component
public class CustomHibernateIntegrator implements Integrator {

    private final CustomPostInsertEventListener customPostInsertEventListener;
    private final CustomPostUpdateEventListener customPostUpdateEventListener;
    private final CustomPreDeleteEventListener customPreDeleteEventListener;

    public CustomHibernateIntegrator(CustomPostInsertEventListener customPostInsertEventListener, CustomPostUpdateEventListener customPostUpdateEventListener, CustomPreDeleteEventListener customPreDeleteEventListener) {
        this.customPostInsertEventListener = customPostInsertEventListener;
        this.customPostUpdateEventListener = customPostUpdateEventListener;
        this.customPreDeleteEventListener = customPreDeleteEventListener;
    }


    @Override
    public void integrate(Metadata metadata, BootstrapContext bootstrapContext,
                          SessionFactoryImplementor sessionFactory) {

        var eventListenerRegistry = sessionFactory.getServiceRegistry()
            .getService(EventListenerRegistry.class);

        eventListenerRegistry.appendListeners(EventType.POST_INSERT,
            customPostInsertEventListener);

        eventListenerRegistry.appendListeners(EventType.POST_UPDATE,
            customPostUpdateEventListener);

        eventListenerRegistry.appendListeners(EventType.PRE_DELETE,
            customPreDeleteEventListener);
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor,
                             SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        // Not needed
    }
}
