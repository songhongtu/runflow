package com.runflow.engine.bpmn.converter;

import com.runflow.engine.bpmn.constants.BpmnXMLConstants;
import com.runflow.engine.bpmn.model.*;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.*;

public abstract class BaseBpmnXMLConverter  implements BpmnXMLConstants {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseBpmnXMLConverter.class);
    protected static final List<ExtensionAttribute> defaultElementAttributes = Arrays.asList(new ExtensionAttribute("id"), new ExtensionAttribute("name"));
    protected static final List<ExtensionAttribute> defaultActivityAttributes = Arrays.asList(new ExtensionAttribute("http://activiti.org/bpmn", "async"), new ExtensionAttribute("http://activiti.org/bpmn", "exclusive"), new ExtensionAttribute("default"), new ExtensionAttribute("http://activiti.org/bpmn", "isForCompensation"));

    public BaseBpmnXMLConverter() {
    }

    public void convertToBpmnModel(XMLStreamReader xtr, BpmnModel model, com.runflow.engine.bpmn.Process activeProcess, List<SubProcess> activeSubProcessList) throws Exception {
        String elementId = xtr.getAttributeValue((String)null, "id");
        String elementName = xtr.getAttributeValue((String)null, "name");
        boolean async = this.parseAsync(xtr);
        boolean notExclusive = this.parseNotExclusive(xtr);
        String defaultFlow = xtr.getAttributeValue((String)null, "default");
        boolean isForCompensation = this.parseForCompensation(xtr);
        BaseElement parsedElement = this.convertXMLToElement(xtr, model);

        if (parsedElement instanceof FlowElement) {
            FlowElement currentFlowElement = (FlowElement)parsedElement;
            currentFlowElement.setId(elementId);
            currentFlowElement.setName(elementName);
            if (currentFlowElement instanceof FlowNode) {
                FlowNode flowNode = (FlowNode)currentFlowElement;
                flowNode.setAsynchronous(async);
                flowNode.setNotExclusive(notExclusive);
                if (currentFlowElement instanceof Activity) {
                    Activity activity = (Activity)currentFlowElement;
                    activity.setForCompensation(isForCompensation);
                    if (StringUtils.isNotEmpty(defaultFlow)) {
                        activity.setDefaultFlow(defaultFlow);
                    }
                }

                if (currentFlowElement instanceof Gateway) {
                    Gateway gateway = (Gateway)currentFlowElement;
                    if (StringUtils.isNotEmpty(defaultFlow)) {
                        gateway.setDefaultFlow(defaultFlow);
                    }
                }
            }

            SubProcess subProcess;
            if (currentFlowElement instanceof DataObject) {
                if (!activeSubProcessList.isEmpty()) {
                    subProcess = (SubProcess)activeSubProcessList.get(activeSubProcessList.size() - 1);
                    subProcess.getDataObjects().add((ValuedDataObject)parsedElement);
                } else {
                    activeProcess.getDataObjects().add((ValuedDataObject)parsedElement);
                }
            }

            if (!activeSubProcessList.isEmpty()) {
                subProcess = (SubProcess)activeSubProcessList.get(activeSubProcessList.size() - 1);
                subProcess.addFlowElement(currentFlowElement);
            } else {
                activeProcess.addFlowElement(currentFlowElement);
            }
        }

    }

    public void convertToXML(XMLStreamWriter xtw, BaseElement baseElement, BpmnModel model) throws Exception {
        xtw.writeStartElement(this.getXMLElementName());
        boolean didWriteExtensionStartElement = false;
        this.writeDefaultAttribute("id", baseElement.getId(), xtw);
        if (baseElement instanceof FlowElement) {
            this.writeDefaultAttribute("name", ((FlowElement)baseElement).getName(), xtw);
        }

        if (baseElement instanceof FlowNode) {
            FlowNode flowNode = (FlowNode)baseElement;
            if (flowNode.isAsynchronous()) {
                this.writeQualifiedAttribute("async", "true", xtw);
                if (flowNode.isNotExclusive()) {
                    this.writeQualifiedAttribute("exclusive", "false", xtw);
                }
            }

            FlowElement defaultFlowElement;
            if (baseElement instanceof Activity) {
                Activity activity = (Activity)baseElement;
                if (activity.isForCompensation()) {
                    this.writeDefaultAttribute("isForCompensation", "true", xtw);
                }

                if (StringUtils.isNotEmpty(activity.getDefaultFlow())) {
                    defaultFlowElement = model.getFlowElement(activity.getDefaultFlow());
                    if (defaultFlowElement instanceof SequenceFlow) {
                        this.writeDefaultAttribute("default", activity.getDefaultFlow(), xtw);
                    }
                }
            }

            if (baseElement instanceof Gateway) {
                Gateway gateway = (Gateway)baseElement;
                if (StringUtils.isNotEmpty(gateway.getDefaultFlow())) {
                    defaultFlowElement = model.getFlowElement(gateway.getDefaultFlow());
                    if (defaultFlowElement instanceof SequenceFlow) {
                        this.writeDefaultAttribute("default", gateway.getDefaultFlow(), xtw);
                    }
                }
            }
        }

        this.writeAdditionalAttributes(baseElement, model, xtw);
        if (baseElement instanceof FlowElement) {
            FlowElement flowElement = (FlowElement)baseElement;
            if (StringUtils.isNotEmpty(flowElement.getDocumentation())) {
                xtw.writeStartElement("documentation");
                xtw.writeCharacters(flowElement.getDocumentation());
                xtw.writeEndElement();
            }
        }

        didWriteExtensionStartElement = this.writeExtensionChildElements(baseElement, didWriteExtensionStartElement, xtw);
        didWriteExtensionStartElement = this.writeListeners(baseElement, didWriteExtensionStartElement, xtw);
        didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(baseElement, didWriteExtensionStartElement, model.getNamespaces(), xtw);
        Activity activity;
        if (baseElement instanceof Activity) {
            activity = (Activity)baseElement;
            FailedJobRetryCountExport.writeFailedJobRetryCount(activity, xtw);
        }

        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        if (baseElement instanceof Activity) {
            activity = (Activity)baseElement;
            MultiInstanceExport.writeMultiInstance(activity, xtw);
        }

        this.writeAdditionalChildElements(baseElement, model, xtw);
        xtw.writeEndElement();
    }

    protected abstract Class<? extends BaseElement> getBpmnElementType();

    protected abstract BaseElement convertXMLToElement(XMLStreamReader var1, BpmnModel var2) throws Exception;

    protected abstract String getXMLElementName();

    protected abstract void writeAdditionalAttributes(BaseElement var1, BpmnModel var2, XMLStreamWriter var3) throws Exception;

    protected boolean writeExtensionChildElements(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
        return didWriteExtensionStartElement;
    }

    protected abstract void writeAdditionalChildElements(BaseElement var1, BpmnModel var2, XMLStreamWriter var3) throws Exception;

    protected void parseChildElements(String elementName, BaseElement parentElement, BpmnModel model, XMLStreamReader xtr) throws Exception {
        this.parseChildElements(elementName, parentElement, (Map)null, model, xtr);
    }

    protected void parseChildElements(String elementName, BaseElement parentElement, Map<String, BaseChildElementParser> additionalParsers, BpmnModel model, XMLStreamReader xtr) throws Exception {
        Map<String, BaseChildElementParser> childParsers = new HashMap();
        if (additionalParsers != null) {
            childParsers.putAll(additionalParsers);
        }

        BpmnXMLUtil.parseChildElements(elementName, parentElement, xtr, childParsers, model);
    }

    protected ExtensionElement parseExtensionElement(XMLStreamReader xtr) throws Exception {
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setName(xtr.getLocalName());
        if (StringUtils.isNotEmpty(xtr.getNamespaceURI())) {
            extensionElement.setNamespace(xtr.getNamespaceURI());
        }

        if (StringUtils.isNotEmpty(xtr.getPrefix())) {
            extensionElement.setNamespacePrefix(xtr.getPrefix());
        }

        BpmnXMLUtil.addCustomAttributes(xtr, extensionElement, new List[]{defaultElementAttributes});
        boolean readyWithExtensionElement = false;

        while(!readyWithExtensionElement && xtr.hasNext()) {
            xtr.next();
            if (!xtr.isCharacters() && 12 != xtr.getEventType()) {
                if (xtr.isStartElement()) {
                    ExtensionElement childExtensionElement = this.parseExtensionElement(xtr);
                    extensionElement.addChildElement(childExtensionElement);
                } else if (xtr.isEndElement() && extensionElement.getName().equalsIgnoreCase(xtr.getLocalName())) {
                    readyWithExtensionElement = true;
                }
            } else if (StringUtils.isNotEmpty(xtr.getText().trim())) {
                extensionElement.setElementText(xtr.getText().trim());
            }
        }

        return extensionElement;
    }

    protected boolean parseAsync(XMLStreamReader xtr) {
        boolean async = false;
        String asyncString = xtr.getAttributeValue("http://activiti.org/bpmn", "async");
        if ("true".equalsIgnoreCase(asyncString)) {
            async = true;
        }

        return async;
    }

    protected boolean parseNotExclusive(XMLStreamReader xtr) {
        boolean notExclusive = false;
        String exclusiveString = xtr.getAttributeValue("http://activiti.org/bpmn", "exclusive");
        if ("false".equalsIgnoreCase(exclusiveString)) {
            notExclusive = true;
        }

        return notExclusive;
    }

    protected boolean parseForCompensation(XMLStreamReader xtr) {
        boolean isForCompensation = false;
        String compensationString = xtr.getAttributeValue((String)null, "isForCompensation");
        if ("true".equalsIgnoreCase(compensationString)) {
            isForCompensation = true;
        }

        return isForCompensation;
    }

    protected List<String> parseDelimitedList(String expression) {
        return BpmnXMLUtil.parseDelimitedList(expression);
    }

    protected String convertToDelimitedString(List<String> stringList) {
        return BpmnXMLUtil.convertToDelimitedString(stringList);
    }

    protected boolean writeFormProperties(FlowElement flowElement, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
        List<FormProperty> propertyList = null;
        if (flowElement instanceof UserTask) {
            propertyList = ((UserTask)flowElement).getFormProperties();
        } else if (flowElement instanceof StartEvent) {
            propertyList = ((StartEvent)flowElement).getFormProperties();
        }

        if (propertyList != null) {
            Iterator var5 = propertyList.iterator();

            while(true) {
                FormProperty property;
                do {
                    if (!var5.hasNext()) {
                        return didWriteExtensionStartElement;
                    }

                    property = (FormProperty)var5.next();
                } while(!StringUtils.isNotEmpty(property.getId()));

                if (!didWriteExtensionStartElement) {
                    xtw.writeStartElement("extensionElements");
                    didWriteExtensionStartElement = true;
                }

                xtw.writeStartElement("activiti", "formProperty", "http://activiti.org/bpmn");
                this.writeDefaultAttribute("id", property.getId(), xtw);
                this.writeDefaultAttribute("name", property.getName(), xtw);
                this.writeDefaultAttribute("type", property.getType(), xtw);
                this.writeDefaultAttribute("expression", property.getExpression(), xtw);
                this.writeDefaultAttribute("variable", property.getVariable(), xtw);
                this.writeDefaultAttribute("default", property.getDefaultExpression(), xtw);
                this.writeDefaultAttribute("datePattern", property.getDatePattern(), xtw);
                if (!property.isReadable()) {
                    this.writeDefaultAttribute("readable", "false", xtw);
                }

                if (!property.isWriteable()) {
                    this.writeDefaultAttribute("writable", "false", xtw);
                }

                if (property.isRequired()) {
                    this.writeDefaultAttribute("required", "true", xtw);
                }

                Iterator var7 = property.getFormValues().iterator();

                while(var7.hasNext()) {
                    FormValue formValue = (FormValue)var7.next();
                    if (StringUtils.isNotEmpty(formValue.getId())) {
                        xtw.writeStartElement("activiti", "value", "http://activiti.org/bpmn");
                        xtw.writeAttribute("id", formValue.getId());
                        xtw.writeAttribute("name", formValue.getName());
                        xtw.writeEndElement();
                    }
                }

                xtw.writeEndElement();
            }
        } else {
            return didWriteExtensionStartElement;
        }
    }

    protected boolean writeListeners(BaseElement element, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
        return ActivitiListenerExport.writeListeners(element, didWriteExtensionStartElement, xtw);
    }

    protected void writeEventDefinitions(Event parentEvent, List<EventDefinition> eventDefinitions, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        Iterator var5 = eventDefinitions.iterator();

        while(var5.hasNext()) {
            EventDefinition eventDefinition = (EventDefinition)var5.next();
            if (eventDefinition instanceof TimerEventDefinition) {
                this.writeTimerDefinition(parentEvent, (TimerEventDefinition)eventDefinition, xtw);
            } else if (eventDefinition instanceof SignalEventDefinition) {
                this.writeSignalDefinition(parentEvent, (SignalEventDefinition)eventDefinition, xtw);
            } else if (eventDefinition instanceof MessageEventDefinition) {
                this.writeMessageDefinition(parentEvent, (MessageEventDefinition)eventDefinition, model, xtw);
            } else if (eventDefinition instanceof ErrorEventDefinition) {
                this.writeErrorDefinition(parentEvent, (ErrorEventDefinition)eventDefinition, xtw);
            } else if (eventDefinition instanceof TerminateEventDefinition) {
                this.writeTerminateDefinition(parentEvent, (TerminateEventDefinition)eventDefinition, xtw);
            } else if (eventDefinition instanceof CancelEventDefinition) {
                this.writeCancelDefinition(parentEvent, (CancelEventDefinition)eventDefinition, xtw);
            } else if (eventDefinition instanceof CompensateEventDefinition) {
                this.writeCompensateDefinition(parentEvent, (CompensateEventDefinition)eventDefinition, xtw);
            }
        }

    }

    protected void writeTimerDefinition(Event parentEvent, TimerEventDefinition timerDefinition, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("timerEventDefinition");
        if (StringUtils.isNotEmpty(timerDefinition.getCalendarName())) {
            this.writeQualifiedAttribute("businessCalendarName", timerDefinition.getCalendarName(), xtw);
        }

        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(timerDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        if (StringUtils.isNotEmpty(timerDefinition.getTimeDate())) {
            xtw.writeStartElement("timeDate");
            xtw.writeCharacters(timerDefinition.getTimeDate());
            xtw.writeEndElement();
        } else if (StringUtils.isNotEmpty(timerDefinition.getTimeCycle())) {
            xtw.writeStartElement("timeCycle");
            if (StringUtils.isNotEmpty(timerDefinition.getEndDate())) {
                xtw.writeAttribute("activiti", "http://activiti.org/bpmn", "endDate", timerDefinition.getEndDate());
            }

            xtw.writeCharacters(timerDefinition.getTimeCycle());
            xtw.writeEndElement();
        } else if (StringUtils.isNotEmpty(timerDefinition.getTimeDuration())) {
            xtw.writeStartElement("timeDuration");
            xtw.writeCharacters(timerDefinition.getTimeDuration());
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeSignalDefinition(Event parentEvent, SignalEventDefinition signalDefinition, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("signalEventDefinition");
        this.writeDefaultAttribute("signalRef", signalDefinition.getSignalRef(), xtw);
        if (parentEvent instanceof ThrowEvent && signalDefinition.isAsync()) {
            BpmnXMLUtil.writeQualifiedAttribute("async", "true", xtw);
        }

        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(signalDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeCancelDefinition(Event parentEvent, CancelEventDefinition cancelEventDefinition, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("cancelEventDefinition");
        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(cancelEventDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeCompensateDefinition(Event parentEvent, CompensateEventDefinition compensateEventDefinition, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("compensateEventDefinition");
        this.writeDefaultAttribute("activityRef", compensateEventDefinition.getActivityRef(), xtw);
        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(compensateEventDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeMessageDefinition(Event parentEvent, MessageEventDefinition messageDefinition, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("messageEventDefinition");
        String messageRef = messageDefinition.getMessageRef();
        if (StringUtils.isNotEmpty(messageRef)) {
            if (messageRef.startsWith(model.getTargetNamespace())) {
                messageRef = messageRef.replace(model.getTargetNamespace(), "");
                messageRef = messageRef.replaceFirst(":", "");
            } else {
                Iterator var6 = model.getNamespaces().keySet().iterator();

                while(var6.hasNext()) {
                    String prefix = (String)var6.next();
                    String namespace = model.getNamespace(prefix);
                    if (messageRef.startsWith(namespace)) {
                        messageRef = messageRef.replace(model.getTargetNamespace(), "");
                        messageRef = prefix + messageRef;
                    }
                }
            }
        }

        this.writeDefaultAttribute("messageRef", messageRef, xtw);
        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(messageDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeErrorDefinition(Event parentEvent, ErrorEventDefinition errorDefinition, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("errorEventDefinition");
        this.writeDefaultAttribute("errorRef", errorDefinition.getErrorCode(), xtw);
        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(errorDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeTerminateDefinition(Event parentEvent, TerminateEventDefinition terminateDefinition, XMLStreamWriter xtw) throws Exception {
        xtw.writeStartElement("terminateEventDefinition");
        if (terminateDefinition.isTerminateAll()) {
            this.writeQualifiedAttribute("terminateAll", "true", xtw);
        }

        if (terminateDefinition.isTerminateMultiInstance()) {
            this.writeQualifiedAttribute("terminateMultiInstance", "true", xtw);
        }

        boolean didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(terminateDefinition, false, xtw);
        if (didWriteExtensionStartElement) {
            xtw.writeEndElement();
        }

        xtw.writeEndElement();
    }

    protected void writeDefaultAttribute(String attributeName, String value, XMLStreamWriter xtw) throws Exception {
        BpmnXMLUtil.writeDefaultAttribute(attributeName, value, xtw);
    }

    protected void writeQualifiedAttribute(String attributeName, String value, XMLStreamWriter xtw) throws Exception {
        BpmnXMLUtil.writeQualifiedAttribute(attributeName, value, xtw);
    }
}
