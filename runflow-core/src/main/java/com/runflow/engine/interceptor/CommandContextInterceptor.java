package com.runflow.engine.interceptor;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.ParallelGatewayActivityBehavior;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.LockSupport;

public class CommandContextInterceptor extends AbstractCommandInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayActivityBehavior.class);
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
            //程序异常，释放中断线程  删除存在的map
            CurrentHashMapCache<ExecutionEntityImpl> allRunTimeExecution = context.getAllRunTimeExecution();
            String serialNumber = context.getSerialNumber();
            final Set<ExecutionEntityImpl> entitySet = allRunTimeExecution.get(serialNumber);


            LOGGER.info("=============================={}",Thread.currentThread().getName());

            if (CollectionUtil.isNotEmpty(entitySet)) {

                 ExecutionEntityImpl executionEntity = new ArrayList<>(entitySet).get(0);
                do {
                    executionEntity = executionEntity.findRootParent();
                    allRunTimeExecution.remove(executionEntity.getSerialNumber());
                    ExecutionEntityImpl superExecution = executionEntity.getSuperExecution();
                    if (superExecution == null) {
                        serialNumber = null;
                    } else {
                        serialNumber = superExecution.getSerialNumber();
                        executionEntity = superExecution;
                    }
                } while (serialNumber != null);
            }
            LockSupport.unpark(context.getMainThread());


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
