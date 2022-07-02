package com.runflow.engine.bpmn.model;

import java.util.List;
import java.util.Map;

public interface HasExtensionAttributes {
    Map<String, List<ExtensionAttribute>> getAttributes();

    String getAttributeValue(String var1, String var2);

    void addAttribute(ExtensionAttribute var1);

    void setAttributes(Map<String, List<ExtensionAttribute>> var1);


}
