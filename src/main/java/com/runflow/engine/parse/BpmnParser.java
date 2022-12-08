package com.runflow.engine.parse;

public class BpmnParser {


    protected BpmnParseHandlers bpmnParserHandlers;

    /**
     * Creates a new {@link BpmnParse} instance that can be used to parse only one BPMN 2.0 process definition.
     */


    public BpmnParseHandlers getBpmnParserHandlers() {
        return bpmnParserHandlers;
    }

    public void setBpmnParserHandlers(BpmnParseHandlers bpmnParserHandlers) {
        this.bpmnParserHandlers = bpmnParserHandlers;
    }


}
