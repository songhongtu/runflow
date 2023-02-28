package com.runflow.engine;

import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import com.runflow.engine.impl.RunTimeServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ApplicationTest extends BaseTestCase {


    /**
     * 排他网关  多线程
     *
     * @throws InterruptedException
     */
    @Test
    public void exclusiveGateway1() throws InterruptedException {

        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);

        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);

        Long startTime = System.currentTimeMillis();

        Map map = new HashMap();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("e", e);
        for (int i = 0; i < THREADCOUNT; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < SECOND; j++) {
                    ExecutionEntityImpl leave = repositoryService.startWorkflow("Process_1671936597549", map);


                }

            });
            thread.start();
        }
        while (true) {
            Thread.sleep(1000 * 3);
            logger.info("exclusiveGateway1 a:{}", a);
            logger.info("exclusiveGateway1 b:{}", b);
            logger.info("exclusiveGateway1 c:{}", c);
            logger.info("exclusiveGateway1 d:{}", d);
            logger.info("exclusiveGateway1 e:{}", e);
            if (a.get() == TOTALCOUNT || b.get() == TOTALCOUNT || c.get() == TOTALCOUNT || d.get() == TOTALCOUNT || e.get() == TOTALCOUNT) {
                logger.info("中断");
                break;
            }
        }
        Long endTime = System.currentTimeMillis();
        logger.info("exclusiveGateway1 总耗时:{}", endTime - startTime);
        Assert.assertEquals(a.get(), TOTALCOUNT);
        Assert.assertEquals(b.get(), TOTALCOUNT);
        Assert.assertEquals(c.get(), TOTALCOUNT);
        Assert.assertEquals(d.get(), TOTALCOUNT);
        Assert.assertEquals(e.get(), TOTALCOUNT);

    }

    /**
     * 排他网关  多线程模拟真实操作
     *
     * @throws InterruptedException
     */
    @Test
    public void exclusiveGateway2() {

        ApplicationTest applicationTest = new ApplicationTest();

        Map map = new HashMap();
        map.put("a", applicationTest);
        map.put("b", applicationTest);
        map.put("c", applicationTest);
        map.put("d", applicationTest);
        map.put("e", applicationTest);

        long start = System.currentTimeMillis();
        for (int j = 0; j < THREADCOUNT; j++) {
            repositoryService.startWorkflow("Process_1671936597549", map);
        }
        long end = System.currentTimeMillis();
        logger.info("exclusiveGateway2 结果:{}", integer.get());
        logger.info("exclusiveGateway2 耗时:{}", end - start);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT * 5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT * 5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT * 5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT * 5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT * 5);

    }

    /**
     * 排他网关  单线程
     *
     * @throws InterruptedException
     */
    @Test
    public void exclusiveGateway3() {

        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);

        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        Long startTime = System.currentTimeMillis();
        Map map = new HashMap();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("e", e);
        for (int j = 0; j < TOTALCOUNT; j++) {
            ExecutionEntityImpl leave = repositoryService.startWorkflow("Process_1671936597549", map);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("exclusiveGateway3 a:{}", a);
        logger.info("exclusiveGateway3 b:{}", b);
        logger.info("exclusiveGateway3 c:{}", c);
        logger.info("exclusiveGateway3 d:{}", d);
        logger.info("exclusiveGateway3 e:{}", e);
        logger.info("exclusiveGateway3 总耗时:{}", endTime - startTime);

        Assert.assertEquals(a.get(), TOTALCOUNT);
        Assert.assertEquals(b.get(), TOTALCOUNT);
        Assert.assertEquals(c.get(), TOTALCOUNT);
        Assert.assertEquals(d.get(), TOTALCOUNT);
        Assert.assertEquals(e.get(), TOTALCOUNT);


    }


    /**
     * 并行网关 多线程
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void parallelGatewayTest1() throws InterruptedException {
        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);
        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        Long startTime = System.currentTimeMillis();
        Map map = new HashMap();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("e", e);
        for (int i = 0; i < THREADCOUNT; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < SECOND; j++) {
                    ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01", map);
                }

            });
            thread.start();
        }


        while (true) {
            Thread.sleep(1000 * 3);
            logger.info("parallelGatewayTest1 a:{}", a);
            logger.info("parallelGatewayTest1 b:{}", b);
            logger.info("parallelGatewayTest1 c:{}", c);
            logger.info("parallelGatewayTest1 d:{}", d);
            logger.info("parallelGatewayTest1 e:{}", e);
            if (a.get() == TOTALCOUNT || b.get() == TOTALCOUNT || c.get() == TOTALCOUNT || d.get() == TOTALCOUNT || e.get() == TOTALCOUNT) {
                logger.info("中断");
                break;
            }

        }
        Long endTime = System.currentTimeMillis();
        logger.info("parallelGatewayTest1 总耗时:{}", endTime - startTime);


        Assert.assertEquals(a.get(), TOTALCOUNT);
        Assert.assertEquals(b.get(), TOTALCOUNT);
        Assert.assertEquals(c.get(), TOTALCOUNT);
        Assert.assertEquals(d.get(), TOTALCOUNT);
        Assert.assertEquals(e.get(), TOTALCOUNT);

    }

    /**
     * 并行网关 多线程 模拟真实操作
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void parallelGatewayTest2() {

        ApplicationTest applicationTest = new ApplicationTest();

        Map map = new HashMap();
        map.put("a", applicationTest);
        map.put("b", applicationTest);
        map.put("c", applicationTest);
        map.put("d", applicationTest);
        map.put("e", applicationTest);
        long start = System.currentTimeMillis();
        for (int j = 0; j < THREADCOUNT; j++) {
            ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01", map);
        }
        long end = System.currentTimeMillis();
        logger.info("parallelGatewayTest2 结果:{}", integer.get());
        logger.info("parallelGatewayTest2 耗时:{}", end - start);


        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT*5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT*5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT*5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT*5);
        Assert.assertEquals(applicationTest.integer.get(), THREADCOUNT*5);

    }

    /**
     * 并行网关 单线程
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void parallelGatewayTest3() {

        RunTimeServiceImpl repositoryService = conf.getRunTimeService();
        AtomicInteger a = new AtomicInteger(0);
        AtomicInteger b = new AtomicInteger(0);

        AtomicInteger c = new AtomicInteger(0);
        AtomicInteger d = new AtomicInteger(0);
        AtomicInteger e = new AtomicInteger(0);
        Map map = new HashMap();
        map.put("a", a);
        map.put("b", b);
        map.put("c", c);
        map.put("d", d);
        map.put("e", e);
        Long startTime = System.currentTimeMillis();
        for (int j = 0; j < TOTALCOUNT; j++) {
            ExecutionEntityImpl leave = repositoryService.startWorkflow("ParallelGatewayTest01", map);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("parallelGatewayTest3 a:{}", a);
        logger.info("parallelGatewayTest3 b:{}", b);
        logger.info("parallelGatewayTest3 c:{}", c);
        logger.info("parallelGatewayTest3 d:{}", d);
        logger.info("parallelGatewayTest3 e:{}", e);
        logger.info("parallelGatewayTest3 总耗时:{}", endTime - startTime);


        Assert.assertEquals(a.get(), TOTALCOUNT);
        Assert.assertEquals(b.get(), TOTALCOUNT);
        Assert.assertEquals(c.get(), TOTALCOUNT);
        Assert.assertEquals(d.get(), TOTALCOUNT);
        Assert.assertEquals(e.get(), TOTALCOUNT);


    }



}
