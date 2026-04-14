package com.pfe;

import com.pfe.config.AsyncSyncConfiguration;
import com.pfe.config.EmbeddedSQL;
import com.pfe.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pfe.web.rest.IntegrationTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BsWorkflowManagementApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@ContextConfiguration(classes = IntegrationTestConfig.class)
@EmbeddedSQL
public @interface IntegrationTest {
}
