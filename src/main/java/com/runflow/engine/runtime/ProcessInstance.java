package com.runflow.engine.runtime;

import java.util.Date;
import java.util.Map;

public interface ProcessInstance extends Execution {

    /**
     * The id of the process definition of the process instance.
     */
    String getProcessDefinitionId();

    /**
     * The name of the process definition of the process instance.
     */
    String getProcessDefinitionName();

    /**
     * The key of the process definition of the process instance.
     */
    String getProcessDefinitionKey();

    /**
     * The version of the process definition of the process instance.
     */
    Integer getProcessDefinitionVersion();

    /**
     * The deployment id of the process definition of the process instance.
     */
    String getDeploymentId();

    /**
     * The business key of this process instance.
     */
    String getBusinessKey();

    /**
     * returns true if the process instance is suspended
     */
    boolean isSuspended();

    /**
     * Returns the process variables if requested in the process instance query
     */
    Map<String, Object> getProcessVariables();

    /**
     * The tenant identifier of this process instance
     */
    String getTenantId();

    /**
     * Returns the name of this process instance.
     */
    String getName();

    /**
     * Returns the description of this process instance.
     */
    String getDescription();

    /**
     * Returns the localized name of this process instance.
     */
    String getLocalizedName();

    /**
     * Returns the localized description of this process instance.
     */
    String getLocalizedDescription();

    /**
     * Returns the start time of this process instance.
     */
    Date getStartTime();

    /**
     * Returns the user id of this process instance.
     */
    String getStartUserId();
}
