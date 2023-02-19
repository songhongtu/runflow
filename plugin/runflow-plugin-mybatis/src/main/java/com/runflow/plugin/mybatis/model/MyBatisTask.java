package com.runflow.plugin.mybatis.model;

import com.runflow.engine.bpmn.entity.impl.CustomTaskFormPropertyTask;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyBatisTask extends CustomTaskFormPropertyTask {

    private String selectType;
    private String statementId;
    private int pageNum;
    private int pageSize;
    private boolean isPage;


    private Map<String, Object> param;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public boolean isPage() {
        return isPage;
    }

    public void setPage(boolean page) {
        isPage = page;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }


    public List<FormProperty> getFormProperties() {
        return formProperties;
    }

    public void setFormProperties(List<FormProperty> formProperties) {
        this.formProperties = formProperties;
    }

    @Override
    public FlowElement clone() {
        return null;
    }
}
