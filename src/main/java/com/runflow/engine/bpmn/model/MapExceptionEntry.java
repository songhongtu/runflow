package com.runflow.engine.bpmn.model;

public class MapExceptionEntry {

    String errorCode;
    String className;
    boolean andChildren;

    public MapExceptionEntry(String errorCode, String className, boolean andChildren) {

        this.errorCode = errorCode;
        this.className = className;
        this.andChildren = andChildren;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isAndChildren() {
        return andChildren;
    }

    public void setAndChildren(boolean andChildren) {
        this.andChildren = andChildren;
    }

}
