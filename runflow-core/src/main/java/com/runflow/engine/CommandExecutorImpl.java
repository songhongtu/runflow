package com.runflow.engine;

import com.runflow.engine.impl.Command;
import com.runflow.engine.interceptor.CommandInterceptor;

public class CommandExecutorImpl {

    protected CommandInterceptor first;

    public CommandExecutorImpl(CommandInterceptor first) {
        this.first = first;
    }

    public <T> T execute(Command<T> command) {
        return first.execute(command);
    }
}
