package com.pfe.dto;


public class HistoricProcessInstanceWithTaskDTO {
    private HistoricProcessInstanceDTO instance;
    private String currentTaskName;


    public String getCurrentTaskName() {
        return this.currentTaskName;
    }

    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public HistoricProcessInstanceDTO getInstance() {
        return this.instance;
    }

    public void setInstance(HistoricProcessInstanceDTO instance) {
        this.instance = instance;
    }


}
