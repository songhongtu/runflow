package com.runflow.engine.bpmn.entity.impl;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.bpmn.deployer.ParsedDeployment;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;

import java.util.*;

public class DeploymentEntityImpl extends AbstractEntityNoRevision implements DeploymentEntity {


    protected String name;
    protected String category;
    protected String key;
    protected String tenantId = ProcessEngineConfigurationImpl.NO_TENANT_ID;
    protected Map<String, ResourceEntity> resources;
    protected Date deploymentTime;
    protected boolean isNew;

    // Backwards compatibility
    protected String engineVersion;


    protected ParsedDeployment parsedDeployment;



    public void addResource(ResourceEntity resource) {
        if (resources == null) {
            resources = new HashMap<>();
        }
        resources.put(resource.getName(), resource);
    }

    // lazy loading ///////////////////////////////////////////////////////////////

    public Map<String, ResourceEntity> getResources() {
        if (resources == null && id != null) {
                throw new RunFlowException("不支持");
        }
        return resources;
    }


    // Deployed artifacts manipulation ////////////////////////////////////////////


    @SuppressWarnings("unchecked")

    // getters and setters ////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setResources(Map<String, ResourceEntity> resources) {
        this.resources = resources;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public ParsedDeployment getParsedDeployment() {
        return parsedDeployment;
    }

    public void setParsedDeployment(ParsedDeployment parsedDeployment) {
        this.parsedDeployment = parsedDeployment;
    }
// common methods //////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "DeploymentEntity[id=" + id + ", name=" + name + "]";
    }

}
