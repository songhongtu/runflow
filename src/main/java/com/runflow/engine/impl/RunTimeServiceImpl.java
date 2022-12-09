package com.runflow.engine.impl;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.RepositoryService;
import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.cmd.DeployCmd;
import com.runflow.engine.cmd.StartProcessInstanceCmd;
import com.runflow.engine.repository.DeploymentBuilder;
import com.runflow.engine.runtime.ProcessInstance;

import java.util.HashMap;
import java.util.Map;

public class RunTimeServiceImpl extends ServiceImpl implements RepositoryService {

    public DeploymentBuilder createDeployment() {
        return commandExecutor.execute(new Command<DeploymentBuilder>() {
            @Override
            public DeploymentBuilder execute(CommandContext commandContext) {
                return new DeploymentBuilderImpl(RunTimeServiceImpl.this);
            }
        });
    }

    public Deployment deploy(DeploymentBuilderImpl deploymentBuilder) {
        return commandExecutor.execute(new DeployCmd<Deployment>(deploymentBuilder));
    }

    public ExecutionEntityImpl startWorkflow(String key){
        return commandExecutor.execute(new StartProcessInstanceCmd<ExecutionEntityImpl>(key,new HashMap<>()));
    }

    public ExecutionEntityImpl startWorkflow(String key, Map<String,Object> variables){
        return commandExecutor.execute(new StartProcessInstanceCmd<ExecutionEntityImpl>(key,variables));
    }





    public void generaImages(String key){
        commandExecutor.execute(new GeneraImageCommand(key));
    }

}
