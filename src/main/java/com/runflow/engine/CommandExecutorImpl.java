package com.runflow.engine;

import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandConfig;
import com.runflow.engine.interceptor.CommandInterceptor;

public class CommandExecutorImpl  {

    protected CommandConfig defaultConfig;
    protected CommandInterceptor first;

    public CommandExecutorImpl(CommandConfig defaultConfig, CommandInterceptor first) {
        this.defaultConfig = defaultConfig;
        this.first = first;
    }

    public CommandInterceptor getFirst() {
        return first;
    }

    public void setFirst(CommandInterceptor commandInterceptor) {
        this.first = commandInterceptor;
    }

    public CommandConfig getDefaultConfig() {
        return defaultConfig;
    }

    public <T> T execute(Command<T> command) {
        return execute(defaultConfig, command);
    }

    public <T> T execute(CommandConfig config, Command<T> command) {
        return first.execute(config, command);
    }

}
