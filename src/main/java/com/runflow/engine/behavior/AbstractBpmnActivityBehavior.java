package com.runflow.engine.behavior;

import com.runflow.engine.ActivitiException;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.context.Context;
import com.runflow.engine.delegate.ActivityBehavior;
import com.runflow.engine.delegate.FlowNodeActivityBehavior;
import com.runflow.engine.parse.handler.EndEventParseHandler;
import com.runflow.engine.utils.CollectionUtil;
import org.activiti.bpmn.model.BoundaryEvent;
import org.activiti.bpmn.model.CompensateEventDefinition;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbstractBpmnActivityBehavior extends FlowNodeActivityBehavior {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractBpmnActivityBehavior.class);
    public void leave(ExecutionEntity execution) {
     super.leave(execution);
    }






}
