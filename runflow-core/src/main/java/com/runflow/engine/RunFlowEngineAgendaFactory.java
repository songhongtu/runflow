package com.runflow.engine;

import com.runflow.engine.impl.CommandContext;

public interface RunFlowEngineAgendaFactory {

    RunFlowEngineAgenda createAgenda(CommandContext commandContext);

}

