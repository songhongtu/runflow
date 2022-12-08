package com.runflow.engine.bpmn.entity;

public interface ResourceEntity  extends Entity {

    String getName();

    void setName(String name);

    byte[] getBytes();

    void setBytes(byte[] bytes);

    String getDeploymentId();

    void setDeploymentId(String deploymentId);

    Object getPersistentState();

    void setGenerated(boolean generated);

    boolean isGenerated();

}

