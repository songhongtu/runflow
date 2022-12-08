package com.runflow.engine.parse;

import com.runflow.engine.ActivitiException;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.DataObject;
import org.activiti.bpmn.model.FlowElement;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BpmnParseHandlers {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BpmnParseHandlers.class);

    protected Map<Class<? extends BaseElement>, List<BpmnParseHandler>> parseHandlers;

    public BpmnParseHandlers() {
        this.parseHandlers = new HashMap<Class<? extends BaseElement>, List<BpmnParseHandler>>();
    }

    public List<BpmnParseHandler> getHandlersFor(Class<? extends BaseElement> clazz) {
        return parseHandlers.get(clazz);
    }

    public void addHandlers(List<BpmnParseHandler> bpmnParseHandlers) {
        for (BpmnParseHandler bpmnParseHandler : bpmnParseHandlers) {
            addHandler(bpmnParseHandler);
        }
    }

    public void addHandler(BpmnParseHandler bpmnParseHandler) {
        for (Class<? extends BaseElement> type : bpmnParseHandler.getHandledTypes()) {
            List<BpmnParseHandler> handlers = parseHandlers.get(type);
            if (handlers == null) {
                handlers = new ArrayList<BpmnParseHandler>();
                parseHandlers.put(type, handlers);
            }
            handlers.add(bpmnParseHandler);
        }
    }

    public void parseElement(BpmnParse bpmnParse, BaseElement element) {

        if (element instanceof DataObject) {
            // ignore DataObject elements because they are processed on Process
            // and Sub process level
            return;
        }

        if (element instanceof FlowElement) {
            bpmnParse.setCurrentFlowElement((FlowElement) element);
        }

        // Execute parse handlers
        List<BpmnParseHandler> handlers = parseHandlers.get(element.getClass());

        if (handlers == null) {
            throw new ActivitiException("不支持处理类型："+element.getClass());
        } else {
            for (BpmnParseHandler handler : handlers) {
                handler.parse(bpmnParse, element);
            }
        }
    }

}
