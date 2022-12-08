package com.runflow.engine.interceptor;

import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;

public class CommandContextFactory {

    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public CommandContext createCommandContext(Command<?> cmd) {
        return new CommandContext(cmd, processEngineConfiguration);
    }

    // getters and setters
    // //////////////////////////////////////////////////////

    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
