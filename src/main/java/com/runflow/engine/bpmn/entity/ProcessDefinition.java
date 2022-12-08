package com.runflow.engine.bpmn.entity;

public interface ProcessDefinition {

    /** unique identifier */
    String getId();

    /**
     * category name which is derived from the targetNamespace attribute in the definitions element
     */
    String getCategory();

    /** label used for display purposes */
    String getName();

    /** unique name for all versions this process definitions */
    String getKey();

    /** description of this process **/
    String getDescription();

    /** version of this process definition */
    int getVersion();

    /**
     * name of {@link RepositoryService#getResourceAsStream(String, String) the resource} of this process definition.
     */
    String getResourceName();

    /** The deployment in which this process definition is contained. */
    String getDeploymentId();

    /** The resource name in the deployment of the diagram image (if any). */
    String getDiagramResourceName();

    /**
     * Does this process definition has a {@link FormService#getStartFormData(String) start form key}.
     */
    boolean hasStartFormKey();

    /**
     * Does this process definition has a graphical notation defined (such that a diagram can be generated)?
     */
    boolean hasGraphicalNotation();

    /** Returns true if the process definition is in suspended state. */
    boolean isSuspended();

    /** The tenant identifier of this process definition */
    String getTenantId();

    /** The engine version for this process definition (5 or 6) */
    String getEngineVersion();

}
