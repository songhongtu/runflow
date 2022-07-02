package com.runflow.engine.bpmn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public  abstract class FlowElement  extends BaseElement{

    protected String name;
    protected String documentation;
    protected FlowElementsContainer parentContainer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }


    @JsonIgnore
    public FlowElementsContainer getParentContainer() {
        return parentContainer;
    }

    @JsonIgnore
    public SubProcess getSubProcess() {
        SubProcess subProcess = null;
        if (parentContainer instanceof SubProcess) {
            subProcess = (SubProcess) parentContainer;
        }

        return subProcess;
    }

    public void setParentContainer(FlowElementsContainer parentContainer) {
        this.parentContainer = parentContainer;
    }

    public abstract FlowElement clone();

    public void setValues(FlowElement otherElement) {
        super.setValues(otherElement);
        setName(otherElement.getName());
        setDocumentation(otherElement.getDocumentation());
    }
}
