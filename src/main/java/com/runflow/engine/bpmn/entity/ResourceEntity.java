package com.runflow.engine.bpmn.entity;

public interface ResourceEntity  extends Entity {

    String getName();

    void setName(String name);

    byte[] getBytes();

    void setBytes(byte[] bytes);


    void setDeploymentId(String deploymentId);


    void setGenerated(boolean generated);

    boolean isGenerated();

}

