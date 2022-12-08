package com.runflow.engine.impl;

import com.runflow.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ProcessEngineImpl {

    private static Logger log = LoggerFactory.getLogger(ProcessEngineImpl.class);

    protected String name;
    protected RepositoryService repositoryService;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public ProcessEngineImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.name = processEngineConfiguration.getProcessEngineName();
        this.repositoryService = processEngineConfiguration.getRepositoryService();


        if (name == null) {
            log.info("default activiti ProcessEngine created");
        } else {
            log.info("ProcessEngine {} created", name);
        }

//        ProcessEngines.registerProcessEngine(this);

    }

    public void close() {
//        ProcessEngines.unregister(this);


    }

    // getters and setters
    // //////////////////////////////////////////////////////

    public String getName() {
        return name;
    }


    public RepositoryService getRepositoryService() {
        return repositoryService;
    }


    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

}
