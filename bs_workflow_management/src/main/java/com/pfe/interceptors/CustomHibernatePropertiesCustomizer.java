package com.pfe.interceptors;


import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;


@Configuration
public class CustomHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

    private final CustomHibernateIntegrator customHibernateIntegrator;

    private static final String HIBERNATE_INTEGRATOR_PROVIDER = "hibernate.integrator_provider";

    public CustomHibernatePropertiesCustomizer(CustomHibernateIntegrator customHibernateIntegrator) {
        this.customHibernateIntegrator = customHibernateIntegrator;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {

        hibernateProperties.put(HIBERNATE_INTEGRATOR_PROVIDER,
            (IntegratorProvider) () -> Collections.singletonList(
                customHibernateIntegrator));
    }
}
