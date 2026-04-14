package com.pfe.delegate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.dto.TaskDTO;
import com.pfe.services.ProcessEngineService;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidateTaskBySystemDelegate implements JavaDelegate {
    private FixedValue timeoutDuration;
    private FixedValue taskName;
    private final ProcessEngineService processEngineService;
    private static final Logger log = LoggerFactory.getLogger(ValidateTaskBySystemDelegate.class);


    public ValidateTaskBySystemDelegate(ProcessEngineService processEngineService) {
        this.processEngineService = processEngineService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Starting task timeout check");

        if (timeoutDuration != null && taskName != null) {
            String processInstanceId = delegateExecution.getProcessInstanceId();

            TaskDTO taskDTO = processEngineService.getCurrentTaskByProcessInstanceId(
                processInstanceId);
            if (taskDTO != null) {
                ZonedDateTime creationDate = taskDTO.getCreateTime();
                int timeoutInMinutes = Integer.parseInt(timeoutDuration.getValue(delegateExecution).toString());
                String taskNameString = taskName.getValue(delegateExecution).toString();

                if (taskDTO.getStatus() == null && Objects.equals(taskNameString, taskDTO.getName())) {

                    Instant now = Instant.now();
                    Instant taskCreationInstant = creationDate.toInstant();
                    Duration durationSinceCreation = Duration.between(taskCreationInstant, now);

                    if (durationSinceCreation.toMinutes() >= timeoutInMinutes) {

                        Map<String, Object> variables = new HashMap<>();
                        String fieldsJson = taskDTO.getForm().getFields();
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> fields = objectMapper.readValue(fieldsJson, new TypeReference<Map<String, Object>>(){});

                        extractKeysFromComponents((List<Map<String, Object>>) fields.get("components"), variables);
                        variables.put("submit", true);
                        processEngineService.validateAndMoveToNextTask(taskDTO.getId(), variables);

                    }
                    else {
                        log.info("Timeout not yet reached");
                    }
                }
                else {
                    log.debug("TaskName {} not found.",taskName);
                }
            }
            else {
                log.info("Task not found.");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Check timeoutDuration and taskName they can't be null");

        }
    }

    private void extractKeysFromComponents(List<Map<String, Object>> components, Map<String, Object> variables) {
        for (Map<String, Object> component : components) {

            if (component.containsKey("key") && Boolean.TRUE.equals(component.get("input"))) {
                String key = (String) component.get("key");
                variables.put(key, "");
            }

            if (component.containsKey("components")) {
                extractKeysFromComponents((List<Map<String, Object>>) component.get("components"), variables);
            }
        }
    }
}
