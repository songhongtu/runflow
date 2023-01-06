package com.runflow.engine.bpmn.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DeploymentEntity extends Deployment, Entity, Serializable {


    void addResource(ResourceEntity resource);

    Map<String, ResourceEntity> getResources();

    void addDeployedArtifact(Object deployedArtifact);

    <T> List<T> getDeployedArtifacts(Class<T> clazz);

    void setName(String name);

    void setCategory(String category);

    void setKey(String key);

    void setTenantId(String tenantId);

    void setResources(Map<String, ResourceEntity> resources);

    void setDeploymentTime(Date deploymentTime);

    boolean isNew();

    void setNew(boolean isNew);

    String getEngineVersion();

    void setEngineVersion(String engineVersion);

}