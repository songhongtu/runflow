package com.runflow.engine.bpmn.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ExtensionElement  extends BaseElement{
    protected String name;
    protected String namespacePrefix;
    protected String namespace;
    protected String elementText;
    protected Map<String, List<ExtensionElement>> childElements = new LinkedHashMap();

    public ExtensionElement() {
    }

    public String getElementText() {
        return this.elementText;
    }

    public void setElementText(String elementText) {
        this.elementText = elementText;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Map<String, List<ExtensionElement>> getChildElements() {
        return this.childElements;
    }

    public void addChildElement(ExtensionElement childElement) {
        if (childElement != null && StringUtils.isNotEmpty(childElement.getName())) {
            List<ExtensionElement> elementList = null;
            if (!this.childElements.containsKey(childElement.getName())) {
                elementList = new ArrayList();
                this.childElements.put(childElement.getName(), elementList);
            }

            ((List)this.childElements.get(childElement.getName())).add(childElement);
        }

    }

    public void setChildElements(Map<String, List<ExtensionElement>> childElements) {
        this.childElements = childElements;
    }

    public ExtensionElement clone() {
        ExtensionElement clone = new ExtensionElement();
        clone.setValues(this);
        return clone;
    }

    public void setValues(ExtensionElement otherElement) {
        this.setName(otherElement.getName());
        this.setNamespacePrefix(otherElement.getNamespacePrefix());
        this.setNamespace(otherElement.getNamespace());
        this.setElementText(otherElement.getElementText());
        this.setAttributes(otherElement.getAttributes());
        this.childElements = new LinkedHashMap();
        if (otherElement.getChildElements() != null && !otherElement.getChildElements().isEmpty()) {
            Iterator var2 = otherElement.getChildElements().keySet().iterator();

            while(true) {
                String key;
                List otherElementList;
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        key = (String)var2.next();
                        otherElementList = (List)otherElement.getChildElements().get(key);
                    } while(otherElementList == null);
                } while(otherElementList.isEmpty());

                List<ExtensionElement> elementList = new ArrayList();
                Iterator var6 = otherElementList.iterator();

                while(var6.hasNext()) {
                    ExtensionElement extensionElement = (ExtensionElement)var6.next();
                    elementList.add(extensionElement.clone());
                }

                this.childElements.put(key, elementList);
            }
        }
    }
}
