package com.runflow.engine.behavior;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class TaskActivityBehavior extends AbstractBpmnActivityBehavior {

    private static final long serialVersionUID = 1L;

    protected String getActiveValue(String originalValue, String propertyName, ObjectNode taskElementProperties) {
        String activeValue = originalValue;
        if (taskElementProperties != null) {
            JsonNode overrideValueNode = taskElementProperties.get(propertyName);
            if (overrideValueNode != null) {
                if (overrideValueNode.isNull()) {
                    activeValue = null;
                } else {
                    activeValue = overrideValueNode.asText();
                }
            }
        }
        return activeValue;
    }

    protected List<String> getActiveValueList(List<String> originalValues, String propertyName, ObjectNode taskElementProperties) {
        List<String> activeValues = originalValues;
        if (taskElementProperties != null) {
            JsonNode overrideValuesNode = taskElementProperties.get(propertyName);
            if (overrideValuesNode != null) {
                if (overrideValuesNode.isNull() || overrideValuesNode.isArray() == false || overrideValuesNode.size() == 0) {
                    activeValues = null;
                } else {
                    activeValues = new ArrayList<String>();
                    for (JsonNode valueNode : overrideValuesNode) {
                        activeValues.add(valueNode.asText());
                    }
                }
            }
        }
        return activeValues;
    }
}
