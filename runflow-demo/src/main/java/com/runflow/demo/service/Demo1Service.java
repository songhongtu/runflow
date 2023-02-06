package com.runflow.demo.service;

import org.springframework.stereotype.Service;

@Service("demo1")
public class Demo1Service {
    public Integer add(Integer a) {
        return a + 1;
    }
}
