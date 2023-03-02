package com.runflow.engine;

import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseTestCase {


    protected static ProcessEngineConfigurationImpl conf = new ProcessEngineConfigurationImpl();

    static RunTimeServiceImpl repositoryService;

    protected static final Logger logger = LoggerFactory.getLogger(BaseTestCase.class);

    private static long testStartTime;
    private static long testEndTime;

    //线程数量
    protected static int THREADCOUNT = 100;
    //循环数量
    protected static int SECOND = 2000;
    //总数
    protected static int TOTALCOUNT = THREADCOUNT * SECOND;


    AtomicInteger integer = new AtomicInteger(0);

    @BeforeClass
    public static void init() {
        testStartTime = System.currentTimeMillis();
        logger.info("=============================测试用例开始=============================");
        conf.init();
        repositoryService = conf.getRunTimeService();
        conf.addPath("/bpmn/leave.bpmn")
                .addPath("/bpmn/parallelLeave.bpmn")
                .addPath("/bpmn/ParallelGatewayTest.bpmn")
                .addPath("/bpmn/callActivity.bpmn")
                .addPath("/bpmn/t.bpmn").addPath("/bpmn/demo1.bpmn");
    }


    @AfterClass
    public static void after() {
        testEndTime = System.currentTimeMillis();
        logger.info("测试用例总耗时：{}", (testEndTime - testStartTime) / 1000);
        logger.info("=============================测试用例结束=============================");
    }


    /**
     * 模拟线程休眠
     *
     * @throws InterruptedException
     */
    public void incrementAndGet() throws InterruptedException {
        Thread.sleep(20);
        integer.incrementAndGet();
    }


}
