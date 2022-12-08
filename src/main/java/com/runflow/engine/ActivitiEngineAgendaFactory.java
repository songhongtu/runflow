package com.runflow.engine;

import com.runflow.engine.impl.CommandContext;

public interface ActivitiEngineAgendaFactory {

    ActivitiEngineAgenda createAgenda(CommandContext commandContext);

}

