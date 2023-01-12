package com.runflow.spring.boot;

import org.springframework.context.ApplicationContext;

import javax.el.ELContext;
import javax.el.ELResolver;
import java.beans.FeatureDescriptor;
import java.util.Iterator;

public class ApplicationContextElResolver extends ELResolver {

    protected ApplicationContext applicationContext;

    public ApplicationContextElResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null) {
            // according to javadoc, can only be a String
            String key = (String) property;

            if (applicationContext.containsBean(key)) {
                context.setPropertyResolved(true);
                return applicationContext.getBean(key);
            }
        }

        return null;
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (base == null) {
            String key = (String) property;
            if (applicationContext.containsBean(key)) {
                throw new RuntimeException("Cannot set value of '" + property + "', it resolves to a bean defined in the Spring application-context.");
            }
        }
    }

    public Class<?> getCommonPropertyType(ELContext context, Object arg) {
        return Object.class;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object arg) {
        return null;
    }

    public Class<?> getType(ELContext context, Object arg1, Object arg2) {
        return Object.class;
    }
}
