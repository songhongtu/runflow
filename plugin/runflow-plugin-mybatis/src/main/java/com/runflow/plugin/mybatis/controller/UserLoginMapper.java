package com.runflow.plugin.mybatis.controller;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserLoginMapper {

    @Select("select * from sp_flow")
    List<Map> selectStudentInfoMap();

}
