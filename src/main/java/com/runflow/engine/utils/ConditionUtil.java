package com.runflow.engine.utils;


import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.context.Context;
import com.runflow.engine.el.ExpressionManager;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.ObjectValueExpression;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.model.SequenceFlow;

import javax.el.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConditionUtil {


    public boolean abs(Object o) {
        return new Random().nextBoolean();
    }


    public static boolean hasTrueCondition(String ex, ExecutionEntityImpl execution) {

        return (boolean) createExpression(ex, execution);
    }


    public static Object createExpression(String expression, ExecutionEntityImpl execution) {
        ExpressionManager expressionManager = Context.getCommandContext().getProcessEngineConfiguration().getExpressionManager();
        ExpressionFactory expressionFactory = expressionManager.getExpressionFactory();
        ELContext elContext = expressionManager.getElContext(execution);
        ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, expression.trim(), boolean.class);
        return   valueExpression.getValue(elContext);
    }


    public static void main(String[] args) throws NoSuchMethodException {
        ExpressionFactoryImpl factory = new ExpressionFactoryImpl();
        SimpleContext elContext = new SimpleContext();

        ObjectValueExpression valueExpression = factory.createValueExpression("666", Object.class);
        elContext.setVariable("pi", valueExpression);
        ValueExpression expression = factory.createValueExpression(elContext, "${pi==true}", String.class);
        System.out.println("math:sin(pi/2) = " + expression.getValue(elContext));


        ObjectValueExpression valueExpression1 = factory.createValueExpression(new ConditionUtil(), Object.class);
        elContext.setVariable("cu", valueExpression1);
        ValueExpression expression1 = factory.createValueExpression(elContext, "${cu.abs(true)}", Object.class);
        System.out.println("math:sin(pi/2) = " + expression1.getValue(elContext));


    }

}
