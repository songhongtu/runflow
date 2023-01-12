package com.runflow.engine.bpmn.entity.impl;

import com.runflow.engine.bpmn.entity.Entity;

public  abstract class AbstractEntityNoRevision implements Entity {

    protected String id;
    protected boolean isInserted;
    protected boolean isUpdated;
    protected boolean isDeleted;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public boolean isInserted() {
        return isInserted;
    }

    public void setInserted(boolean isInserted) {
        this.isInserted = isInserted;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    @Override
    public String getSerialNumber() {
        return null;
    }

    @Override
    public void setSerialNumber(String serialNumber) {

    }
}
