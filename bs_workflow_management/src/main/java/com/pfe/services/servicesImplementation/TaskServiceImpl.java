package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements com.pfe.services.TaskService {


    private final TaskService taskService;

    public TaskServiceImpl(TaskService taskService){
        this.taskService = taskService;
    }

    @Override
    public List<Task> getTasksByAssignee(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list();
    }

    @Override
    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    @Override
    public void assignTask(String taskId, String userId) {
        taskService.setAssignee(taskId, userId);
    }

    @Override
    public void addCandidateUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

    @Override
    public Task getCurrentTask(String processInstanceId) {
        return taskService.createTaskQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
    }
}


