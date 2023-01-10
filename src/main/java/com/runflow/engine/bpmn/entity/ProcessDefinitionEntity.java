package com.runflow.engine.bpmn.entity;


public interface ProcessDefinitionEntity extends ProcessDefinition, Entity {


    void setKey(String key);

    void setName(String name);

    void setDescription(String description);

    void setDeploymentId(String deploymentId);


    void setResourceName(String resourceName);




    void setCategory(String category);

    void setDiagramResourceName(String diagramResourceName);



    boolean isGraphicalNotationDefined();

    void setGraphicalNotationDefined(boolean isGraphicalNotationDefined);


    String getEngineVersion();

    void setEngineVersion(String engineVersion);

}