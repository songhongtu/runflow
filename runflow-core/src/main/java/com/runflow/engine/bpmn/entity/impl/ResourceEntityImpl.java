package com.runflow.engine.bpmn.entity.impl;

import com.runflow.engine.bpmn.entity.ResourceEntity;

import java.io.Serializable;

public class ResourceEntityImpl  extends AbstractEntityNoRevision implements ResourceEntity, Serializable {

    private static final long serialVersionUID = 1L;

    protected String name;
    protected byte[] bytes;
    protected String deploymentId;
    protected boolean generated;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Object getPersistentState() {
        return ResourceEntityImpl.class;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    /**
     * Indicated whether or not the resource has been generated while deploying rather than being actual part of the deployment.
     */
    public boolean isGenerated() {
        return generated;
    }

    // common methods //////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "ResourceEntity[id=" + id + ", name=" + name + "]";
    }
}
