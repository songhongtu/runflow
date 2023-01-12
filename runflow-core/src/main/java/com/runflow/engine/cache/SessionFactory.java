package com.runflow.engine.cache;

import com.runflow.engine.impl.CommandContext;

public interface SessionFactory {

    Class<?> getSessionType();

    Session openSession(CommandContext commandContext);

}