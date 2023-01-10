package com.runflow.engine.bpmn.entity.impl;

import com.runflow.engine.bpmn.entity.ProcessDefinition;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;


public class ProcessDefinitionCacheEntry  {


    protected ProcessDefinition processDefinition;
    protected BpmnModel bpmnModel;
    protected Process process;

    public ProcessDefinitionCacheEntry(ProcessDefinition processDefinition, BpmnModel bpmnModel, Process process) {
        this.processDefinition = processDefinition;
        this.bpmnModel = bpmnModel;
        this.process = process;
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public BpmnModel getBpmnModel() {
        return bpmnModel;
    }

    public void setBpmnModel(BpmnModel bpmnModel) {
        this.bpmnModel = bpmnModel;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }





}

