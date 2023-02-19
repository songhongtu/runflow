package com.runflow.engine.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.utils.ConditionUtil;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        LOGGER.debug("用户任务  名称：{}  id:{}  线程名称:{} ", name, execution.getId(), Thread.currentThread().getName());
        String skipExpression = userTask.getSkipExpression();
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
