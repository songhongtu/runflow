package com.runflow.engine.bpmn.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class BpmnModel {

    protected Map<String, List<ExtensionAttribute>> definitionsAttributes = new LinkedHashMap();
    protected List<Process> processes = new ArrayList();
    protected Map<String, GraphicInfo> locationMap = new LinkedHashMap();
    protected Map<String, GraphicInfo> labelLocationMap = new LinkedHashMap();
    protected Map<String, List<GraphicInfo>> flowLocationMap = new LinkedHashMap();
    protected Map<String, String> errorMap = new LinkedHashMap();
    protected Map<String, String> namespaceMap = new LinkedHashMap();
    protected String targetNamespace;
    protected String sourceSystemId;
    protected List<String> userTaskFormTypes;
    protected List<String> startEventFormTypes;
    protected int nextFlowIdCounter = 1;
    protected Object eventSupport;
    public Map<String, List<ExtensionAttribute>> getDefinitionsAttributes() {
        return definitionsAttributes;
    }

    public String getDefinitionsAttributeValue(String namespace, String name) {
        List<ExtensionAttribute> attributes = getDefinitionsAttributes().get(name);
        if (attributes != null && !attributes.isEmpty()) {
            for (ExtensionAttribute attribute : attributes) {
                if (namespace.equals(attribute.getNamespace()))
                    return attribute.getValue();
            }
        }
        return null;
    }

    public void addDefinitionsAttribute(ExtensionAttribute attribute) {
        if (attribute != null && StringUtils.isNotEmpty(attribute.getName())) {
            List<ExtensionAttribute> attributeList = null;
            if (this.definitionsAttributes.containsKey(attribute.getName()) == false) {
                attributeList = new ArrayList<ExtensionAttribute>();
                this.definitionsAttributes.put(attribute.getName(), attributeList);
            }
            this.definitionsAttributes.get(attribute.getName()).add(attribute);
        }
    }

    public void setDefinitionsAttributes(Map<String, List<ExtensionAttribute>> attributes) {
        this.definitionsAttributes = attributes;
    }




    public List<Process> getProcesses() {
        return processes;
    }

    public void addProcess(Process process) {
        processes.add(process);
    }







    public void addGraphicInfo(String key, GraphicInfo graphicInfo) {
        locationMap.put(key, graphicInfo);
    }

    public GraphicInfo getGraphicInfo(String key) {
        return locationMap.get(key);
    }

    public void removeGraphicInfo(String key) {
        locationMap.remove(key);
    }

    public List<GraphicInfo> getFlowLocationGraphicInfo(String key) {
        return flowLocationMap.get(key);
    }

    public void removeFlowGraphicInfoList(String key) {
        flowLocationMap.remove(key);
    }

    public Map<String, GraphicInfo> getLocationMap() {
        return locationMap;
    }

    public Map<String, List<GraphicInfo>> getFlowLocationMap() {
        return flowLocationMap;
    }

    public GraphicInfo getLabelGraphicInfo(String key) {
        return labelLocationMap.get(key);
    }

    public void addLabelGraphicInfo(String key, GraphicInfo graphicInfo) {
        labelLocationMap.put(key, graphicInfo);
    }

    public void removeLabelGraphicInfo(String key) {
        labelLocationMap.remove(key);
    }

    public Map<String, GraphicInfo> getLabelLocationMap() {
        return labelLocationMap;
    }

    public void addFlowGraphicInfoList(String key, List<GraphicInfo> graphicInfoList) {
        flowLocationMap.put(key, graphicInfoList);
    }

















    public Map<String, String> getErrors() {
        return errorMap;
    }

    public void setErrors(Map<String, String> errorMap) {
        this.errorMap = errorMap;
    }

    public void addError(String errorRef, String errorCode) {
        if (StringUtils.isNotEmpty(errorRef)) {
            errorMap.put(errorRef, errorCode);
        }
    }

    public boolean containsErrorRef(String errorRef) {
        return errorMap.containsKey(errorRef);
    }











    public void addNamespace(String prefix, String uri) {
        namespaceMap.put(prefix, uri);
    }

    public boolean containsNamespacePrefix(String prefix) {
        return namespaceMap.containsKey(prefix);
    }

    public String getNamespace(String prefix) {
        return namespaceMap.get(prefix);
    }

    public Map<String, String> getNamespaces() {
        return namespaceMap;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public String getSourceSystemId() {
        return sourceSystemId;
    }

    public void setSourceSystemId(String sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

    public List<String> getUserTaskFormTypes() {
        return userTaskFormTypes;
    }

    public void setUserTaskFormTypes(List<String> userTaskFormTypes) {
        this.userTaskFormTypes = userTaskFormTypes;
    }

    public List<String> getStartEventFormTypes() {
        return startEventFormTypes;
    }

    public void setStartEventFormTypes(List<String> startEventFormTypes) {
        this.startEventFormTypes = startEventFormTypes;
    }

//    @JsonIgnore
    public Object getEventSupport() {
        return eventSupport;
    }

    public void setEventSupport(Object eventSupport) {
        this.eventSupport = eventSupport;
    }

}
