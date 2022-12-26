package com.runflow.engine.behavior;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.utils.ConditionUtil;
import org.activiti.bpmn.model.CustomProperty;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class UserTaskActivityBehavior extends TaskActivityBehavior {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskActivityBehavior.class);

    protected UserTask userTask;

    public UserTaskActivityBehavior(UserTask userTask) {
        this.userTask = userTask;
    }

    public void execute(ExecutionEntityImpl execution) {
        ExecutionEntityImpl execution1 = (ExecutionEntityImpl) execution;
        String name = execution1.getCurrentFlowElement().getName();
        LOGGER.debug("id:" + execution.getId() + ":" + "线程名称：" + Thread.currentThread().getName() + "：" + name + ":" + "开始");
        String skipExpression = userTask.getSkipExpression();
        if (!StringUtils.isEmpty(skipExpression)) {
            ConditionUtil.createExpression(skipExpression, execution);
        }
        LOGGER.debug("id:" + execution.getId() + ":" + Thread.currentThread().getName() + "：" + name + ":" + "结束");
        leave(execution);
    }


}
