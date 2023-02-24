package com.runflow.plugin.excel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.spring.boot.SpringContextUtil;
import com.runflow.spring.boot.SpringProcessEngineConfiguration;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("excelController")
public class ExcelController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ApplicationContext applicationContext;


    @GetMapping("/demo1")
    public void demo1() throws JsonProcessingException {
        SpringProcessEngineConfiguration.refresh();
        RunTimeServiceImpl bean = SpringContextUtil.getApplicationContext().getBean(RunTimeServiceImpl.class);
        ExecutionEntityImpl executionEntity = bean.startWorkflow("Process_1677220767223");
    }

    public List<Map> createDataList() {
        Map map1 = new HashMap();
        map1.put("k1", 4);
        map1.put("k2", 5);
        map1.put("k3", 6);

        Map map2 = new HashMap();
        map2.put("k1", 7);
        map2.put("k2", 8);
        map2.put("k3", 9);


        return Arrays.asList(map1, map2);
    }


    public void downloadFile(Workbook workbook) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        try (

                ServletOutputStream outputStream = response.getOutputStream();
        ) {

            response.setHeader("Content-disposition", "attachment;filename=123.xlsx");
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }


    }


}
