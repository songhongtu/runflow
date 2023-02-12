package com.runflow.demo.controller;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Demo2Controller {
    @Autowired
    private RunTimeServiceImpl runTimeService;
    @GetMapping("/demo2")
    public Map<String, Object> demo2() {
        AtomicInteger integer = new AtomicInteger(0);
        Map initMap = new HashMap();
        initMap.put("integer", integer);
        //Process_16719365976 是bpmn 文件中的  process id="Process_16719365976"
        ExecutionEntityImpl executionEntity = runTimeService.startWorkflow("Process_16719365976", initMap);
        return executionEntity.getVariableInstances();
    }
}
