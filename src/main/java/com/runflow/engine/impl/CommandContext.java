package com.runflow.engine.impl;

import com.runflow.engine.ActivitiEngineAgenda;
import com.runflow.engine.ActivitiException;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.cache.impl.CurrentHashMapCache;
import com.runflow.engine.delegate.DefaultActivitiEngineAgenda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CommandContext {

    private static Logger log = LoggerFactory.getLogger(CommandContext.class);
    protected Command<?> command;

    protected ActivitiEngineAgenda agenda;

    protected boolean reused;
    protected Throwable exception;


    protected String serialNumber;


    protected LinkedList<Object> resultStack = new LinkedList<Object>(); // needs to be a stack, as JavaDelegates can do api calls again


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

    public boolean isReused() {
        return reused;
    }

    public void setReused(boolean reused) {
        this.reused = reused;
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
        this.agenda = new DefaultActivitiEngineAgenda(this);
//        this.sessionFactory =processEngineConfiguration.getSessionFactory();
    }


    protected Map<String, ExecutionEntity> involvedExecutions = new HashMap<String, ExecutionEntity>(1); // The executions involved with the command

    protected ProcessEngineConfigurationImpl processEngineConfiguration;


    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return processEngineConfiguration;
    }

    public void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    public void addInvolvedExecution(ExecutionEntity executionEntity) {
        if (executionEntity.getId() != null) {
            involvedExecutions.put(executionEntity.getId(), executionEntity);
        }
    }

    public boolean hasInvolvedExecutions() {
        return involvedExecutions.size() > 0;
    }

    public Collection<ExecutionEntity> getInvolvedExecutions() {
        return involvedExecutions.values();
    }

    // getters and setters


    public Command<?> getCommand() {
        return command;
    }

    public void setCommand(Command<?> command) {
        this.command = command;
    }

    public ActivitiEngineAgenda getAgenda() {
        return agenda;
    }

    public void setAgenda(ActivitiEngineAgenda agenda) {
        this.agenda = agenda;
    }

    public CurrentHashMapCache getDefaultSession() {
        return getSession();
    }


    public CurrentHashMapCache getSession() {
        CurrentHashMapCache runTimeExecution = this.processEngineConfiguration.getRunTimeExecution();
        return runTimeExecution;
    }


    public void close() {
        if (exception != null) {
            if (exception instanceof Error) {
                throw (Error) exception;
            } else if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else {
                throw new ActivitiException("exception while executing command " + command, exception);
            }
        }

    }

}
