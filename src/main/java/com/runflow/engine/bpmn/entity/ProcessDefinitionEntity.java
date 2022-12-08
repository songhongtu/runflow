package com.runflow.engine.bpmn.entity;

import java.util.List;

public interface ProcessDefinitionEntity extends ProcessDefinition, Entity {


    void setKey(String key);

    void setName(String name);

    void setDescription(String description);

    void setDeploymentId(String deploymentId);

    void setVersion(int version);

    void setResourceName(String resourceName);

    void setTenantId(String tenantId);

    Integer getHistoryLevel();

    void setHistoryLevel(Integer historyLevel);

    void setCategory(String category);

    void setDiagramResourceName(String diagramResourceName);

    boolean getHasStartFormKey();

    void setStartFormKey(boolean hasStartFormKey);

    void setHasStartFormKey(boolean hasStartFormKey);

    boolean isGraphicalNotationDefined();

    void setGraphicalNotationDefined(boolean isGraphicalNotationDefined);

    int getSuspensionState();

    void setSuspensionState(int suspensionState);

    String getEngineVersion();

    void setEngineVersion(String engineVersion);

}