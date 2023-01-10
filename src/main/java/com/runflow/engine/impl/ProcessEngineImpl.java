package com.runflow.engine.impl;

import com.runflow.engine.RunTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessEngineImpl {


    protected RunTimeService repositoryService;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public ProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.repositoryService = processEngineConfiguration.getRunTimeService();

    }


    // getters and setters
    // //////////////////////////////////////////////////////



    public RunTimeService getRepositoryService() {
        return repositoryService;
    }


    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

}
