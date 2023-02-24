package com.runflow.plugin.excel.behavior;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.behavior.TaskActivityBehavior;
import com.runflow.engine.behavior.UserTaskActivityBehavior;
import com.runflow.engine.utils.BpmnUtils;
import com.runflow.engine.utils.ConditionUtil;
import com.runflow.engine.utils.Conv;
import com.runflow.plugin.excel.constant.ExcelTaskConstant;
import com.runflow.plugin.excel.model.ExcelTask;
import com.runflow.plugin.excel.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

public class ExcelTaskBehavior extends TaskActivityBehavior implements ExcelTaskConstant {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskActivityBehavior.class);

    protected ExcelTask excelTask;

    public ExcelTaskBehavior(ExcelTask excelTask) {
        this.excelTask = excelTask;
    }

    @Override
    public void execute(ExecutionEntityImpl execution) {

        String name = execution.getCurrentFlowElement().getName();
        LOGGER.debug("excel任务  名称：{}  id:{}  线程名称:{} ", name, execution.getId(), Thread.currentThread().getName());
        final Map<String, String> extensionElementPropertity = BpmnUtils.getExtensionElementPropertity(execution.getCurrentFlowElement().getExtensionElements());
        final String excelExpression = excelTask.getExcelExpression();
        if (extensionElementPropertity != null && excelExpression != null) {
            Set<String> strings = extensionElementPropertity.keySet();
            final Collection<String> values = extensionElementPropertity.values();
            List<List> dataList = new ArrayList<>();
            List expressionList = (List) ConditionUtil.createExpression(excelExpression, execution);
            for (Object o : expressionList) {
                if (o instanceof Map) {
                    List<String> list = new ArrayList<>();
                    for (String ve : values) {
                        Object propertyValue = ((Map) o).get(ve);
                        list.add(Conv.NS(propertyValue));
                    }
                    dataList.add(list);

                } else {

                    final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(o);
                    List<String> list = new ArrayList<>();
                    for (String ve : values) {
                        Object propertyValue = beanWrapper.getPropertyValue(ve);
                        list.add(Conv.NS(propertyValue));
                    }
                    dataList.add(list);

                }


            }
            final Workbook workbook = ExcelUtils.getWorkbook(new ArrayList<>(strings), dataList);
            execution.findRootParent(execution).variableInstances.put(execution.getCurrentFlowElement().getId(), workbook);

        }
        leave(execution);
    }
}
