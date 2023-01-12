package com.runflow.engine.cmd;

import com.runflow.engine.bpmn.deployer.ParsedDeployment;
import com.runflow.engine.bpmn.entity.Deployment;
import com.runflow.engine.bpmn.entity.DeploymentEntity;
import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandContext;
import com.runflow.engine.impl.DeploymentBuilderImpl;

import java.util.*;

public class  DeployCmd implements Command<Deployment> {

    protected DeploymentBuilderImpl deploymentBuilder;

    public DeployCmd(DeploymentBuilderImpl deploymentBuilder) {
        this.deploymentBuilder = deploymentBuilder;
    }

    public Deployment execute(CommandContext commandContext) {


        return executeDeploy(commandContext);
    }

    protected Deployment executeDeploy(CommandContext commandContext) {
        DeploymentEntity deployment = deploymentBuilder.getDeployment();
        deployment.setDeploymentTime(new Date());
        deployment.setNew(true);
        // Actually deploy
        ParsedDeployment deploy = commandContext.getProcessEngineConfiguration().getBpmnDeployer().deploy(deployment, commandContext.getProcessEngineConfiguration().getBpmnParser());
        deployment.setParsedDeployment(deploy);
        return deployment;
    }





}
