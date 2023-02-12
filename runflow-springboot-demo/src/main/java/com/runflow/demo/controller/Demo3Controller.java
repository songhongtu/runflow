package com.runflow.demo.controller;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Demo3Controller {

    @Autowired
    private RunTimeServiceImpl runTimeService;

    @GetMapping("/demo3")
    public Map<String, Object> demo2() {
        return runTimeService.startWorkflow("Process_1676186873054").getVariableInstances();
    }

}
