package com.runflow.engine;

import org.activiti.bpmn.model.FlowElement;

import java.util.Date;

public interface ExecutionEntity {
    String getId();

    String getProcessDefinitionId();

    String getProcessInstanceId();

    String getParentId();

    FlowElement getCurrentFlowElement();

    ExecutionEntityImpl getParent();

    void setCurrentFlowElement(FlowElement flowElement);

    String getCurrentActivityId();

    void setRootProcessInstanceId(String rootProcessInstanceId);

    String getRootProcessInstanceId();


    void setActive(boolean isActive);

    /**
     * returns whether this execution is currently active.
     */
    boolean isActive();


    String getSerialNumber();

    void setSerialNumber(String serialNumber);

}
