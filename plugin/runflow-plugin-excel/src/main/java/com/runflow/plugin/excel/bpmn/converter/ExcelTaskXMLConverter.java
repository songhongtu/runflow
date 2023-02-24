package com.runflow.plugin.excel.bpmn.converter;

import com.runflow.plugin.excel.constant.ExcelTaskConstant;
import com.runflow.plugin.excel.model.ExcelTask;
import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class ExcelTaskXMLConverter extends BaseBpmnXMLConverter {


    @Override
    protected Class<? extends BaseElement> getBpmnElementType() {
        return ExcelTask.class;
    }

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        ExcelTask excelTask = new ExcelTask();
        BpmnXMLUtil.addXMLLocation(excelTask, xtr);
        String excelExpression = xtr.getAttributeValue(null, "excelExpression");
        excelTask.setExcelExpression(excelExpression);
        parseChildElements(getXMLElementName(), excelTask, model, xtr);
        return excelTask;
    }


    @Override
    protected String getXMLElementName() {
        return ExcelTaskConstant.ELEMENT_TASK_USER;
    }


    protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {


    }

    @Override
    protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {

    }
}
