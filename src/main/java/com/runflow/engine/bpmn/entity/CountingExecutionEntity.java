package com.runflow.engine.bpmn.entity;

public interface CountingExecutionEntity {

    boolean isCountEnabled();
    void setCountEnabled(boolean isCountEnabled);

    void setEventSubscriptionCount(int eventSubscriptionCount);
    int getEventSubscriptionCount();

    void setTaskCount(int taskcount);
    int getTaskCount();

    void setJobCount(int jobCount);
    int getJobCount();

    void setTimerJobCount(int timerJobCount);
    int getTimerJobCount();

    void setSuspendedJobCount(int suspendedJobCount);
    int getSuspendedJobCount();

    void setDeadLetterJobCount(int deadLetterJobCount);
    int getDeadLetterJobCount();

    void setVariableCount(int variableCount);
    int getVariableCount();

    void setIdentityLinkCount(int identityLinkCount);
    int getIdentityLinkCount();

}
