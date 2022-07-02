package com.runflow.engine.impl;

public interface Command<T> {

    T execute(CommandContext commandContext);

}
