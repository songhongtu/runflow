package com.runflow.engine.bpmn.converter;

import com.runflow.engine.bpmn.constants.BpmnXMLConstants;
import com.runflow.engine.bpmn.model.BaseElement;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.stream.*;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.*;

public class BpmnXMLConverter implements BpmnXMLConstants {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BpmnXMLConverter.class);
    protected static final String BPMN_XSD = "org/activiti/impl/bpmn/parser/BPMN20.xsd";
    protected static final String DEFAULT_ENCODING = "UTF-8";
    protected static Map<String, BaseBpmnXMLConverter> convertersToBpmnMap = new HashMap();
    protected static Map<Class<? extends BaseElement>, BaseBpmnXMLConverter> convertersToXMLMap = new HashMap();
    protected ClassLoader classloader;
    protected List<String> userTaskFormTypes;
    protected List<String> startEventFormTypes;
    protected BpmnEdgeParser bpmnEdgeParser = new BpmnEdgeParser();
    protected BpmnShapeParser bpmnShapeParser = new BpmnShapeParser();
    protected DefinitionsParser definitionsParser = new DefinitionsParser();
    protected DocumentationParser documentationParser = new DocumentationParser();
    protected ExtensionElementsParser extensionElementsParser = new ExtensionElementsParser();
    protected ImportParser importParser = new ImportParser();
    protected InterfaceParser interfaceParser = new InterfaceParser();
    protected ItemDefinitionParser itemDefinitionParser = new ItemDefinitionParser();
    protected IOSpecificationParser ioSpecificationParser = new IOSpecificationParser();
    protected DataStoreParser dataStoreParser = new DataStoreParser();
    protected LaneParser laneParser = new LaneParser();
    protected MessageParser messageParser = new MessageParser();
    protected MessageFlowParser messageFlowParser = new MessageFlowParser();
    protected MultiInstanceParser multiInstanceParser = new MultiInstanceParser();
    protected ParticipantParser participantParser = new ParticipantParser();
    protected PotentialStarterParser potentialStarterParser = new PotentialStarterParser();
    protected ProcessParser processParser = new ProcessParser();
    protected ResourceParser resourceParser = new ResourceParser();
    protected SignalParser signalParser = new SignalParser();
    protected SubProcessParser subProcessParser = new SubProcessParser();

    public BpmnXMLConverter() {
    }

    public static void addConverter(BaseBpmnXMLConverter converter) {
        addConverter(converter, converter.getBpmnElementType());
    }

    public static void addConverter(BaseBpmnXMLConverter converter, Class<? extends BaseElement> elementType) {
        convertersToBpmnMap.put(converter.getXMLElementName(), converter);
        convertersToXMLMap.put(elementType, converter);
    }

    public void setClassloader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public void setUserTaskFormTypes(List<String> userTaskFormTypes) {
        this.userTaskFormTypes = userTaskFormTypes;
    }

    public void setStartEventFormTypes(List<String> startEventFormTypes) {
        this.startEventFormTypes = startEventFormTypes;
    }

    public void validateModel(InputStreamProvider inputStreamProvider) throws Exception {
        Schema schema = this.createSchema();
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(inputStreamProvider.getInputStream()));
    }

    public void validateModel(XMLStreamReader xmlStreamReader) throws Exception {
        Schema schema = this.createSchema();
        Validator validator = schema.newValidator();
        validator.validate(new StAXSource(xmlStreamReader));
    }

    protected Schema createSchema() throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = null;
        if (this.classloader != null) {
            schema = factory.newSchema(this.classloader.getResource("org/activiti/impl/bpmn/parser/BPMN20.xsd"));
        }

        if (schema == null) {
            schema = factory.newSchema(BpmnXMLConverter.class.getClassLoader().getResource("org/activiti/impl/bpmn/parser/BPMN20.xsd"));
        }

        if (schema == null) {
            throw new XMLException("BPMN XSD could not be found");
        } else {
            return schema;
        }
    }

    public BpmnModel convertToBpmnModel(InputStreamProvider inputStreamProvider, boolean validateSchema, boolean enableSafeBpmnXml) {
        return this.convertToBpmnModel(inputStreamProvider, validateSchema, enableSafeBpmnXml, "UTF-8");
    }

    public BpmnModel convertToBpmnModel(InputStreamProvider inputStreamProvider, boolean validateSchema, boolean enableSafeBpmnXml, String encoding) {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        if (xif.isPropertySupported("javax.xml.stream.isReplacingEntityReferences")) {
            xif.setProperty("javax.xml.stream.isReplacingEntityReferences", false);
        }

        if (xif.isPropertySupported("javax.xml.stream.isSupportingExternalEntities")) {
            xif.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
        }

        if (xif.isPropertySupported("javax.xml.stream.supportDTD")) {
            xif.setProperty("javax.xml.stream.supportDTD", false);
        }

        InputStreamReader in = null;

        BpmnModel var8;
        try {
            in = new InputStreamReader(inputStreamProvider.getInputStream(), encoding);
            XMLStreamReader xtr = xif.createXMLStreamReader(in);

            try {
                if (validateSchema) {
                    if (!enableSafeBpmnXml) {
                        this.validateModel(inputStreamProvider);
                    } else {
                        this.validateModel(xtr);
                    }

                    in = new InputStreamReader(inputStreamProvider.getInputStream(), encoding);
                    xtr = xif.createXMLStreamReader(in);
                }
            } catch (Exception var19) {
                throw new XMLException(var19.getMessage(), var19);
            }

            var8 = this.convertToBpmnModel(xtr);
        } catch (UnsupportedEncodingException var20) {
            throw new XMLException("The bpmn 2.0 xml is not UTF8 encoded", var20);
        } catch (XMLStreamException var21) {
            throw new XMLException("Error while reading the BPMN 2.0 XML", var21);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var18) {
                    LOGGER.debug("Problem closing BPMN input stream", var18);
                }
            }

        }

        return var8;
    }

    public BpmnModel convertToBpmnModel(XMLStreamReader xtr) {
        BpmnModel model = new BpmnModel();
        model.setStartEventFormTypes(this.startEventFormTypes);
        model.setUserTaskFormTypes(this.userTaskFormTypes);

        try {
            Process activeProcess = null;
            ArrayList activeSubProcessList = new ArrayList();

            while(xtr.hasNext()) {
                try {
                    xtr.next();
                } catch (Exception var9) {
                    LOGGER.debug("Error reading XML document", var9);
                    throw new XMLException("Error reading XML", var9);
                }

                if (xtr.isEndElement() && ("subProcess".equals(xtr.getLocalName()) || "transaction".equals(xtr.getLocalName()) || "adHocSubProcess".equals(xtr.getLocalName()))) {
                    activeSubProcessList.remove(activeSubProcessList.size() - 1);
                }

                if (xtr.isStartElement()) {
                    if ("definitions".equals(xtr.getLocalName())) {
                        this.definitionsParser.parse(xtr, model);
                    } else if ("resource".equals(xtr.getLocalName())) {
                        this.resourceParser.parse(xtr, model);
                    } else if ("signal".equals(xtr.getLocalName())) {
                        this.signalParser.parse(xtr, model);
                    } else if ("message".equals(xtr.getLocalName())) {
                        this.messageParser.parse(xtr, model);
                    } else if ("error".equals(xtr.getLocalName())) {
                        if (StringUtils.isNotEmpty(xtr.getAttributeValue((String)null, "id"))) {
                            model.addError(xtr.getAttributeValue((String)null, "id"), xtr.getAttributeValue((String)null, "errorCode"));
                        }
                    } else if ("import".equals(xtr.getLocalName())) {
                        this.importParser.parse(xtr, model);
                    } else if ("itemDefinition".equals(xtr.getLocalName())) {
                        this.itemDefinitionParser.parse(xtr, model);
                    } else if ("dataStore".equals(xtr.getLocalName())) {
                        this.dataStoreParser.parse(xtr, model);
                    } else if ("interface".equals(xtr.getLocalName())) {
                        this.interfaceParser.parse(xtr, model);
                    } else if ("ioSpecification".equals(xtr.getLocalName())) {
                        this.ioSpecificationParser.parseChildElement(xtr, activeProcess, model);
                    } else if ("participant".equals(xtr.getLocalName())) {
                        this.participantParser.parse(xtr, model);
                    } else if ("messageFlow".equals(xtr.getLocalName())) {
                        this.messageFlowParser.parse(xtr, model);
                    } else if ("process".equals(xtr.getLocalName())) {
                        Process process = this.processParser.parse(xtr, model);
                        if (process != null) {
                            activeProcess = process;
                        }
                    } else if ("potentialStarter".equals(xtr.getLocalName())) {
                        this.potentialStarterParser.parse(xtr, activeProcess);
                    } else if ("lane".equals(xtr.getLocalName())) {
                        this.laneParser.parse(xtr, activeProcess, model);
                    } else if ("documentation".equals(xtr.getLocalName())) {
                        BaseElement parentElement = null;
                        if (!activeSubProcessList.isEmpty()) {
                            parentElement = (BaseElement)activeSubProcessList.get(activeSubProcessList.size() - 1);
                        } else if (activeProcess != null) {
                            parentElement = activeProcess;
                        }

                        this.documentationParser.parseChildElement(xtr, (BaseElement)parentElement, model);
                    } else {
                        String elementId;
                        if (activeProcess == null && "textAnnotation".equals(xtr.getLocalName())) {
                            elementId = xtr.getAttributeValue((String)null, "id");
                            TextAnnotation textAnnotation = (TextAnnotation)(new TextAnnotationXMLConverter()).convertXMLToElement(xtr, model);
                            textAnnotation.setId(elementId);
                            model.getGlobalArtifacts().add(textAnnotation);
                        } else if (activeProcess == null && "association".equals(xtr.getLocalName())) {
                            elementId = xtr.getAttributeValue((String)null, "id");
                            Association association = (Association)(new AssociationXMLConverter()).convertXMLToElement(xtr, model);
                            association.setId(elementId);
                            model.getGlobalArtifacts().add(association);
                        } else if ("extensionElements".equals(xtr.getLocalName())) {
                            this.extensionElementsParser.parse(xtr, activeSubProcessList, activeProcess, model);
                        } else if (!"subProcess".equals(xtr.getLocalName()) && !"transaction".equals(xtr.getLocalName()) && !"adHocSubProcess".equals(xtr.getLocalName())) {
                            if ("completionCondition".equals(xtr.getLocalName())) {
                                if (!activeSubProcessList.isEmpty()) {
                                    SubProcess subProcess = (SubProcess)activeSubProcessList.get(activeSubProcessList.size() - 1);
                                    if (subProcess instanceof AdhocSubProcess) {
                                        AdhocSubProcess adhocSubProcess = (AdhocSubProcess)subProcess;
                                        adhocSubProcess.setCompletionCondition(xtr.getElementText());
                                    }
                                }
                            } else if ("BPMNShape".equals(xtr.getLocalName())) {
                                this.bpmnShapeParser.parse(xtr, model);
                            } else if ("BPMNEdge".equals(xtr.getLocalName())) {
                                this.bpmnEdgeParser.parse(xtr, model);
                            } else if (!activeSubProcessList.isEmpty() && "multiInstanceLoopCharacteristics".equalsIgnoreCase(xtr.getLocalName())) {
                                this.multiInstanceParser.parseChildElement(xtr, (BaseElement)activeSubProcessList.get(activeSubProcessList.size() - 1), model);
                            } else if (convertersToBpmnMap.containsKey(xtr.getLocalName()) && activeProcess != null) {
                                BaseBpmnXMLConverter converter = (BaseBpmnXMLConverter)convertersToBpmnMap.get(xtr.getLocalName());
                                converter.convertToBpmnModel(xtr, model, activeProcess, activeSubProcessList);
                            }
                        } else {
                            this.subProcessParser.parse(xtr, activeSubProcessList, activeProcess);
                        }
                    }
                }
            }

            Iterator var19 = model.getProcesses().iterator();

            while(var19.hasNext()) {
                Process process = (Process)var19.next();
                Iterator var7 = model.getPools().iterator();

                while(var7.hasNext()) {
                    Pool pool = (Pool)var7.next();
                    if (process.getId().equals(pool.getProcessRef())) {
                        pool.setExecutable(process.isExecutable());
                    }
                }

                this.processFlowElements(process.getFlowElements(), process);
            }

            return model;
        } catch (XMLException var10) {
            throw var10;
        } catch (Exception var11) {
            LOGGER.error("Error processing BPMN document", var11);
            throw new XMLException("Error processing BPMN document", var11);
        }
    }

    protected void processFlowElements(Collection<FlowElement> flowElementList, BaseElement parentScope) {
        Iterator var3 = flowElementList.iterator();

        while(var3.hasNext()) {
            FlowElement flowElement = (FlowElement)var3.next();
            FlowNode attachedToElement;
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow)flowElement;
                attachedToElement = this.getFlowNodeFromScope(sequenceFlow.getSourceRef(), parentScope);
                if (attachedToElement != null) {
                    attachedToElement.getOutgoingFlows().add(sequenceFlow);
                    sequenceFlow.setSourceFlowElement(attachedToElement);
                }

                FlowNode targetNode = this.getFlowNodeFromScope(sequenceFlow.getTargetRef(), parentScope);
                if (targetNode != null) {
                    targetNode.getIncomingFlows().add(sequenceFlow);
                    sequenceFlow.setTargetFlowElement(targetNode);
                }
            } else if (flowElement instanceof BoundaryEvent) {
                BoundaryEvent boundaryEvent = (BoundaryEvent)flowElement;
                attachedToElement = this.getFlowNodeFromScope(boundaryEvent.getAttachedToRefId(), parentScope);
                if (attachedToElement instanceof Activity) {
                    Activity attachedActivity = (Activity)attachedToElement;
                    boundaryEvent.setAttachedToRef(attachedActivity);
                    attachedActivity.getBoundaryEvents().add(boundaryEvent);
                }
            } else if (flowElement instanceof SubProcess) {
                SubProcess subProcess = (SubProcess)flowElement;
                this.processFlowElements(subProcess.getFlowElements(), subProcess);
            }
        }

    }

    protected FlowNode getFlowNodeFromScope(String elementId, BaseElement scope) {
        FlowNode flowNode = null;
        if (StringUtils.isNotEmpty(elementId)) {
            if (scope instanceof Process) {
                flowNode = (FlowNode)((Process)scope).getFlowElement(elementId);
            } else if (scope instanceof SubProcess) {
                flowNode = (FlowNode)((SubProcess)scope).getFlowElement(elementId);
            }
        }

        return flowNode;
    }

    public byte[] convertToXML(BpmnModel model) {
        return this.convertToXML(model, "UTF-8");
    }

    public byte[] convertToXML(BpmnModel model, String encoding) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            OutputStreamWriter out = new OutputStreamWriter(outputStream, encoding);
            XMLStreamWriter writer = xof.createXMLStreamWriter(out);
            XMLStreamWriter xtw = new IndentingXMLStreamWriter(writer);
            DefinitionsRootExport.writeRootElement(model, xtw, encoding);
            CollaborationExport.writePools(model, xtw);
            DataStoreExport.writeDataStores(model, xtw);
            SignalAndMessageDefinitionExport.writeSignalsAndMessages(model, xtw);
            Iterator var8 = model.getProcesses().iterator();

            while(true) {
                Process process;
                do {
                    if (!var8.hasNext()) {
                        BPMNDIExport.writeBPMNDI(model, xtw);
                        xtw.writeEndElement();
                        xtw.writeEndDocument();
                        xtw.flush();
                        outputStream.close();
                        xtw.close();
                        return outputStream.toByteArray();
                    }

                    process = (Process)var8.next();
                } while(process.getFlowElements().isEmpty() && process.getLanes().isEmpty());

                ProcessExport.writeProcess(process, xtw);
                Iterator var10 = process.getFlowElements().iterator();

                while(var10.hasNext()) {
                    FlowElement flowElement = (FlowElement)var10.next();
                    this.createXML((FlowElement)flowElement, model, xtw);
                }

                var10 = process.getArtifacts().iterator();

                while(var10.hasNext()) {
                    Artifact artifact = (Artifact)var10.next();
                    this.createXML((Artifact)artifact, model, xtw);
                }

                xtw.writeEndElement();
            }
        } catch (Exception var12) {
            LOGGER.error("Error writing BPMN XML", var12);
            throw new XMLException("Error writing BPMN XML", var12);
        }
    }

    protected void createXML(FlowElement flowElement, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        if (flowElement instanceof SubProcess) {
            SubProcess subProcess = (SubProcess)flowElement;
            if (flowElement instanceof Transaction) {
                xtw.writeStartElement("transaction");
            } else if (flowElement instanceof AdhocSubProcess) {
                xtw.writeStartElement("adHocSubProcess");
            } else {
                xtw.writeStartElement("subProcess");
            }

            xtw.writeAttribute("id", subProcess.getId());
            if (StringUtils.isNotEmpty(subProcess.getName())) {
                xtw.writeAttribute("name", subProcess.getName());
            } else {
                xtw.writeAttribute("name", "subProcess");
            }

            if (subProcess instanceof EventSubProcess) {
                xtw.writeAttribute("triggeredByEvent", "true");
            } else if (!(subProcess instanceof Transaction)) {
                if (subProcess.isAsynchronous()) {
                    BpmnXMLUtil.writeQualifiedAttribute("async", "true", xtw);
                    if (subProcess.isNotExclusive()) {
                        BpmnXMLUtil.writeQualifiedAttribute("exclusive", "false", xtw);
                    }
                }
            } else if (subProcess instanceof AdhocSubProcess) {
                AdhocSubProcess adhocSubProcess = (AdhocSubProcess)subProcess;
                BpmnXMLUtil.writeDefaultAttribute("cancelRemainingInstances", String.valueOf(adhocSubProcess.isCancelRemainingInstances()), xtw);
                if (StringUtils.isNotEmpty(adhocSubProcess.getOrdering())) {
                    BpmnXMLUtil.writeDefaultAttribute("ordering", adhocSubProcess.getOrdering(), xtw);
                }
            }

            if (StringUtils.isNotEmpty(subProcess.getDocumentation())) {
                xtw.writeStartElement("documentation");
                xtw.writeCharacters(subProcess.getDocumentation());
                xtw.writeEndElement();
            }

            boolean didWriteExtensionStartElement = ActivitiListenerExport.writeListeners(subProcess, false, xtw);
            didWriteExtensionStartElement = BpmnXMLUtil.writeExtensionElements(subProcess, didWriteExtensionStartElement, model.getNamespaces(), xtw);
            if (didWriteExtensionStartElement) {
                xtw.writeEndElement();
            }

            MultiInstanceExport.writeMultiInstance(subProcess, xtw);
            if (subProcess instanceof AdhocSubProcess) {
                AdhocSubProcess adhocSubProcess = (AdhocSubProcess)subProcess;
                if (StringUtils.isNotEmpty(adhocSubProcess.getCompletionCondition())) {
                    xtw.writeStartElement("completionCondition");
                    xtw.writeCData(adhocSubProcess.getCompletionCondition());
                    xtw.writeEndElement();
                }
            }

            Iterator var10 = subProcess.getFlowElements().iterator();

            while(var10.hasNext()) {
                FlowElement subElement = (FlowElement)var10.next();
                this.createXML(subElement, model, xtw);
            }

            var10 = subProcess.getArtifacts().iterator();

            while(var10.hasNext()) {
                Artifact artifact = (Artifact)var10.next();
                this.createXML(artifact, model, xtw);
            }

            xtw.writeEndElement();
        } else {
            BaseBpmnXMLConverter converter = (BaseBpmnXMLConverter)convertersToXMLMap.get(flowElement.getClass());
            if (converter == null) {
                throw new XMLException("No converter for " + flowElement.getClass() + " found");
            }

            converter.convertToXML(xtw, flowElement, model);
        }

    }

    protected void createXML(Artifact artifact, BpmnModel model, XMLStreamWriter xtw) throws Exception {
        BaseBpmnXMLConverter converter = (BaseBpmnXMLConverter)convertersToXMLMap.get(artifact.getClass());
        if (converter == null) {
            throw new XMLException("No converter for " + artifact.getClass() + " found");
        } else {
            converter.convertToXML(xtw, artifact, model);
        }
    }

    static {
        addConverter(new EndEventXMLConverter());
        addConverter(new StartEventXMLConverter());
        addConverter(new BusinessRuleTaskXMLConverter());
        addConverter(new ManualTaskXMLConverter());
        addConverter(new ReceiveTaskXMLConverter());
        addConverter(new ScriptTaskXMLConverter());
        addConverter(new ServiceTaskXMLConverter());
        addConverter(new SendTaskXMLConverter());
        addConverter(new UserTaskXMLConverter());
        addConverter(new TaskXMLConverter());
        addConverter(new CallActivityXMLConverter());
        addConverter(new EventGatewayXMLConverter());
        addConverter(new ExclusiveGatewayXMLConverter());
        addConverter(new InclusiveGatewayXMLConverter());
        addConverter(new ParallelGatewayXMLConverter());
        addConverter(new ComplexGatewayXMLConverter());
        addConverter(new SequenceFlowXMLConverter());
        addConverter(new CatchEventXMLConverter());
        addConverter(new ThrowEventXMLConverter());
        addConverter(new BoundaryEventXMLConverter());
        addConverter(new TextAnnotationXMLConverter());
        addConverter(new AssociationXMLConverter());
        addConverter(new DataStoreReferenceXMLConverter());
        addConverter(new ValuedDataObjectXMLConverter(), StringDataObject.class);
        addConverter(new ValuedDataObjectXMLConverter(), BooleanDataObject.class);
        addConverter(new ValuedDataObjectXMLConverter(), IntegerDataObject.class);
        addConverter(new ValuedDataObjectXMLConverter(), LongDataObject.class);
        addConverter(new ValuedDataObjectXMLConverter(), DoubleDataObject.class);
        addConverter(new ValuedDataObjectXMLConverter(), DateDataObject.class);
        addConverter(new AlfrescoStartEventXMLConverter());
        addConverter(new AlfrescoUserTaskXMLConverter());
    }
}
