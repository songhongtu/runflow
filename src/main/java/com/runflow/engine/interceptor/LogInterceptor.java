package com.runflow.engine.interceptor;

import com.runflow.engine.impl.Command;
import com.runflow.engine.impl.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInterceptor extends AbstractCommandInterceptor{

    private static Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    public <T> T execute(CommandConfig config, Command<T> command) {
        if (!log.isDebugEnabled()) {
            // do nothing here if we cannot log
            return next.execute(config, command);
        }
        log.debug("\n");
        log.debug("--- starting {} --------------------------------------------------------", command.getClass().getSimpleName());
        try {

            return next.execute(config, command);

        } finally {
            log.debug("--- {} finished --------------------------------------------------------", command.getClass().getSimpleName());
            log.debug("\n");
        }
    }
}
