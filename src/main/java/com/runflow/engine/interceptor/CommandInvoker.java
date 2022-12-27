package com.runflow.engine.interceptor;

import com.runflow.engine.context.Context;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandConfig;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.agenda.AbstractOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

public class CommandInvoker extends AbstractCommandInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CommandInvoker.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(final CommandConfig config, final Command<T> command) {
        final CommandContext commandContext = Context.getCommandContext();
        // Execute the command.
        // This will produce operations that will be put on the agenda.
        commandContext.getAgenda().planOperation(new Runnable() {
            @Override
            public void run() {
                commandContext.setResult(command.execute(commandContext));
            }
        });

        // Run loop for agenda
        executeOperations(commandContext);
        String serialNumber = commandContext.getSerialNumber();
        if (serialNumber != null && commandContext.getSession().get(commandContext.getSerialNumber()) != null) {
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
            executeOperation(runnable);
        }
    }

    public void executeOperation(Runnable runnable) {
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;

            // Execute the operation if the operation has no execution (i.e. it's an operation not working on a process instance)
            // or the operation has an execution and it is not ended
            if (operation.getExecution() == null || !operation.getExecution().isEnded()) {

                runnable.run();

            }

        } else {
            runnable.run();
        }
    }

    @Override
    public CommandInterceptor getNext() {
        return null;
    }

    @Override
    public void setNext(CommandInterceptor next) {
        throw new UnsupportedOperationException("CommandInvoker must be the last interceptor in the chain");
    }

}
