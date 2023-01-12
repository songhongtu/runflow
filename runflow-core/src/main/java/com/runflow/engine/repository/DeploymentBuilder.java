package com.runflow.engine.repository;

import com.runflow.engine.bpmn.entity.Deployment;

import java.io.InputStream;

public interface DeploymentBuilder {

    DeploymentBuilder addInputStream(String resourceName, InputStream inputStream);


    DeploymentBuilder addString(String resourceName, String text);


    /**
     * Gives the deployment the given name.
     */
    DeploymentBuilder name(String name);

    /**
     * Gives the deployment the given key.
     */
    DeploymentBuilder key(String key);

    Deployment deploy();

}
