package com.runflow.plugin.mybatis.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
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
    public PageInfo<Object> demo1() {
        PageHelper.startPage(1, 1);
        Map map = new HashMap();
        map.put("cc", 1);
        List<Object> list = sqlSession.selectList("com.runflow.plugin.mybatis.dao.RunFlowDao.getUserList1", map);
        PageInfo<Object> objectPageInfo = new PageInfo<>(list);
        return objectPageInfo;
    }


}
