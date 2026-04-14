package com.pfe.services;

import org.camunda.bpm.engine.runtime.Job;

import java.util.List;

public interface ManagementService {
    List<Job> getAllJobs();

    void executeJob(String jobId);

    void deleteJob(String jobId);

    long getJobCount();
}
