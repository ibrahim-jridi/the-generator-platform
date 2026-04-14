package com.pfe.delegate;

import com.pfe.dto.TaskDTO;
import com.pfe.feignServices.NotificationService;
import com.pfe.services.ProcessEngineService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.SendTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StartAndStopNotificationDelegate implements JavaDelegate {
    private final NotificationService notificationService;
    private final ProcessEngineService processEngineService;

    private static final Logger log = LoggerFactory.getLogger(StartAndStopNotificationDelegate.class);

    public StartAndStopNotificationDelegate(NotificationService notificationService, ProcessEngineService processEngineService) {
        this.notificationService = notificationService;
        this.processEngineService = processEngineService;
    }


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();
        SendTask sendTask = (SendTask) delegateExecution.getBpmnModelElementInstance();

        String startCheckValue = sendTask.getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "startCheck");
        boolean startCheck = Boolean.parseBoolean(startCheckValue);
        String stopCheckValue = sendTask.getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "stopCheck");
        boolean stopCheck = Boolean.parseBoolean(stopCheckValue);
        String notifIdStr = sendTask.getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "notificationList");
        UUID notifId = UUID.fromString(notifIdStr);
        String processInstanceId = delegateExecution.getProcessInstanceId();

        TaskDTO taskDTO = processEngineService.getCurrentTaskByProcessInstanceId(
            processInstanceId);
        String variableNotif = sendTask.getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "variableNotif");
        Map<String, String> notificationEntry = new HashMap<>();
        if (variableNotif != null && !variableNotif.isEmpty()) {
            notificationEntry.put("variableNotif", variableNotif);
            notificationEntry.put("taskId", taskDTO.getId());
        }
        if (startCheck && notifIdStr != null) {
            delegateExecution.setVariable("notif_" + variableNotif, taskDTO.getId());
            notificationService.startNotification(notifId, taskDTO.getId());
        } else if (stopCheck && notifIdStr != null) {
            if (variableNotif.equals(notificationEntry.get("variableNotif"))) {
                String matchedTaskId = (String) delegateExecution.getVariable("notif_" + variableNotif); // e.g., notif_Test
                if (matchedTaskId != null) {
                    notificationService.stopNotification(notifId, matchedTaskId);
                } else {
                    log.warn("No matching taskId found for variableNotif: {}", variableNotif);
                }
            } else {
                log.warn("VariableNotif mismatch. Expected: {}, Found: {}",
                    notificationEntry.get("variableNotif"), variableNotif);
            }
        } else {
            log.info("Notification not started or stopped. startCheck: {}, stopCheck: {}, notifIdStr: {}, variableNotif: {}",
                startCheck, stopCheck, notifIdStr, variableNotif);
        }

    }



    }
