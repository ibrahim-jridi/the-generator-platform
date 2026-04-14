package com.pfe.config;


import com.pfe.aop.logging.JpaActionsAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAspectConfiguration {

    @Bean
    @Profile("!testdev")
    public JpaActionsAspect jpaActionsAspect(Environment env) {
        return new JpaActionsAspect(env);
    }


}
