package com.runflow.engine.parse.handler;

import com.runflow.engine.bpmn.entity.ProcessDefinitionEntity;
import com.runflow.engine.bpmn.entity.impl.ProcessDefinitionEntityImpl;
import com.runflow.engine.parse.AbstractBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProcessParseHandler extends AbstractBpmnParseHandler<Process> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessParseHandler.class);

    public static final String PROPERTYNAME_DOCUMENTATION = "documentation";





    public Class<? extends BaseElement> getHandledType() {
        return Process.class;
    }

    protected void executeParse(BpmnParse bpmnParse, Process process) {
        if (process.isExecutable()) {
            bpmnParse.getProcessDefinitions().add(transformProcess(bpmnParse, process));
        }
    }

    protected ProcessDefinitionEntity transformProcess(BpmnParse bpmnParse, Process process) {
        ProcessDefinitionEntity currentProcessDefinition = new ProcessDefinitionEntityImpl();
        bpmnParse.setCurrentProcessDefinition(currentProcessDefinition);

        /*
         * Mapping object model - bpmn xml: processDefinition.id -> generated by activiti engine processDefinition.key -> bpmn id (required) processDefinition.name -> bpmn name (optional)
         */
        currentProcessDefinition.setKey(process.getId());
        currentProcessDefinition.setName(process.getName());
        currentProcessDefinition.setCategory(bpmnParse.getBpmnModel().getTargetNamespace());
        currentProcessDefinition.setDescription(process.getDocumentation());
        currentProcessDefinition.setDeploymentId(bpmnParse.getDeployment().getId());

        if (bpmnParse.getDeployment().getEngineVersion() != null) {
            currentProcessDefinition.setEngineVersion(bpmnParse.getDeployment().getEngineVersion());
        }


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Parsing process {}", currentProcessDefinition.getKey());
        }

        bpmnParse.processFlowElements(process.getFlowElements());
        processArtifacts(bpmnParse, process.getArtifacts());

        return currentProcessDefinition;
    }



}

