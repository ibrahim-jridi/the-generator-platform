package com.pfe.services;

import org.camunda.bpm.engine.form.TaskFormData;

import java.util.Map;

public interface FormService {
    TaskFormData getTaskFormData(String taskId);

    void submitTaskForm(String taskId, Map<String, Object> formProperties);

    Object getRenderedTaskForm(String taskId);
}
