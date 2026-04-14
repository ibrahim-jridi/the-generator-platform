package com.pfe.services;

import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;

import java.util.List;

public interface ExternalTaskService {
    List<LockedExternalTask> fetchAndLockTasks(String workerId, String topicName, int maxTasks);

    void completeExternalTask(String taskId, String workerId);

    void handleFailure(String taskId, String workerId, String errorMessage, int retries, long retryTimeout);

}
