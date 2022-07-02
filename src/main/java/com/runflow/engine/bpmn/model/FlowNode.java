package com.runflow.engine.bpmn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public abstract class FlowNode extends FlowElement  {

    protected boolean asynchronous;
    protected boolean notExclusive;

    protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();

    @JsonIgnore
    protected Object behavior;

    public FlowNode() {

    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public boolean isExclusive() {
        return !notExclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.notExclusive = !exclusive;
    }

    public boolean isNotExclusive() {
        return notExclusive;
    }
    public void setNotExclusive(boolean notExclusive) {
        this.notExclusive = notExclusive;
    }

    public Object getBehavior() {
        return behavior;
    }

    public void setBehavior(Object behavior) {
        this.behavior = behavior;
    }

    public List<SequenceFlow> getIncomingFlows() {
        return incomingFlows;
    }

    public void setIncomingFlows(List<SequenceFlow> incomingFlows) {
        this.incomingFlows = incomingFlows;
    }

    public List<SequenceFlow> getOutgoingFlows() {
        return outgoingFlows;
    }

    public void setOutgoingFlows(List<SequenceFlow> outgoingFlows) {
        this.outgoingFlows = outgoingFlows;
    }

    public void setValues(FlowNode otherNode) {
        super.setValues(otherNode);
        setAsynchronous(otherNode.isAsynchronous());
        setNotExclusive(otherNode.isNotExclusive());
    }
}
