package com.runflow.engine.delegate;

public interface TriggerableActivityBehavior extends ActivityBehavior {

    void trigger(DelegateExecution execution, String signalEvent, Object signalData);

}
