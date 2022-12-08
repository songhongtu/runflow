package com.runflow.engine.el;

import com.runflow.engine.ExecutionEntityImpl;

import javax.el.ELContext;
import javax.el.ELResolver;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class VariableScopeElResolver extends ELResolver {


    protected ExecutionEntityImpl variableScope;

    public VariableScopeElResolver(ExecutionEntityImpl variableScope) {
        this.variableScope = variableScope;
    }

    public Object getValue(ELContext context, Object base, Object property) {
        String variable = (String) property; // according to javadoc, can only be a String

        if (base == null) {
            if (variableScope.hasVariable(variable)) {
                context.setPropertyResolved(true); // if not set, the next elResolver in the CompositeElResolver will be called
                return variableScope.getVariableInstances(variable);
            }
        }

        return null;
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (base == null) {
            String variable = (String) property;
            return !variableScope.hasVariable(variable);
        }
        return true;
    }

    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (base == null) {
            String variable = (String) property;
            if (variableScope.hasVariable(variable)) {
                variableScope.setVariableInstances(variable, value);
            }
        }
    }

    public Class<?> getCommonPropertyType(ELContext arg0, Object arg1) {
        return Object.class;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0, Object arg1) {
        return null;
    }

    public Class<?> getType(ELContext arg0, Object arg1, Object arg2) {
        return Object.class;
    }

}
