package com.runflow.engine.bpmn.entity;

import com.runflow.engine.bpmn.deployer.ParsedDeployment;

import java.util.Date;

public  interface Deployment {

    String getId();

    String getName();

    Date getDeploymentTime();

    String getCategory();

    String getKey();

    String getTenantId();

     ParsedDeployment getParsedDeployment();

     void setParsedDeployment(ParsedDeployment parsedDeployment);


}
