package com.runflow.engine.parse;

import org.activiti.bpmn.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractBpmnParseHandler<T extends BaseElement> implements BpmnParseHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBpmnParseHandler.class);

    public Set<Class<? extends BaseElement>> getHandledTypes() {
        Set<Class<? extends BaseElement>> types = new HashSet<Class<? extends BaseElement>>();
        types.add(getHandledType());
        return types;
    }

    protected abstract Class<? extends BaseElement> getHandledType();

    @SuppressWarnings("unchecked")
    public void parse(BpmnParse bpmnParse, BaseElement element) {
        T baseElement = (T) element;
        executeParse(bpmnParse, baseElement);
    }

    protected abstract void executeParse(BpmnParse bpmnParse, T element);


    protected void processArtifacts(BpmnParse bpmnParse, Collection<Artifact> artifacts) {
        // associations
        for (Artifact artifact : artifacts) {
            if (artifact instanceof Association) {
                createAssociation(bpmnParse, (Association) artifact);
            }
        }
    }

    protected void createAssociation(BpmnParse bpmnParse, Association association) {
        BpmnModel bpmnModel = bpmnParse.getBpmnModel();
        if (bpmnModel.getArtifact(association.getSourceRef()) != null || bpmnModel.getArtifact(association.getTargetRef()) != null) {

            // connected to a text annotation so skipping it
            return;
        }
    }
}
