package com.runflow.engine.bpmn.model;

public class Association extends Artifact {

    protected AssociationDirection associationDirection = AssociationDirection.NONE;
    protected String sourceRef;
    protected String targetRef;

    public AssociationDirection getAssociationDirection() {
        return associationDirection;
    }

    public void setAssociationDirection(AssociationDirection associationDirection) {
        this.associationDirection = associationDirection;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public Association clone() {
        Association clone = new Association();
        clone.setValues(this);
        return clone;
    }

    public void setValues(Association otherElement) {
        super.setValues(otherElement);
        setSourceRef(otherElement.getSourceRef());
        setTargetRef(otherElement.getTargetRef());

        if (otherElement.getAssociationDirection() != null) {
            setAssociationDirection(otherElement.getAssociationDirection());
        }
    }
}
