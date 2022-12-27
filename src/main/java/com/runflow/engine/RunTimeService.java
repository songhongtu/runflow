package com.runflow.engine;

import com.runflow.engine.repository.DeploymentBuilder;

public interface RunTimeService {
    /** Starts creating a new deployment */
    DeploymentBuilder createDeployment();

    /**
     * Deletes the given deployment.
     *
     * @param deploymentId
     *          id of the deployment, cannot be null.
     * @throwns RuntimeException if there are still runtime or history process instances or jobs.
     */
}
