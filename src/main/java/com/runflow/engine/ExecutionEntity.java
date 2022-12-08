package com.runflow.engine;

import org.activiti.bpmn.model.FlowElement;

import java.util.Date;

public interface ExecutionEntity {
    String getId();
    String getProcessDefinitionId();
    String getProcessInstanceId();
    void setParentId(String parentId);
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
    Date getStartTime();

    void setStartTime(Date startTime);
    boolean isEnded();

    String getSerialNumber();

    void setSerialNumber(String serialNumber);

}
