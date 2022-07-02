package com.runflow.engine.bpmn.model;

public class GraphicInfo {
    protected double x;
    protected double y;
    protected double height;
    protected double width;
    protected BaseElement element;
    protected Boolean expanded;
    protected int xmlRowNumber;
    protected int xmlColumnNumber;

    public GraphicInfo() {
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Boolean getExpanded() {
        return this.expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public BaseElement getElement() {
        return this.element;
    }

    public void setElement(BaseElement element) {
        this.element = element;
    }

    public int getXmlRowNumber() {
        return this.xmlRowNumber;
    }

    public void setXmlRowNumber(int xmlRowNumber) {
        this.xmlRowNumber = xmlRowNumber;
    }

    public int getXmlColumnNumber() {
        return this.xmlColumnNumber;
    }

    public void setXmlColumnNumber(int xmlColumnNumber) {
        this.xmlColumnNumber = xmlColumnNumber;
    }
}
