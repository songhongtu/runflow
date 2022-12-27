package com.runflow.engine.interceptor;

import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandConfig;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandContextInterceptor extends AbstractCommandInterceptor {

    private static final Logger log = LoggerFactory.getLogger(CommandContextInterceptor.class);

    protected CommandContextFactory commandContextFactory;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public CommandContextInterceptor(CommandContextFactory commandContextFactory, ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.commandContextFactory = commandContextFactory;
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public <T> T execute(CommandConfig config, Command<T> command) {
        CommandContext context = Context.getCommandContext();

        boolean contextReused = false;

        context = commandContextFactory.createCommandContext(command);

        try {

            // Push on stack
            Context.setCommandContext(context);
            Context.setProcessEngineConfiguration(processEngineConfiguration);

            return next.execute(config, command);

        } catch (Exception e) {
            context.exception(e);
        } finally {
            try {
                if (!contextReused) {
                    context.close();
                }
            } finally {
                // Pop from stack
                Context.removeCommandContext();
                Context.removeProcessEngineConfiguration();
                Context.removeBpmnOverrideContext();
            }
        }
        return null;
    }

    public CommandContextFactory getCommandContextFactory() {
        return commandContextFactory;
    }

    public void setCommandContextFactory(CommandContextFactory commandContextFactory) {
        this.commandContextFactory = commandContextFactory;
    }

    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public void setProcessEngineContext(ProcessEngineConfigurationImpl processEngineContext) {
        this.processEngineConfiguration = processEngineContext;
    }


}
