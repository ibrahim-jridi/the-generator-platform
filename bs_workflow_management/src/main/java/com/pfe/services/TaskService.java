package com.pfe.services;

import org.camunda.bpm.engine.task.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasksByAssignee(String assignee);

    void completeTask(String taskId);

    void assignTask(String taskId, String userId);

    void addCandidateUser(String taskId, String userId);
    Task getCurrentTask(String processInstanceId);
}
