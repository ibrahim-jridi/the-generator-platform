package com.pfe.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.domain.enumeration.NotificationType;
import com.pfe.domain.enumeration.Topic;
import com.pfe.dto.request.Notification;
import com.pfe.feignServices.GroupService;
import com.pfe.feignServices.NotificationService;
import com.pfe.feignServices.RoleService;
import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class NotificationDelegate implements JavaDelegate {
    private final NotificationService notificationService;
    private final UserService userService;
    private final GroupService groupService;
    private final RoleService roleService;
    private static final Logger log = LoggerFactory.getLogger(NotificationDelegate.class);

    public NotificationDelegate(NotificationService notificationService, UserService userService, GroupService groupService, RoleService roleService) {
        this.notificationService = notificationService;

        this.userService = userService;
        this.groupService = groupService;
        this.roleService = roleService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Object sendToObj = delegateExecution.getVariable("sendTo");
        Object messageSubjectObj = delegateExecution.getVariable("messageSubject");
        Object messageBodyObj = delegateExecution.getVariable("messageBody");

        if (sendToObj != null) {
            String sendToJson = sendToObj.toString();
            String subject = messageSubjectObj.toString();
            String message = messageBodyObj.toString();
            subject = replacePlaceholders(subject, delegateExecution);
            message = replacePlaceholders(message, delegateExecution);
            List<UUID> users = new ArrayList<>();
            List<UUID> groups = new ArrayList<>();
            List<UUID> roles = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode sendToJsonObject = objectMapper.readTree(sendToJson);
            if (sendToJson.contains("\"user\":") && sendToJsonObject.get("user").isArray() && !isEmptyOrContainsOnlyEmptyStrings(sendToJsonObject.get("user"))) {
                users = extractUUIDList(sendToJson, "user");
            }
            if (sendToJson.contains("\"group\":") && sendToJsonObject.get("group").isArray() && !isEmptyOrContainsOnlyEmptyStrings(sendToJsonObject.get("group"))) {
                groups = extractUUIDList(sendToJson, "group");
            }

            if (sendToJson.contains("\"role\":") && sendToJsonObject.get("role").isArray() && !isEmptyOrContainsOnlyEmptyStrings(sendToJsonObject.get("role"))) {
                roles = extractUUIDList(sendToJson, "role");
            }
            List<String> to = new ArrayList<>();
            if (users != null) {
                for (UUID user : users) {
                    to.add(user.toString());
                }
            }
            if (groups != null) {
                for (UUID group : groups) {
                    List<UUID> ids = groupService.getUserIdsByGroupId(group).getBody();
                    for (UUID id : ids) {
                        to.add(id.toString());
                    }

                }
            }
            if (roles != null) {
                for (UUID role : roles) {
                    List<UUID> ids = roleService.getUserIdsByRoleId(role).getBody();
                    for (UUID id : ids) {
                        to.add(id.toString());
                    }

                }
            }
            for (String recipient : to.stream().distinct().collect(Collectors.toList())) {
                Notification notification = new Notification();
                notification.setBody(message);
                notification.setSubject(subject);
                notification.setRecipient(recipient);
                notification.setType(NotificationType.SUCCESS);
                notification.setTopic(Topic.WORKFLOW_EVENT_TO_USER);
                notificationService.sendAndSaveNotificationToUser(notification);
            }


        } else {
            log.info("sendTo is not defined in the process variables.");
        }
    }

    private List<UUID> extractUUIDList(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);

        if (startIndex == -1) {
            return new ArrayList<>();
        }
        startIndex += searchKey.length();
        int endIndex = json.indexOf("]", startIndex) + 1;

        String jsonList = json.substring(startIndex, endIndex);
        jsonList = jsonList.replaceAll("[\\[\\]\"]", "");

        String[] items = jsonList.split(",");

        return Arrays.stream(items)
            .map(UUID::fromString)
            .collect(Collectors.toList());
    }

    private boolean isEmptyOrContainsOnlyEmptyStrings(JsonNode arrayNode) {
        if (arrayNode.size() == 0) {
            return true;
        }

        for (JsonNode element : arrayNode) {
            if (!element.asText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String replacePlaceholders(String message, DelegateExecution execution) {
        Pattern pattern = Pattern.compile("\\$\\{\\{(.*?)}}");
        Matcher matcher = pattern.matcher(message);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            String replacement;
            Object variableValue = execution.getVariable(variableName);
            replacement = variableValue != null ? variableValue.toString() : "";

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
