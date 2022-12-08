package com.runflow.engine.impl;

import com.runflow.engine.ActivitiException;
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

import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DeploymentBuilderImpl  implements DeploymentBuilder, Serializable {

    private static final long serialVersionUID = 1L;
    protected static final String DEFAULT_ENCODING = "UTF-8";

    protected transient RunTimeServiceImpl repositoryService;

    protected DeploymentEntity deployment;
    protected boolean isBpmn20XsdValidationEnabled = true;
    protected boolean isProcessValidationEnabled = true;
    protected boolean isDuplicateFilterEnabled;
    protected Date processDefinitionsActivationDate;
    protected Map<String, Object> deploymentProperties = new HashMap<String, Object>();

    public DeploymentBuilderImpl(RunTimeServiceImpl repositoryService) {
        this.repositoryService = repositoryService;
        this.deployment =new DeploymentEntityImpl();
    }

    public DeploymentBuilder addInputStream(String resourceName, InputStream inputStream) {
        if (inputStream == null) {
            throw new ActivitiException("inputStream for resource '" + resourceName + "' is null");
        }
        byte[] bytes = IoUtil.readInputStream(inputStream, resourceName);
        ResourceEntity resource =new ResourceEntityImpl();
        resource.setName(resourceName);
        resource.setBytes(bytes);
        deployment.addResource(resource);
        return this;
    }

    public DeploymentBuilder addClasspathResource(String resource) {
        InputStream inputStream = ReflectUtil.getResourceAsStream(resource);
        if (inputStream == null) {
            throw new ActivitiException("resource '" + resource + "' not found");
        }
        return addInputStream(resource, inputStream);
    }

    public DeploymentBuilder addString(String resourceName, String text) {
        if (text == null) {
            throw new ActivitiException("text is null");
        }
        ResourceEntity resource = new ResourceEntityImpl();
        resource.setName(resourceName);
        try {
            resource.setBytes(text.getBytes(DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            throw new ActivitiException("Unable to get process bytes.", e);
        }
        deployment.addResource(resource);
        return this;
    }

    public DeploymentBuilder addBytes(String resourceName, byte[] bytes) {
        if (bytes == null) {
            throw new ActivitiException("bytes is null");
        }
        ResourceEntity resource = new ResourceEntityImpl();
        resource.setName(resourceName);
        resource.setBytes(bytes);

        deployment.addResource(resource);
        return this;
    }

    public DeploymentBuilder addZipInputStream(ZipInputStream zipInputStream) {
        try {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    byte[] bytes = IoUtil.readInputStream(zipInputStream, entryName);
                    ResourceEntity resource =new ResourceEntityImpl();
                    resource.setName(entryName);
                    resource.setBytes(bytes);
                    deployment.addResource(resource);
                }
                entry = zipInputStream.getNextEntry();
            }
        } catch (Exception e) {
            throw new ActivitiException("problem reading zip input stream", e);
        }
        return this;
    }

    public DeploymentBuilder addBpmnModel(String resourceName, BpmnModel bpmnModel) {
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        try {
            String bpmn20Xml = new String(bpmnXMLConverter.convertToXML(bpmnModel), "UTF-8");
            addString(resourceName, bpmn20Xml);
        } catch (UnsupportedEncodingException e) {
            throw new ActivitiException("Error while transforming BPMN model to xml: not UTF-8 encoded", e);
        }
        return this;
    }

    public DeploymentBuilder name(String name) {
        deployment.setName(name);
        return this;
    }

    public DeploymentBuilder category(String category) {
        deployment.setCategory(category);
        return this;
    }

    public DeploymentBuilder key(String key) {
        deployment.setKey(key);
        return this;
    }

    public DeploymentBuilder disableBpmnValidation() {
        this.isProcessValidationEnabled = false;
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

    public boolean isProcessValidationEnabled() {
        return isProcessValidationEnabled;
    }

    public boolean isBpmn20XsdValidationEnabled() {
        return isBpmn20XsdValidationEnabled;
    }

    public boolean isDuplicateFilterEnabled() {
        return isDuplicateFilterEnabled;
    }

    public Date getProcessDefinitionsActivationDate() {
        return processDefinitionsActivationDate;
    }

    public Map<String, Object> getDeploymentProperties() {
        return deploymentProperties;
    }

}
