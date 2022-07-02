package com.runflow.engine.bpmn.model;

import java.util.Collection;

public interface FlowElementsContainer {

    FlowElement getFlowElement(String id);

    Collection<FlowElement> getFlowElements();

    void addFlowElement(FlowElement element);

    void addFlowElementToMap(FlowElement element);

    void removeFlowElement(String elementId);

    void removeFlowElementFromMap(String elementId);

}
