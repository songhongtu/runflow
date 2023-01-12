package com.runflow.engine.cache;

public interface Session {

    void flush();

    void close();
}
