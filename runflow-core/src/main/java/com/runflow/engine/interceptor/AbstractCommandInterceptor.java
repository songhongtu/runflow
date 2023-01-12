package com.runflow.engine.interceptor;
public abstract class AbstractCommandInterceptor implements CommandInterceptor {

    /**
     * will be initialized by the {@link org.activiti.engine.ProcessEngineConfiguration ProcessEngineConfiguration}
     */
    protected CommandInterceptor next;

    @Override
    public CommandInterceptor getNext() {
        return next;
    }

    @Override
    public void setNext(CommandInterceptor next) {
        this.next = next;
    }
}
