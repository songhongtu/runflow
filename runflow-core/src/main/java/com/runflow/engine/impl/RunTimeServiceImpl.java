package com.runflow.engine.impl;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.RunTimeService;
import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.cmd.DeployCmd;
import com.runflow.engine.cmd.StartProcessInstanceCmd;
import com.runflow.engine.repository.DeploymentBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RunTimeServiceImpl extends ServiceImpl implements RunTimeService {

    public DeploymentBuilder createDeployment() {
        return commandExecutor.execute((Command<DeploymentBuilder>) commandContext -> new DeploymentBuilderImpl(RunTimeServiceImpl.this));
    }

    public Deployment deploy(DeploymentBuilderImpl deploymentBuilder) {
        return commandExecutor.execute(new DeployCmd(deploymentBuilder));
    }

    public ExecutionEntityImpl startWorkflow(String key){
        return commandExecutor.execute(new StartProcessInstanceCmd(key,new HashMap<>()));
    }

    public ExecutionEntityImpl startWorkflow(String key, Map<String,Object> variables){
        return commandExecutor.execute(new StartProcessInstanceCmd(key,variables));
    }


}
