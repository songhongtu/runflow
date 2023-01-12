package com.runflow.engine.interceptor;

import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;

public class CommandContextInterceptor extends AbstractCommandInterceptor {


    protected CommandContextFactory commandContextFactory;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public CommandContextInterceptor(CommandContextFactory commandContextFactory, ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.commandContextFactory = commandContextFactory;
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public <T> T execute( Command<T> command) {
        CommandContext context = Context.getCommandContext();

        if (context == null) {
            context = commandContextFactory.createCommandContext(command);
        }
        try {
            // Push on stack
            Context.setCommandContext(context);
            Context.setProcessEngineConfiguration(processEngineConfiguration);
            return next.execute(command);
        } catch (Exception e) {
            context.exception(e);
        } finally {
            try {
                context.close();
            } finally {
                // Pop from stack
                Context.removeCommandContext();
                Context.removeProcessEngineConfiguration();
            }
        }
        return null;
    }
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }


}
