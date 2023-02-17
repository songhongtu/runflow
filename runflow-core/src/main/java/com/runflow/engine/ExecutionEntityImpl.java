package com.runflow.engine;

import com.runflow.engine.bpmn.entity.*;
import com.runflow.engine.context.Context;
import de.odysseus.el.util.SimpleContext;
import org.activiti.bpmn.model.FlowElement;

import java.util.*;

public class ExecutionEntityImpl implements ExecutionEntity, Entity {

    protected Thread mainThread;

    // current position /////////////////////////////////////////////////////////

    protected FlowElement currentFlowElement;
    public Map<String, Object> variableInstances; // needs to be null, the logic depends on it for checking if vars were already fetched

    /**
     * the parent execution
     */
    protected ExecutionEntityImpl parent;
    /**
     * nested executions representing scopes or concurrent paths
     */
    public List<ExecutionEntityImpl> executions;

    /**
     * super execution, not-null if this execution is part of a subprocess
     */
    protected ExecutionEntityImpl superExecution;

    /**
     * reference to a subprocessinstance, not-null if currently subprocess is started from this execution
     */
    protected ExecutionEntityImpl subProcessInstance;

    /**
     * The tenant identifier (if any)
     */
    protected String name;
    protected Date startTime;

    // state/type of execution //////////////////////////////////////////////////

    protected boolean isActive = true;
    protected boolean isScope = true;

    // events ///////////////////////////////////////////////////////////////////



    // associated entities /////////////////////////////////////////////////////

    protected String processDefinitionId;

    /**
     * persisted reference to the process definition key.
     */
    protected String processDefinitionKey;


    protected String activityId;


    protected SimpleContext cachedElContext;

    protected String processInstanceId;


    protected String parentId;

    protected String superExecutionId;

    protected String rootProcessInstanceId;


    protected String serialNumber;

    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }


    public FlowElement getCurrentFlowElement() {
        if (currentFlowElement == null) {
            if ( getProcessDefinitionId() != null) {
                org.activiti.bpmn.model.Process process = Context.getProcessEngineConfiguration().getProcessDefinitionCache().get(processDefinitionKey).getProcess();
                currentFlowElement = process.getFlowElement(getCurrentActivityId(), true);
            }
        }
        return currentFlowElement;
    }

    public void setCurrentFlowElement(FlowElement currentFlowElement) {
        this.currentFlowElement = currentFlowElement;
        if (currentFlowElement != null) {
            this.activityId = currentFlowElement.getId();
        } else {
            this.activityId = null;
        }
    }

    @Override
    public String getCurrentActivityId() {
        return activityId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    @Override
    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }


    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }




    public Map<String, Object> getVariableInstances() {
        Map<String, Object> map = new HashMap<>();
        return collectVariableInstances(map);
    }


    public Map<String, Object> collectVariableInstances(Map<String, Object> map) {
        map.putAll(this.variableInstances);
        if (parent != null) {
            parent.collectVariableInstances(map);
        }
        return map;
    }


    public Object getVariableInstances(String key) {

        return this.getVariableInstances().get(key);
    }


    public void setVariableInstances(String variableName, Object value) {
        this.setVariableInstances(variableName, value, this);
    }


    public void setVariableInstances(Map<String, Object> map) {
        this.variableInstances = map;
    }

    public void setVariableInstances(String variableName, Object value, ExecutionEntityImpl sourceExecution) {
        if (sourceExecution != null) {
            sourceExecution.variableInstances.put(variableName, value);
        } else {
            this.variableInstances.put(variableName, value);
        }


    }



    public ExecutionEntityImpl getParent() {
        ensureParentInitialized();
        return parent;
    }

    protected void ensureParentInitialized() {
        if (parent == null && parentId != null) {
            throw new RunFlowException("parentId 不存在");
        }
    }

    public void setParent(ExecutionEntity parent) {
        this.parent = (ExecutionEntityImpl) parent;

        if (parent != null) {
            this.parentId = parent.getId();
        } else {
            this.parentId = null;
        }
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }


    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public List<ExecutionEntityImpl> getExecutions() {
        ensureExecutionsInitialized();
        return executions;
    }

    public boolean isScope() {
        return isScope;
    }

    public void setScope(boolean isScope) {
        this.isScope = isScope;
    }

    public void addChildExecution(ExecutionEntity executionEntity) {
        ensureExecutionsInitialized();
        executions.add((ExecutionEntityImpl) executionEntity);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void ensureExecutionsInitialized() {
        if (executions == null) {
            throw new RunFlowException("不支持");
        }
    }

    public void inactivate() {
        this.isActive = false;
    }

    public String getParentId() {
        return parentId;
    }


    public ExecutionEntityImpl getSuperExecution() {
        return superExecution;
    }

    public void setSuperExecution(ExecutionEntity superExecution) {
        this.superExecution = (ExecutionEntityImpl) superExecution;
        if (superExecution != null) {
            this.superExecutionId = ((ExecutionEntityImpl) superExecution).getId();
        } else {
            this.superExecutionId = null;
        }
    }


    public ExecutionEntityImpl createChildExecution(ExecutionEntityImpl parentExecutionEntity) {
        ExecutionEntityImpl childExecution1 = createWithEmptyRelationshipCollections();
        childExecution1.setId(this.randomID());
        inheritCommonProperties(parentExecutionEntity, childExecution1);
        childExecution1.setParent(parentExecutionEntity);
        childExecution1.setProcessDefinitionKey(parentExecutionEntity.getProcessDefinitionKey());
        childExecution1.setProcessInstanceId(parentExecutionEntity.getProcessInstanceId() != null
                ? parentExecutionEntity.getProcessInstanceId() : parentExecutionEntity.getId());
        childExecution1.setScope(false);
        parentExecutionEntity.addChildExecution(childExecution1);
        Context.getCommandContext().getAllRunTimeExecution().putSingle(childExecution1);
        return childExecution1;
    }


    protected void inheritCommonProperties(ExecutionEntityImpl parentExecutionEntity, ExecutionEntityImpl childExecution) {
        // Inherits the 'count' feature from the parent.
        // If the parent was not 'counting', we can't make the child 'counting' again.
        if (parentExecutionEntity instanceof CountingExecutionEntity) {
            CountingExecutionEntity countingParentExecutionEntity = (CountingExecutionEntity) parentExecutionEntity;
            ((CountingExecutionEntity) childExecution).setCountEnabled(countingParentExecutionEntity.isCountEnabled());
        }
        childExecution.setRootProcessInstanceId(parentExecutionEntity.getRootProcessInstanceId());
        childExecution.setActive(true);
        childExecution.setSerialNumber(parentExecutionEntity.getSerialNumber());
        childExecution.setMainThread(parentExecutionEntity.getMainThread());
    }


    public ExecutionEntityImpl createSubprocessInstance(ProcessDefinition processDefinition, ExecutionEntityImpl superExecutionEntity, String businessKey) {
        ExecutionEntityImpl subProcessInstance = createWithEmptyRelationshipCollections();
        subProcessInstance.setId(this.randomID());
        inheritCommonProperties(superExecutionEntity, subProcessInstance);
        subProcessInstance.setProcessDefinitionId(processDefinition.getId());
        subProcessInstance.setProcessDefinitionKey(processDefinition.getKey());
        subProcessInstance.setSuperExecution(superExecutionEntity);
        subProcessInstance.setRootProcessInstanceId(superExecutionEntity.getRootProcessInstanceId());
        subProcessInstance.setScope(true); // process instance is always a scope for all child executions

        superExecutionEntity.setSubProcessInstance(subProcessInstance);

        Context.getCommandContext().getAllRunTimeExecution().putSingle(subProcessInstance);
        return subProcessInstance;

    }


    public ExecutionEntityImpl createWithEmptyRelationshipCollections() {
        ExecutionEntityImpl execution1 = new ExecutionEntityImpl();
        execution1.executions = new ArrayList<>();
        execution1.variableInstances = new HashMap<>();
        return execution1;
    }

    public boolean isProcessInstanceType() {
        return parentId == null;
    }

    public String getActivityId() {
        return activityId;
    }


    public String randomID() {

        return UUID.randomUUID().toString();
    }

    public ExecutionEntityImpl findRootParent(){
        return this.findRootParent(this);
    }
    public ExecutionEntityImpl findRootParent(ExecutionEntityImpl parent) {
        // Find highest parent
        ExecutionEntityImpl parentExecution = parent;
        while (parentExecution.getParent() != null) {
            parentExecution = this.findRootParent(parentExecution.getParent());
        }
        return parentExecution;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public Thread getMainThread() {
        return mainThread;
    }

    public void setMainThread(Thread mainThread) {
        this.mainThread = mainThread;
    }

    public SimpleContext getCachedElContext() {
        return cachedElContext;
    }

    public void setCachedElContext(SimpleContext cachedElContext) {
        this.cachedElContext = cachedElContext;
    }

    public ExecutionEntityImpl getSubProcessInstance() {
        return subProcessInstance;
    }

    public void setSubProcessInstance(ExecutionEntityImpl subProcessInstance) {
        this.subProcessInstance = subProcessInstance;
    }

    public boolean hasVariable(String variableName) {
        return  this.getVariableInstances(variableName)==null?false:true;

    }


}
