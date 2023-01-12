package com.runflow.engine.util.io;

import org.activiti.bpmn.converter.util.InputStreamProvider;

import java.io.InputStream;

public interface StreamSource extends InputStreamProvider {

    /**
     * Creates a <b>NEW</b> {@link InputStream} to the provided resource.
     */
    InputStream getInputStream();

}

