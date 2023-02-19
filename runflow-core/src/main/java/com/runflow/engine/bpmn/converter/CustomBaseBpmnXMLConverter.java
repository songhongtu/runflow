package com.runflow.engine.bpmn.converter;

import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionElement;

import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomBaseBpmnXMLConverter extends BaseBpmnXMLConverter {

    public CustomBaseBpmnXMLConverter() {
        CustomFormPropertyParser customFormPropertyParser = new CustomFormPropertyParser();
        childParserMap.put(customFormPropertyParser.getElementName(), customFormPropertyParser);
    }

    protected Map<String, BaseChildElementParser> childParserMap = new HashMap<String, BaseChildElementParser>();

    protected void parseChildElements(String elementName, BaseElement parentElement, BpmnModel model, XMLStreamReader xtr) throws Exception {
        super.parseChildElements(elementName, parentElement, childParserMap, model, xtr);
    }

}
