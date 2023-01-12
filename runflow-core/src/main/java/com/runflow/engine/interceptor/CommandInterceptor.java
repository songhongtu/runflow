package com.runflow.engine.interceptor;

import com.runflow.engine.impl.Command;

public interface CommandInterceptor {

    <T> T execute( Command<T> command);

    CommandInterceptor getNext();

    void setNext(CommandInterceptor next);

}
