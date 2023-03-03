package com.runflow.engine;

import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 调用活动
 */
public class CallActivityTest extends BaseTestCase {

    /**
     * 打印子流程出参
     * @param map1
     * @param map2
     */
    public void print(Map map1,Map map2) {
        Set<Map.Entry<Object,Object>> ent = map1.entrySet();
        for(Map.Entry<Object,Object> entry:ent){
            if(entry.getValue() instanceof ApplicationTest){
                logger.info(entry.getKey()+"  :  "+((ApplicationTest)(entry.getValue())).integer);
            }
        }
    }


    @Test
    public void callActivity() {
        CallActivityTest callActivityTest = new CallActivityTest();
        Map map = new HashMap();
        ApplicationTest applicationTest = new ApplicationTest();
        map.put("key1", applicationTest);
        map.put("callActivity",callActivityTest);
        final ExecutionEntityImpl executionEntity = repositoryService.startWorkflow("Process_1", map);
    }


}
