package com.runflow.engine.bpmn.entity.impl;

import com.runflow.engine.bpmn.entity.AbstractEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import org.activiti.bpmn.model.IOSpecification;


public class ProcessDefinitionEntityImpl extends AbstractEntity implements ProcessDefinitionEntity {

    protected String id;

    protected String name;
    protected String description;
    protected String key;
    protected int version;
    protected String category;
    protected String deploymentId;
    protected String resourceName;
    protected String tenantId = ProcessEngineConfigurationImpl.NO_TENANT_ID;
    protected String diagramResourceName;
    protected boolean isGraphicalNotationDefined;
    protected boolean hasStartFormKey;
    protected IOSpecification ioSpecification;

    // Backwards compatibility
    protected String engineVersion;


    // getters and setters
    // //////////////////////////////////////////////////////


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public int getVersion() {
        return version;
    }


    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getTenantId() {
        return tenantId;
    }






    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public boolean hasStartFormKey() {
        return hasStartFormKey;
    }

    public boolean getHasStartFormKey() {
        return hasStartFormKey;
    }

    public void setStartFormKey(boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public void setHasStartFormKey(boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public boolean isGraphicalNotationDefined() {
        return isGraphicalNotationDefined;
    }

    public boolean hasGraphicalNotation() {
        return isGraphicalNotationDefined;
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    public void setGraphicalNotationDefined(boolean isGraphicalNotationDefined) {
        this.isGraphicalNotationDefined = isGraphicalNotationDefined;
    }



    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public IOSpecification getIoSpecification() {
        return ioSpecification;
    }

    public void setIoSpecification(IOSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public String toString() {
        return "ProcessDefinitionEntity[" + id + "]";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
    this.id = id;
    }
}
