package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormServiceImpl implements com.pfe.services.FormService {


    private final FormService formService;

    public FormServiceImpl(FormService formService) {
        this.formService = formService;
    }

    @Override
    public TaskFormData getTaskFormData(String taskId) {
        return formService.getTaskFormData(taskId);
    }

    @Override
    public void submitTaskForm(String taskId, Map<String, Object> formProperties) {
        formService.submitTaskForm(taskId, formProperties);
    }

    @Override
    public Object getRenderedTaskForm(String taskId) {
        return formService.getRenderedTaskForm(taskId);
    }
}


