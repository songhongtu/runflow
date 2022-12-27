package com.runflow.engine.el;

import com.runflow.engine.ExecutionEntityImpl;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;

import javax.el.*;

public class ExpressionManager {
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    public ExpressionManager(ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    protected ExpressionFactory expressionFactory = new ExpressionFactoryImpl();


    public ELContext getElContext(ExecutionEntityImpl variableScope) {
        ELContext elContext = null;
        ExecutionEntityImpl variableScopeImpl = variableScope;
        elContext = variableScopeImpl.getCachedElContext();
        if (elContext == null) {
            SimpleContext simpleContext = new SimpleContext(this.createElResolver(variableScope));
            variableScope.setCachedElContext(simpleContext);
        }
        return variableScope.getCachedElContext();
    }


    public ExpressionFactory getExpressionFactory() {
        return expressionFactory;
    }

    protected ELResolver createElResolver(ExecutionEntityImpl variableScope) {
        CompositeELResolver elResolver = new CompositeELResolver();
        elResolver.add(new VariableScopeElResolver(variableScope));
        for (ELResolver resolver : processEngineConfiguration.getResolverList()) {
            elResolver.add(resolver);
        }
        elResolver.add(new ArrayELResolver());
        elResolver.add(new ListELResolver());
        elResolver.add(new MapELResolver());
        elResolver.add(new BeanELResolver());
        return elResolver;
    }


}
