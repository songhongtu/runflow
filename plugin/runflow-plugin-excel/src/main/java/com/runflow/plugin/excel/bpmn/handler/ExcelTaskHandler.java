package com.runflow.plugin.excel.bpmn.handler;

import com.runflow.engine.parse.AbstractActivityBpmnParseHandler;
import com.runflow.engine.parse.BpmnParse;
import com.runflow.plugin.excel.behavior.ExcelTaskBehavior;
import com.runflow.plugin.excel.model.ExcelTask;
import org.activiti.bpmn.model.BaseElement;

public class ExcelTaskHandler extends AbstractActivityBpmnParseHandler<ExcelTask> {

    public Class<? extends BaseElement> getHandledType() {
        return ExcelTask.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, ExcelTask redisTask) {
        redisTask.setBehavior(new ExcelTaskBehavior(redisTask));
    }

}