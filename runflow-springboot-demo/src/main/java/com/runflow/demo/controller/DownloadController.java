package com.runflow.demo.controller;

import com.runflow.engine.impl.RunTimeServiceImpl;
import com.runflow.spring.boot.SpringProcessEngineConfiguration;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController("downloadController")
public class DownloadController {
    @Autowired
    private RunTimeServiceImpl runTimeService;

    @GetMapping("/download")
    public void download(){
        SpringProcessEngineConfiguration.refresh();
        runTimeService.startWorkflow("Process_1677224815807").getVariableInstances();
    }

    public void downloadFile(Workbook workbook) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        try (
                ServletOutputStream outputStream = response.getOutputStream();
        ) {

            response.setHeader("Content-disposition", "attachment;filename=download.xlsx");
            workbook.write(outputStream);
        } finally {
            workbook.close();
        }


    }

}
