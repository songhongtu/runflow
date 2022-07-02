package com.runflow.engine.bpmn.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class BaseElement implements HasExtensionAttributes {
    protected String id;
    protected int xmlRowNumber;
    protected int xmlColumnNumber;
    protected Map<String, List<ExtensionElement>> extensionElements = new LinkedHashMap();
    protected Map<String, List<ExtensionAttribute>> attributes = new LinkedHashMap();

    public BaseElement() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, List<ExtensionElement>> getExtensionElements() {
        return this.extensionElements;
    }

    public void addExtensionElement(ExtensionElement extensionElement) {
        if (extensionElement != null && StringUtils.isNotEmpty(extensionElement.getName())) {
            List<ExtensionElement> elementList = null;
            if (!this.extensionElements.containsKey(extensionElement.getName())) {
                elementList = new ArrayList();
                this.extensionElements.put(extensionElement.getName(), elementList);
            }

            ((List)this.extensionElements.get(extensionElement.getName())).add(extensionElement);
        }

    }

    public void setExtensionElements(Map<String, List<ExtensionElement>> extensionElements) {
        this.extensionElements = extensionElements;
    }

    public Map<String, List<ExtensionAttribute>> getAttributes() {
        return this.attributes;
    }

    public String getAttributeValue(String namespace, String name) {
        List<ExtensionAttribute> attributes = (List)this.getAttributes().get(name);
        if (attributes != null && !attributes.isEmpty()) {
            Iterator var4 = attributes.iterator();

            while(var4.hasNext()) {
                ExtensionAttribute attribute = (ExtensionAttribute)var4.next();
                if (namespace == null && attribute.getNamespace() == null || namespace.equals(attribute.getNamespace())) {
                    return attribute.getValue();
                }
            }
        }

        return null;
    }

    public void addAttribute(ExtensionAttribute attribute) {
        if (attribute != null && StringUtils.isNotEmpty(attribute.getName())) {
            List<ExtensionAttribute> attributeList = null;
            if (!this.attributes.containsKey(attribute.getName())) {
                attributeList = new ArrayList();
                this.attributes.put(attribute.getName(), attributeList);
            }

            ((List)this.attributes.get(attribute.getName())).add(attribute);
        }

    }

    public void setAttributes(Map<String, List<ExtensionAttribute>> attributes) {
        this.attributes = attributes;
    }

    public void setValues(BaseElement otherElement) {
        this.setId(otherElement.getId());
        this.extensionElements = new LinkedHashMap();
        Iterator var2;
        String key;
        List otherAttributeList;
        ArrayList attributeList;
        Iterator var6;
        if (otherElement.getExtensionElements() != null && !otherElement.getExtensionElements().isEmpty()) {
            var2 = otherElement.getExtensionElements().keySet().iterator();

            label66:
            while(true) {
                do {
                    do {
                        if (!var2.hasNext()) {
                            break label66;
                        }

                        key = (String)var2.next();
                        otherAttributeList = (List)otherElement.getExtensionElements().get(key);
                    } while(otherAttributeList == null);
                } while(otherAttributeList.isEmpty());

                attributeList = new ArrayList();
                var6 = otherAttributeList.iterator();

                while(var6.hasNext()) {
                    ExtensionElement extensionElement = (ExtensionElement)var6.next();
                    attributeList.add(extensionElement.clone());
                }

                this.extensionElements.put(key, attributeList);
            }
        }

        this.attributes = new LinkedHashMap();
        if (otherElement.getAttributes() != null && !otherElement.getAttributes().isEmpty()) {
            var2 = otherElement.getAttributes().keySet().iterator();

            while(true) {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return;
                        }

                        key = (String)var2.next();
                        otherAttributeList = (List)otherElement.getAttributes().get(key);
                    } while(otherAttributeList == null);
                } while(otherAttributeList.isEmpty());

                attributeList = new ArrayList();
                var6 = otherAttributeList.iterator();

                while(var6.hasNext()) {
                    ExtensionAttribute extensionAttribute = (ExtensionAttribute)var6.next();
                    attributeList.add(extensionAttribute.clone());
                }

                this.attributes.put(key, attributeList);
            }
        }
    }

    public abstract BaseElement clone();
}
