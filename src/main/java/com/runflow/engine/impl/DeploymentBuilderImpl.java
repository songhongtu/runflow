package com.runflow.engine.impl;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ResourceEntity;
import com.runflow.engine.bpmn.entity.impl.DeploymentEntityImpl;
import com.runflow.engine.bpmn.entity.impl.ResourceEntityImpl;
import com.runflow.engine.repository.DeploymentBuilder;
import com.runflow.engine.util.io.IoUtil;
import com.runflow.engine.utils.ReflectUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.apache.commons.io.Charsets;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DeploymentBuilderImpl  implements DeploymentBuilder {

    protected static final String DEFAULT_ENCODING = Charsets.UTF_8.toString();

    protected  RunTimeServiceImpl repositoryService;

    protected DeploymentEntity deployment;
    protected boolean isBpmn20XsdValidationEnabled = true;
    protected boolean isDuplicateFilterEnabled;
    protected Date processDefinitionsActivationDate;
    protected Map<String, Object> deploymentProperties = new HashMap<>();

    public DeploymentBuilderImpl(RunTimeServiceImpl repositoryService) {
        this.repositoryService = repositoryService;
        this.deployment =new DeploymentEntityImpl();
    }

    public DeploymentBuilder addInputStream(String resourceName, InputStream inputStream) {
        if (inputStream == null) {
            throw new RunFlowException("inputStream for resource '" + resourceName + "' is null");
        }
        byte[] bytes = IoUtil.readInputStream(inputStream, resourceName);
        ResourceEntity resource =new ResourceEntityImpl();
        resource.setName(resourceName);
        resource.setBytes(bytes);
        deployment.addResource(resource);
        return this;
    }


    public DeploymentBuilder addString(String resourceName, String text) {
        if (text == null) {
            throw new RunFlowException("text is null");
        }
        ResourceEntity resource = new ResourceEntityImpl();
        resource.setName(resourceName);
        try {
            resource.setBytes(text.getBytes(DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new RunFlowException("Unable to get process bytes.", e);
        }
        deployment.addResource(resource);
        return this;
    }




    public DeploymentBuilder name(String name) {
        deployment.setName(name);
        return this;
    }


    public DeploymentBuilder key(String key) {
        deployment.setKey(key);
        return this;
    }


    public DeploymentBuilder disableSchemaValidation() {
        this.isBpmn20XsdValidationEnabled = false;
        return this;
    }

    public DeploymentBuilder tenantId(String tenantId) {
        deployment.setTenantId(tenantId);
        return this;
    }

    public DeploymentBuilder enableDuplicateFiltering() {
        this.isDuplicateFilterEnabled = true;
        return this;
    }

    public DeploymentBuilder activateProcessDefinitionsOn(Date date) {
        this.processDefinitionsActivationDate = date;
        return this;
    }

    public DeploymentBuilder deploymentProperty(String propertyKey, Object propertyValue) {
        deploymentProperties.put(propertyKey, propertyValue);
        return this;
    }

    public Deployment deploy() {
        return repositoryService.deploy(this);
    }

    // getters and setters
    // //////////////////////////////////////////////////////

    public DeploymentEntity getDeployment() {
        return deployment;
    }



}
