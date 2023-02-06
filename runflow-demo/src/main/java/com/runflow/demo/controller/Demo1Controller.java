package com.runflow.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runflow.engine.Application;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Demo1Controller {

    private static final Logger logger = LoggerFactory.getLogger(Demo1Controller.class);
    @Autowired
    private RunTimeServiceImpl runTimeService;
    @GetMapping("/demo1")
    @ResponseBody
    public Object demo1() throws JsonProcessingException {
        //Process_16719365975 是bpmn 文件中的  process id="Process_16719365975"
        ExecutionEntityImpl executionEntity = runTimeService.startWorkflow("Process_16719365975");
        return executionEntity.getVariableInstances();
    }

}
