package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalTaskServiceImpl implements com.pfe.services.ExternalTaskService {


    private final ExternalTaskService externalTaskService;

    public ExternalTaskServiceImpl(ExternalTaskService externalTaskService) {
        this.externalTaskService = externalTaskService;
    }

    @Override
    public List<LockedExternalTask> fetchAndLockTasks(String workerId, String topicName, int maxTasks) {
        return externalTaskService.fetchAndLock(maxTasks, workerId)
            .topic(topicName, 60000L)
            .execute();
    }

    @Override
    public void completeExternalTask(String taskId, String workerId) {
        externalTaskService.complete(taskId, workerId);
    }

    @Override
    public void handleFailure(String taskId, String workerId, String errorMessage, int retries, long retryTimeout) {
        externalTaskService.handleFailure(taskId, workerId, errorMessage, retries, retryTimeout);
    }
}

