package com.runflow.engine.interceptor;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.agenda.AbstractOperation;
import com.runflow.engine.utils.debug.ExecutionTreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugCommandInvoker  extends CommandInvoker {

    private static final Logger logger = LoggerFactory.getLogger(DebugCommandInvoker.class);

    @Override
    public void executeOperation(Runnable runnable) {
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;

            if (operation.getExecution() != null) {
                logger.info("Execution tree while executing operation {} :", operation.getClass());
                logger.info("{}", System.lineSeparator() +  ExecutionTreeUtil.buildExecutionTree((ExecutionEntityImpl) operation.getExecution()));
            }

        }

        super.executeOperation(runnable);
    }

}
