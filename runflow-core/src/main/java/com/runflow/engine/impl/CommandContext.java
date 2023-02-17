package com.runflow.engine.impl;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.RunFlowEngineAgenda;
import com.runflow.engine.RunFlowException;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.delegate.DefaultRunFlowEngineAgenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommandContext {

    private static Logger log = LoggerFactory.getLogger(CommandContext.class);
    protected Command<?> command;

    protected RunFlowEngineAgenda agenda;

    protected Throwable exception;


    protected String serialNumber;


    protected LinkedList<Object> resultStack = new LinkedList<>(); // needs to be a stack, as JavaDelegates can do api calls again


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }


    public void exception(Throwable exception) {
        if (this.exception == null) {
            this.exception = exception;

        } else {
            log.error("masked exception in command context. for root cause, see below as it will be rethrown later.", exception);
        }
    }

    public Object getResult() {
        return resultStack.pollLast();
    }

    public void setResult(Object result) {
        resultStack.add(result);
    }

    public CommandContext(Command<?> command, ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.command = command;
        this.processEngineConfiguration = processEngineConfiguration;
        this.agenda = new DefaultRunFlowEngineAgenda(this);
    }


    protected ProcessEngineConfigurationImpl processEngineConfiguration;


    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


    // getters and setters


    public Command getCommand() {
        return command;
    }

    public void setCommand(Command<?> command) {
        this.command = command;
    }

    public RunFlowEngineAgenda getAgenda() {
        return agenda;
    }




    public CurrentHashMapCache<ExecutionEntityImpl> getAllRunTimeExecution() {
        return this.processEngineConfiguration.getRunTimeExecution();
    }


    public void close() {
        if (exception != null) {
            if (exception instanceof Error) {
                throw (Error) exception;
            } else if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else {
                throw new RunFlowException("exception while executing command " + command, exception);
            }
        }

    }

}
