package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.utils.ConditionUtil;
import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class UserTaskActivityBehavior extends TaskActivityBehavior {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskActivityBehavior.class);

    protected UserTask userTask;

    public UserTaskActivityBehavior(UserTask userTask) {
        this.userTask = userTask;
    }

    @Override
    public void execute(ExecutionEntityImpl execution) {
        String name = execution.getCurrentFlowElement().getName();
        String skipExpression = userTask.getSkipExpression();
        Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
        if (!StringUtils.isEmpty(skipExpression)) {
            Object expression = ConditionUtil.createExpression(skipExpression, execution);
            if (expression != null) {
                ExecutionEntityImpl rootParent = execution.findRootParent(execution);
                rootParent.variableInstances.put(execution.getActivityId(), expression);
            }
        }
        leave(execution);
    }


}
