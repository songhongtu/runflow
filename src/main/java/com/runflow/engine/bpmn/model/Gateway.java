package com.runflow.engine.bpmn.model;

public abstract class Gateway extends FlowNode {

    protected String defaultFlow;

    public String getDefaultFlow() {
        return defaultFlow;
    }

    public void setDefaultFlow(String defaultFlow) {
        this.defaultFlow = defaultFlow;
    }

    public abstract Gateway clone();

    public void setValues(Gateway otherElement) {
        super.setValues(otherElement);
        setDefaultFlow(otherElement.getDefaultFlow());
    }
}
