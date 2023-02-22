package com.runflow.plugin.mybatis.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.spring.boot.SpringContextUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MybatisController {
    @Autowired
    UserLoginMapper userLoginMapper;

    @Autowired
    SqlSession sqlSession;


    @Transactional
    @GetMapping("/demo1")
    public Map<String, Object> demo1() {
        RunTimeServiceImpl bean = SpringContextUtil.getBean(RunTimeServiceImpl.class);
        ExecutionEntityImpl executionEntity = bean.startWorkflow("Process_1677031768578");
        return executionEntity.getVariableInstances();
    }


}
