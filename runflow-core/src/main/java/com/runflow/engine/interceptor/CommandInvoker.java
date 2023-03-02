package com.runflow.engine.interceptor;

import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;

import java.util.concurrent.locks.LockSupport;

public class CommandInvoker extends AbstractCommandInterceptor {


    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute( final Command<T> command) {
        final CommandContext commandContext = Context.getCommandContext();
        // Execute the command.
        // This will produce operations that will be put on the agenda.
        commandContext.getAgenda().planOperation(() -> commandContext.setResult(command.execute(commandContext)));

        // Run loop for agenda
        executeOperations(commandContext);
        //只有主线程阻塞，等待执行完成
        if (commandContext.getMainThread()!=null&&commandContext.getMainThread().equals(Thread.currentThread())&&commandContext.getAllRunTimeExecution().get(commandContext.getSerialNumber())!=null) {
            try {
                LockSupport.park();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return (T) commandContext.getResult();
    }

    protected void executeOperations(final CommandContext commandContext) {
        while (!commandContext.getAgenda().isEmpty()) {
            Runnable runnable = commandContext.getAgenda().getNextOperation();
            runnable.run();
        }
    }


}
