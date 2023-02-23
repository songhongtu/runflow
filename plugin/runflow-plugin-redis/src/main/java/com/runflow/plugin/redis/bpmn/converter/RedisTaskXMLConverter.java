package com.runflow.plugin.redis.bpmn.converter;

import com.runflow.plugin.redis.constant.RedisTaskConstant;
import com.runflow.plugin.redis.model.RedisTask;
import org.activiti.bpmn.converter.BaseBpmnXMLConverter;
import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class RedisTaskXMLConverter extends BaseBpmnXMLConverter {


    @Override
    protected Class<? extends BaseElement> getBpmnElementType() {
        return RedisTask.class;
    }

    @Override
    protected BaseElement convertXMLToElement(XMLStreamReader xtr, BpmnModel model) throws Exception {
        RedisTask redisTask = new RedisTask();
        BpmnXMLUtil.addXMLLocation(redisTask, xtr);
        String type = xtr.getAttributeValue(null, "type");
        String redisExpression = xtr.getAttributeValue(null, "redisExpression");
        redisTask.setType(type);
        redisTask.setRedisExpression(redisExpression);
        parseChildElements(getXMLElementName(), redisTask, model, xtr);


        return redisTask;
    }


    @Override
    protected String getXMLElementName() {
        return RedisTaskConstant.ELEMENT_TASK_USER;
    }


    protected void writeAdditionalAttributes(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {


    }

    @Override
    protected void writeAdditionalChildElements(BaseElement element, BpmnModel model, XMLStreamWriter xtw) throws Exception {

    }
}
