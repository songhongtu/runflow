package com.runflow.engine.interceptor;

import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandConfig;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandContextInterceptor extends AbstractCommandInterceptor {


    protected CommandContextFactory commandContextFactory;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;

    public CommandContextInterceptor(CommandContextFactory commandContextFactory, ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.commandContextFactory = commandContextFactory;
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public <T> T execute(CommandConfig config, Command<T> command) {
        CommandContext context = Context.getCommandContext();

        if(context==null){
            context = commandContextFactory.createCommandContext(command);
        }

        try {

            // Push on stack
            Context.setCommandContext(context);
            Context.setProcessEngineConfiguration(processEngineConfiguration);

            return next.execute(config, command);

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
