package com.runflow.engine.bpmn.model;

public class MultiInstanceLoopCharacteristics extends BaseElement{

    protected String inputDataItem;
    protected String loopCardinality;
    protected String completionCondition;
    protected String elementVariable;
    protected String elementIndexVariable;
    protected boolean sequential;

    public String getInputDataItem() {
        return inputDataItem;
    }

    public void setInputDataItem(String inputDataItem) {
        this.inputDataItem = inputDataItem;
    }

    public String getLoopCardinality() {
        return loopCardinality;
    }

    public void setLoopCardinality(String loopCardinality) {
        this.loopCardinality = loopCardinality;
    }

    public String getCompletionCondition() {
        return completionCondition;
    }

    public void setCompletionCondition(String completionCondition) {
        this.completionCondition = completionCondition;
    }

    public String getElementVariable() {
        return elementVariable;
    }

    public void setElementVariable(String elementVariable) {
        this.elementVariable = elementVariable;
    }

    public String getElementIndexVariable() {
        return elementIndexVariable;
    }

    public void setElementIndexVariable(String elementIndexVariable) {
        this.elementIndexVariable = elementIndexVariable;
    }

    public boolean isSequential() {
        return sequential;
    }

    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    public MultiInstanceLoopCharacteristics clone() {
        MultiInstanceLoopCharacteristics clone = new MultiInstanceLoopCharacteristics();
        clone.setValues(this);
        return clone;
    }

    public void setValues(MultiInstanceLoopCharacteristics otherLoopCharacteristics) {
        setInputDataItem(otherLoopCharacteristics.getInputDataItem());
        setLoopCardinality(otherLoopCharacteristics.getLoopCardinality());
        setCompletionCondition(otherLoopCharacteristics.getCompletionCondition());
        setElementVariable(otherLoopCharacteristics.getElementVariable());
        setElementIndexVariable(otherLoopCharacteristics.getElementIndexVariable());
        setSequential(otherLoopCharacteristics.isSequential());
    }
}
