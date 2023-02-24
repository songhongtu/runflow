package com.runflow.engine.utils;

import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.ExtensionElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BpmnUtils {


    public static Map<String, String> getExtensionElementPropertity(Map<String, List<ExtensionElement>> extensionElements) {
        Map<String, String> map = new TreeMap<>();
        List<ExtensionElement> extensionElementList = extensionElements.get("properties");

        if (CollectionUtil.isNotEmpty(extensionElementList)) {
            for (ExtensionElement extensionElement : extensionElementList) {
                Map<String, List<ExtensionElement>> childElements = extensionElement.getChildElements();
                List<ExtensionElement> properties = childElements.get("property");
                for (ExtensionElement e : properties) {
                    Map<String, List<ExtensionAttribute>> attributes = e.getAttributes();
                    map.put(attributes.get("name").get(0).getValue(), attributes.get("value").get(0).getValue());
                }
            }

        }
        return map;
    }

}
