package com.runflow.demo.service;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service("demo2")
public class Demo2Service {
    public Integer add(AtomicInteger integer) throws InterruptedException {
        //模拟真实环境休眠
        Thread.sleep(RandomUtils.nextInt(0, 1000));
        return integer.incrementAndGet();
    }
}
