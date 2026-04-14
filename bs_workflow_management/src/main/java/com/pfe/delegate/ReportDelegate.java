package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class ReportDelegate implements JavaDelegate {


    private static final Logger log = LoggerFactory.getLogger(ReportDelegate.class);



        @Override
        public void execute(DelegateExecution delegateExecution) throws Exception {

            ServiceTask serviceTask = (ServiceTask) delegateExecution.getBpmnModelElementInstance();
            String templateIdValue = serviceTask.getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "template");

            if (templateIdValue == null) {
                log.warn("camunda:template is not set in the execution context. Cannot proceed with ReportDelegate.");
                return;
            }

            log.info("Templates fetched with response: {}",UUID.fromString(templateIdValue) );
        }
    }


