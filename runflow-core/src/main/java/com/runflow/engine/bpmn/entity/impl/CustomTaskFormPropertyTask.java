package com.runflow.engine.bpmn.entity.impl;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomTaskFormPropertyTask extends Task implements Serializable {

    protected List<FormProperty> formProperties = new ArrayList<FormProperty>();

    public List<FormProperty> getFormProperties() {
        return formProperties;
    }

    public void setFormProperties(List<FormProperty> formProperties) {
        this.formProperties = formProperties;
    }


    @Override
    public FlowElement clone() {
        return null;
    }
}
