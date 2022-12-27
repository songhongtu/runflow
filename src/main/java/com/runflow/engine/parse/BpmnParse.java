package com.runflow.engine.parse;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.util.io.InputStreamSource;
import com.runflow.engine.util.io.StreamSource;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

public class BpmnParse {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BpmnParse.class);
    protected FlowElement currentFlowElement;
    protected Map<String, SequenceFlow> sequenceFlows;
    protected BpmnModel bpmnModel;
    protected BpmnParseHandlers bpmnParserHandlers;
    protected StreamSource streamSource;
    protected String name;
    protected String sourceSystemId;
    protected Process currentProcess;
    protected DeploymentEntity deployment;
    protected ProcessDefinitionEntity currentProcessDefinition;

    public DeploymentEntity getDeployment() {
        return deployment;
    }

    public void setDeployment(DeploymentEntity deployment) {
        this.deployment = deployment;
    }

    /**
     * The end result of the parsing: a list of process definition.
     */
    protected List<ProcessDefinitionEntity> processDefinitions = new ArrayList<ProcessDefinitionEntity>();


    public BpmnParse deployment(DeploymentEntity deployment) {
        this.deployment = deployment;
        return this;
    }


    public BpmnParse(BpmnParser parser) {
        this.bpmnParserHandlers = parser.getBpmnParserHandlers();
    }


    public List<ProcessDefinitionEntity> getProcessDefinitions() {
        return processDefinitions;
    }

    public BpmnParse execute() {

        BpmnXMLConverter converter = new BpmnXMLConverter();

            bpmnModel = converter.convertToBpmnModel(streamSource, true, false, "utf-8");



        ProcessValidator processValidator = null;
        if (processValidator == null) {
            processValidator = new ProcessValidatorFactory().createDefaultProcessValidator();
        }
        List<ValidationError> validationErrors = processValidator.validate(bpmnModel);
        if (validationErrors != null && !validationErrors.isEmpty()) {

            StringBuilder warningBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();

            for (ValidationError error : validationErrors) {
                if (error.isWarning()) {
                    warningBuilder.append(error.toString());
                    warningBuilder.append("\n");
                } else {
                    errorBuilder.append(error.toString());
                    errorBuilder.append("\n");
                }
            }

            // Throw exception if there is any error
            if (errorBuilder.length() > 0) {
                throw new RunFlowException("Errors while parsing:\n" + errorBuilder.toString());
            }

            // Write out warnings (if any)
            if (warningBuilder.length() > 0) {
                LOGGER.warn("Following warnings encountered during process validation: " + warningBuilder.toString());
            }

        }
        applyParseHandlers();
        List<Process> processes = bpmnModel.getProcesses();

        for (Process p : processes) {
            if (p.isExecutable()) {
                this.processFlowElements(p.getFlowElements());
            }

        }

        processDI();



        return this;
    }








    public void createBPMNEdge(String key, List<GraphicInfo> graphicList) {
        FlowElement flowElement = bpmnModel.getFlowElement(key);
        if (flowElement instanceof SequenceFlow) {
            SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
            List<Integer> waypoints = new ArrayList<Integer>();
            for (GraphicInfo waypointInfo : graphicList) {
                waypoints.add((int) waypointInfo.getX());
                waypoints.add((int) waypointInfo.getY());
            }
            sequenceFlow.setWaypoints(waypoints);

        } else if (bpmnModel.getArtifact(key) != null) {
            // it's an association, so nothing to do
        } else {
            LOGGER.warn("Invalid reference in 'bpmnElement' attribute, sequenceFlow " + key + " not found");
        }
    }


    public void processDI() {

        if (processDefinitions.isEmpty()) {
            return;
        }

        if (!bpmnModel.getLocationMap().isEmpty()) {

            // Verify if all referenced elements exist
            for (String bpmnReference : bpmnModel.getLocationMap().keySet()) {
                if (bpmnModel.getFlowElement(bpmnReference) == null) {
                    // ACT-1625: don't warn when artifacts are referenced from DI
                    if (bpmnModel.getArtifact(bpmnReference) == null) {
                        // Check if it's a Pool or Lane, then DI is ok
                        if (bpmnModel.getPool(bpmnReference) == null && bpmnModel.getLane(bpmnReference) == null) {
                            LOGGER.warn("Invalid reference in diagram interchange definition: could not find " + bpmnReference);
                        }
                    }
                } else if (!(bpmnModel.getFlowElement(bpmnReference) instanceof FlowNode)) {
                    LOGGER.warn("Invalid reference in diagram interchange definition: " + bpmnReference + " does not reference a flow node");
                }
            }

            for (String bpmnReference : bpmnModel.getFlowLocationMap().keySet()) {
                if (bpmnModel.getFlowElement(bpmnReference) == null) {
                    // ACT-1625: don't warn when artifacts are referenced from DI
                    if (bpmnModel.getArtifact(bpmnReference) == null) {
                        LOGGER.warn("Invalid reference in diagram interchange definition: could not find " + bpmnReference);
                    }
                } else if (!(bpmnModel.getFlowElement(bpmnReference) instanceof SequenceFlow)) {
                    LOGGER.warn("Invalid reference in diagram interchange definition: " + bpmnReference + " does not reference a sequence flow");
                }
            }

            for (Process process : bpmnModel.getProcesses()) {
                if (!process.isExecutable()) {
                    continue;
                }

                // Parse diagram interchange information
                ProcessDefinitionEntity processDefinition = getProcessDefinition(process.getId());
                if (processDefinition != null) {
                    processDefinition.setGraphicalNotationDefined(true);

                    for (String edgeId : bpmnModel.getFlowLocationMap().keySet()) {
                        if (bpmnModel.getFlowElement(edgeId) != null) {
                            createBPMNEdge(edgeId, bpmnModel.getFlowLocationGraphicInfo(edgeId));
                        }
                    }
                }
            }
        }
    }

    public ProcessDefinitionEntity getProcessDefinition(String processDefinitionKey) {
        for (ProcessDefinitionEntity processDefinition : processDefinitions) {
            if (processDefinition.getKey().equals(processDefinitionKey)) {
                return processDefinition;
            }
        }
        return null;
    }

    protected void applyParseHandlers() {
        sequenceFlows = new HashMap<String, SequenceFlow>();
        for (Process process : bpmnModel.getProcesses()) {
            currentProcess = process;
            if (process.isExecutable()) {
                bpmnParserHandlers.parseElement(this, process);
            }
        }
    }


    public BpmnParse name(String name) {
        this.name = name;
        return this;
    }

    public BpmnParse setSourceSystemId(String sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
        return this;
    }


    public BpmnParse sourceInputStream(InputStream inputStream) {
        if (name == null) {
            name("inputStream");
        }
        InputStreamSource inputStreamSource = new InputStreamSource(inputStream);
        setStreamSource(inputStreamSource);
        return this;
    }


    protected void setStreamSource(StreamSource streamSource) {
        if (this.streamSource != null) {
            throw new RunFlowException("invalid: multiple sources " + this.streamSource + " and " + streamSource);
        }
        this.streamSource = streamSource;
    }


    public void processFlowElements(Collection<FlowElement> flowElements) {

        // Parsing the elements is done in a strict order of types,
        // as otherwise certain information might not be available when parsing
        // a certain type.

        // Using lists as we want to keep the order in which they are defined
        List<SequenceFlow> sequenceFlowToParse = new ArrayList<SequenceFlow>();
        List<BoundaryEvent> boundaryEventsToParse = new ArrayList<BoundaryEvent>();

        // Flow elements that depend on other elements are parse after the first run-through
        List<FlowElement> defferedFlowElementsToParse = new ArrayList<FlowElement>();

        // Activities are parsed first
        for (FlowElement flowElement : flowElements) {

            // Sequence flow are also flow elements, but are only parsed once every activity is found
            if (flowElement instanceof SequenceFlow) {
                sequenceFlowToParse.add((SequenceFlow) flowElement);
            } else if (flowElement instanceof BoundaryEvent) {
                boundaryEventsToParse.add((BoundaryEvent) flowElement);
            } else if (flowElement instanceof Event) {
                defferedFlowElementsToParse.add(flowElement);
            } else {
                bpmnParserHandlers.parseElement(this, flowElement);
            }

        }

        // Deferred elements
        for (FlowElement flowElement : defferedFlowElementsToParse) {
            bpmnParserHandlers.parseElement(this, flowElement);
        }

        // Boundary events are parsed after all the regular activities are parsed
        for (BoundaryEvent boundaryEvent : boundaryEventsToParse) {
            bpmnParserHandlers.parseElement(this, boundaryEvent);
        }

        // sequence flows
        for (SequenceFlow sequenceFlow : sequenceFlowToParse) {
            bpmnParserHandlers.parseElement(this, sequenceFlow);
        }

    }


    public BpmnModel getBpmnModel() {
        return bpmnModel;
    }

    public void setBpmnModel(BpmnModel bpmnModel) {
        this.bpmnModel = bpmnModel;
    }

    public FlowElement getCurrentFlowElement() {
        return currentFlowElement;
    }

    public void setCurrentFlowElement(FlowElement currentFlowElement) {
        this.currentFlowElement = currentFlowElement;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
    }

    public ProcessDefinitionEntity getCurrentProcessDefinition() {
        return currentProcessDefinition;
    }

    public void setCurrentProcessDefinition(ProcessDefinitionEntity currentProcessDefinition) {
        this.currentProcessDefinition = currentProcessDefinition;
    }
}
