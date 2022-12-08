package com.runflow.engine.bpmn.entity.cache;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ProcessDefinitionInfoCacheObject {

    protected String id;
    protected int revision;
    protected ObjectNode infoNode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public ObjectNode getInfoNode() {
        return infoNode;
    }

    public void setInfoNode(ObjectNode infoNode) {
        this.infoNode = infoNode;
    }
}