package com.runflow.engine.interceptor;

import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandConfig;

public interface CommandInterceptor {

    <T> T execute(CommandConfig config, Command<T> command);

    CommandInterceptor getNext();

    void setNext(CommandInterceptor next);

}
