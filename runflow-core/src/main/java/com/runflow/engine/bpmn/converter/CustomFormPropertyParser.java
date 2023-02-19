package com.runflow.engine.bpmn.converter;

import com.runflow.engine.bpmn.entity.impl.CustomTaskFormPropertyTask;
import org.activiti.bpmn.converter.child.FormPropertyParser;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.FormValue;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamReader;

public class CustomFormPropertyParser extends FormPropertyParser {

    public boolean accepts(BaseElement element) {
        return (element instanceof CustomTaskFormPropertyTask);
    }

    public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {

        if (!accepts(parentElement))
            return;

        FormProperty property = new FormProperty();
        BpmnXMLUtil.addXMLLocation(property, xtr);
        property.setId(xtr.getAttributeValue(null, ATTRIBUTE_FORM_ID));
        property.setName(xtr.getAttributeValue(null, ATTRIBUTE_FORM_NAME));
        property.setType(xtr.getAttributeValue(null, ATTRIBUTE_FORM_TYPE));
        property.setVariable(xtr.getAttributeValue(null, ATTRIBUTE_FORM_VARIABLE));
        property.setExpression(xtr.getAttributeValue(null, ATTRIBUTE_FORM_EXPRESSION));
        property.setDefaultExpression(xtr.getAttributeValue(null, ATTRIBUTE_FORM_DEFAULT));
        property.setDatePattern(xtr.getAttributeValue(null, ATTRIBUTE_FORM_DATEPATTERN));
        if (StringUtils.isNotEmpty(xtr.getAttributeValue(null, ATTRIBUTE_FORM_REQUIRED))) {
            property.setRequired(Boolean.valueOf(xtr.getAttributeValue(null, ATTRIBUTE_FORM_REQUIRED)));
        }
        if (StringUtils.isNotEmpty(xtr.getAttributeValue(null, ATTRIBUTE_FORM_READABLE))) {
            property.setReadable(Boolean.valueOf(xtr.getAttributeValue(null, ATTRIBUTE_FORM_READABLE)));
        }
        if (StringUtils.isNotEmpty(xtr.getAttributeValue(null, ATTRIBUTE_FORM_WRITABLE))) {
            property.setWriteable(Boolean.valueOf(xtr.getAttributeValue(null, ATTRIBUTE_FORM_WRITABLE)));
        }

        boolean readyWithFormProperty = false;
        try {
            while (readyWithFormProperty == false && xtr.hasNext()) {
                xtr.next();
                if (xtr.isStartElement() && ELEMENT_VALUE.equalsIgnoreCase(xtr.getLocalName())) {
                    FormValue value = new FormValue();
                    BpmnXMLUtil.addXMLLocation(value, xtr);
                    value.setId(xtr.getAttributeValue(null, ATTRIBUTE_ID));
                    value.setName(xtr.getAttributeValue(null, ATTRIBUTE_NAME));
                    property.getFormValues().add(value);

                } else if (xtr.isEndElement() && getElementName().equalsIgnoreCase(xtr.getLocalName())) {
                    readyWithFormProperty = true;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error parsing form properties child elements", e);
        }
        ((CustomTaskFormPropertyTask) parentElement).getFormProperties().add(property);
    }

}
