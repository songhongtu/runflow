package com.runflow.engine.impl;

import com.runflow.engine.RunTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessEngineImpl {

    private static Logger log = LoggerFactory.getLogger(ProcessEngineImpl.class);

    protected RunTimeService repositoryService;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public ProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.repositoryService = processEngineConfiguration.getRunTimeService();

    }

    public void close() {
//        ProcessEngines.unregister(this);


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
