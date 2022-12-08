package com.runflow.engine.context;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.runflow.engine.ActivitiEngineAgenda;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;

import java.util.*;

public  class Context {

    protected static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal<Stack<CommandContext>>();
    protected static ThreadLocal<Stack<ProcessEngineConfigurationImpl>> processEngineConfigurationStackThreadLocal = new ThreadLocal<Stack<ProcessEngineConfigurationImpl>>();
    protected static ThreadLocal<Map<String, ObjectNode>> bpmnOverrideContextThreadLocal = new ThreadLocal<Map<String, ObjectNode>>();


    public static CommandContext getCommandContext() {
        Stack<CommandContext> stack = getStack(commandContextThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    public static ActivitiEngineAgenda getAgenda() {
        return getCommandContext().getAgenda();
    }

    public static void setCommandContext(CommandContext commandContext) {
        getStack(commandContextThreadLocal).push(commandContext);
    }

    public static void removeCommandContext() {
        getStack(commandContextThreadLocal).pop();
    }

    public static ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        Stack<ProcessEngineConfigurationImpl> stack = getStack(processEngineConfigurationStackThreadLocal);
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    public static void setProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {
        getStack(processEngineConfigurationStackThreadLocal).push(processEngineConfiguration);
    }

    public static void removeProcessEngineConfiguration() {
        getStack(processEngineConfigurationStackThreadLocal).pop();
    }

    protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
        Stack<T> stack = threadLocal.get();
        if (stack == null) {
            stack = new Stack<T>();
            threadLocal.set(stack);
        }
        return stack;
    }



    public static void removeBpmnOverrideContext() {
        bpmnOverrideContextThreadLocal.remove();
    }


    protected static Map<String, ObjectNode> getBpmnOverrideContext() {
        Map<String, ObjectNode> bpmnOverrideMap = bpmnOverrideContextThreadLocal.get();
        if (bpmnOverrideMap == null) {
            bpmnOverrideMap = new HashMap<String, ObjectNode>();
        }
        return bpmnOverrideMap;
    }




}
