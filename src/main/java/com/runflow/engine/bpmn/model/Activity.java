package com.runflow.engine.bpmn.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Activity extends FlowNode{

    protected String defaultFlow;
    protected boolean forCompensation;
    protected MultiInstanceLoopCharacteristics loopCharacteristics;
    protected IOSpecification ioSpecification;
    protected List<DataAssociation> dataInputAssociations = new ArrayList<DataAssociation>();
    protected List<DataAssociation> dataOutputAssociations = new ArrayList<DataAssociation>();
    protected String failedJobRetryTimeCycleValue;
    protected List<MapExceptionEntry> mapExceptions = new ArrayList<MapExceptionEntry>();

    public String getFailedJobRetryTimeCycleValue() {
        return failedJobRetryTimeCycleValue;
    }

    public void setFailedJobRetryTimeCycleValue(String failedJobRetryTimeCycleValue) {
        this.failedJobRetryTimeCycleValue = failedJobRetryTimeCycleValue;
    }
    public boolean isForCompensation() {
        return forCompensation;
    }

    public void setForCompensation(boolean forCompensation) {
        this.forCompensation = forCompensation;
    }



    public String getDefaultFlow() {
        return defaultFlow;
    }

    public void setDefaultFlow(String defaultFlow) {
        this.defaultFlow = defaultFlow;
    }

    public MultiInstanceLoopCharacteristics getLoopCharacteristics() {
        return loopCharacteristics;
    }

    public void setLoopCharacteristics(MultiInstanceLoopCharacteristics loopCharacteristics) {
        this.loopCharacteristics = loopCharacteristics;
    }

    public boolean hasMultiInstanceLoopCharacteristics() {
        return getLoopCharacteristics() != null;
    }

    public IOSpecification getIoSpecification() {
        return ioSpecification;
    }

    public void setIoSpecification(IOSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public List<DataAssociation> getDataInputAssociations() {
        return dataInputAssociations;
    }

    public void setDataInputAssociations(List<DataAssociation> dataInputAssociations) {
        this.dataInputAssociations = dataInputAssociations;
    }

    public List<DataAssociation> getDataOutputAssociations() {
        return dataOutputAssociations;
    }

    public void setDataOutputAssociations(List<DataAssociation> dataOutputAssociations) {
        this.dataOutputAssociations = dataOutputAssociations;
    }
    public List<MapExceptionEntry> getMapExceptions() {
        return mapExceptions;
    }
    public void setMapExceptions(List<MapExceptionEntry> mapExceptions) {
        this.mapExceptions = mapExceptions;
    }

    public void setValues(Activity otherActivity) {
        super.setValues(otherActivity);
        setFailedJobRetryTimeCycleValue(otherActivity.getFailedJobRetryTimeCycleValue());
        setDefaultFlow(otherActivity.getDefaultFlow());
        setForCompensation(otherActivity.isForCompensation());
        if (otherActivity.getLoopCharacteristics() != null) {
            setLoopCharacteristics(otherActivity.getLoopCharacteristics().clone());
        }
        if (otherActivity.getIoSpecification() != null) {
            setIoSpecification(otherActivity.getIoSpecification().clone());
        }

        dataInputAssociations = new ArrayList<DataAssociation>();
        if (otherActivity.getDataInputAssociations() != null && !otherActivity.getDataInputAssociations().isEmpty()) {
            for (DataAssociation association : otherActivity.getDataInputAssociations()) {
                dataInputAssociations.add(association.clone());
            }
        }

        dataOutputAssociations = new ArrayList<DataAssociation>();
        if (otherActivity.getDataOutputAssociations() != null && !otherActivity.getDataOutputAssociations().isEmpty()) {
            for (DataAssociation association : otherActivity.getDataOutputAssociations()) {
                dataOutputAssociations.add(association.clone());
            }
        }

    }
}
