package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.runtime.Job;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementServiceImpl implements com.pfe.services.ManagementService {


    private final ManagementService managementService;

    public ManagementServiceImpl(ManagementService managementService) {
        this.managementService = managementService;
    }

    @Override
    public List<Job> getAllJobs() {
        return managementService.createJobQuery().list();
    }

    @Override
    public void executeJob(String jobId) {
        managementService.executeJob(jobId);
    }

    @Override
    public void deleteJob(String jobId) {
        managementService.deleteJob(jobId);
    }

    @Override
    public long getJobCount() {
        return managementService.createJobQuery().count();
    }



}


