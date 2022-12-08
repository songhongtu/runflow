package com.runflow.engine.impl;

import com.runflow.engine.CommandExecutorImpl;

public class ServiceImpl {
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public ServiceImpl() {

    }

    public ServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    protected CommandExecutorImpl commandExecutor;

    public CommandExecutorImpl getCommandExecutor() {
        return commandExecutor;
    }

    public void setCommandExecutor(CommandExecutorImpl commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

}
