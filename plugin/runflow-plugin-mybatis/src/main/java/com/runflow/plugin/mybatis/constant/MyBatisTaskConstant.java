package com.runflow.plugin.mybatis.constant;

public interface MyBatisTaskConstant {

    //是否分页 0 是 1否
    int DEFAULT_PAGESIZE = 10;
    int DEFAULT_PAGENUM = 1;


    String ELEMENT_TASK_USER = "myBatisTask";


    enum SelectTypeEnum {
        SELECTONE, SELECTLIST, INSERT, DELETE;
    }


}
