package com.runflow.engine.bpmn.model;

public class ExtensionAttribute {
    protected String name;
    protected String value;
    protected String namespacePrefix;
    protected String namespace;

    public ExtensionAttribute() {
    }

    public ExtensionAttribute(String name) {
        this.name = name;
    }

    public ExtensionAttribute(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNamespacePrefix() {
        return this.namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.namespacePrefix != null) {
            sb.append(this.namespacePrefix);
            if (this.name != null) {
                sb.append(":").append(this.name);
            }
        } else {
            sb.append(this.name);
        }

        if (this.value != null) {
            sb.append("=").append(this.value);
        }

        return sb.toString();
    }

    public ExtensionAttribute clone() {
        ExtensionAttribute clone = new ExtensionAttribute();
        clone.setValues(this);
        return clone;
    }

    public void setValues(ExtensionAttribute otherAttribute) {
        this.setName(otherAttribute.getName());
        this.setValue(otherAttribute.getValue());
        this.setNamespacePrefix(otherAttribute.getNamespacePrefix());
        this.setNamespace(otherAttribute.getNamespace());
    }
}
