package com.pfe;

import com.pfe.config.AsyncSyncConfiguration;
import com.pfe.config.EmbeddedSQL;
import com.pfe.config.JacksonConfiguration;
import com.pfe.web.rest.IntegrationTestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BsUserManagementApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@ContextConfiguration(classes = IntegrationTestConfig.class)
public @interface IntegrationTest {
}
