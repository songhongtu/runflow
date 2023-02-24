package com.runflow.engine.utils;


import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.el.ExpressionManager;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.ObjectValueExpression;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.model.SequenceFlow;
import org.slf4j.LoggerFactory;

import javax.el.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConditionUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConditionUtil.class);


    public static boolean hasTrueCondition(String ex, ExecutionEntityImpl execution) {
        Object value = createExpression(ex, execution);
        if (value != null) {
            ExecutionEntityImpl rootParent = execution.findRootParent(execution);
            rootParent.variableInstances.put(execution.getActivityId(), value);
        }
        return (boolean) value;
    }


    public static Object createExpression(String expression, ExecutionEntityImpl execution) {
        try {
            ExpressionManager expressionManager = Context.getCommandContext().getProcessEngineConfiguration().getExpressionManager();
            ExpressionFactory expressionFactory = expressionManager.getExpressionFactory();
            ELContext elContext = expressionManager.getElContext(execution);
            ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, expression.trim(), Object.class);
            Object value = valueExpression.getValue(elContext);
            return value;
        } catch (Exception e) {
            logger.error("el表达式执行失败:{}",expression, e);
            throw e;
        }
    }


    public static void main(String[] args) throws NoSuchMethodException {
        ExpressionFactoryImpl factory = new ExpressionFactoryImpl();
        SimpleContext elContext = new SimpleContext();

        ObjectValueExpression valueExpression = factory.createValueExpression("666", Object.class);
        elContext.setVariable("pi", valueExpression);
        ValueExpression expression = factory.createValueExpression(elContext, "${pi==true}", String.class);
        System.out.println("math:sin(pi/2) = " + expression.getValue(elContext));


        ObjectValueExpression valueExpression1 = factory.createValueExpression(new ConditionUtil(), Object.class);
        ValueExpression expression1 = factory.createValueExpression(elContext, "${2==2}", Object.class);
        System.out.println("math:sin(pi/2) = " + expression1.getValue(elContext));


    }

}
