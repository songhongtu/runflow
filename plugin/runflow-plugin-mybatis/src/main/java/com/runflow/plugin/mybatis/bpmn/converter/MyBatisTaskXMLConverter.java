package com.runflow.plugin.mybatis.bpmn.converter;

import com.runflow.engine.bpmn.converter.CustomBaseBpmnXMLConverter;
import com.runflow.engine.bpmn.converter.CustomFormPropertyParser;
import com.runflow.engine.utils.Conv;
import com.runflow.plugin.mybatis.constant.MyBatisTaskConstant;
import com.runflow.plugin.mybatis.model.MyBatisTask;
import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.child.BaseChildElementParser;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionElement;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class MyBatisTaskXMLConverter extends CustomBaseBpmnXMLConverter {


    @Override
    protected Class<? extends BaseElement> getBpmnElementType() {
        return MyBatisTask.class;
    }

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        MyBatisTask mybatisTask = new MyBatisTask();
        BpmnXMLUtil.addXMLLocation(mybatisTask, xtr);
        String selectType = xtr.getAttributeValue(null, "selectType");
        String statementId = xtr.getAttributeValue(null, "statementId");
        int pageNum = Conv.NI(xtr.getAttributeValue(null, "pageNum"));
        int pageSize = Conv.NI(xtr.getAttributeValue(null, "pageSize"));
        boolean isPage = Conv.NB(xtr.getAttributeValue(null, "isPage"));
        mybatisTask.setSelectType(selectType);
        mybatisTask.setStatementId(statementId);
        mybatisTask.setPageNum(pageNum);
        mybatisTask.setPageSize(pageSize);
        mybatisTask.setPage(isPage);
        parseChildElements(getXMLElementName(), mybatisTask, model, xtr);


        return mybatisTask;
    }


    @Override
    protected String getXMLElementName() {
        return MyBatisTaskConstant.ELEMENT_TASK_USER;
    }


    protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {


    }

    @Override
    protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {

    }
}
