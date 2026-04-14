//package com.pfe.config;
//
//import org.camunda.bpm.engine.ProcessEngine;
//import org.camunda.bpm.engine.ProcessEngineConfiguration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CamundaConfig {
//
//    @Value("${spring.datasource.driver-class-name}")
//    String driver;
//
//    @Value("${spring.datasource.password}")
//    String password;
//
//    @Value("${spring.datasource.username}")
//    String username;
//
//    @Value("${spring.datasource.url}")
//    String url;
//
//    @Bean
//    public ProcessEngine processEngine() {
//        return ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
//            .setJdbcUrl(url)
//            .setJdbcDriver(driver)
//            .setJdbcUsername(username)
//            .setJdbcPassword(password)
//            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
//            .setHistory(ProcessEngineConfiguration.HISTORY_FULL)
//            .buildProcessEngine();
//
//    }
//}
