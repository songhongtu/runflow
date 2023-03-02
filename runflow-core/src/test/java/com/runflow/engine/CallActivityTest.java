package com.runflow.engine;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用活动
 */
public class CallActivityTest extends BaseTestCase {


    @Test
    public void callActivity() {
        Map map = new HashMap();
        ApplicationTest applicationTest = new ApplicationTest();
        map.put("key1", applicationTest);
        final ExecutionEntityImpl executionEntity = repositoryService.startWorkflow("Process_1", map);

        logger.info("{}", executionEntity.variableInstances);


    }


}
