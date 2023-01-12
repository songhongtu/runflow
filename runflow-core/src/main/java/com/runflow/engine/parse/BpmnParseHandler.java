package com.runflow.engine.parse;

import org.activiti.bpmn.model.BaseElement;

import java.util.Collection;

public interface BpmnParseHandler {


    /**
     * The types for which this handler must be called during process parsing.
     */
    Collection<Class<? extends BaseElement>> getHandledTypes();

    /**
     * The actual delegation method. The parser will calls this method on a match with the {@link #getHandledTypes()} return value.
     *
     * @param bpmnParse
     *          The {@link BpmnParse} instance that acts as container for all things produced during the parsing.
     */
    void parse(BpmnParse bpmnParse, BaseElement element);

}
