package com.runflow.spring.boot;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;
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
        if (base == null) {
            String beanName = property.toString();
            if (applicationContext.containsBean(beanName)) {
                return true;
            }
        }

        return false;
    }

    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (base == null) {
            String beanName = property.toString();
            if (applicationContext.containsBean(beanName)) {
                if (value != applicationContext.getBean(beanName)) {
                    throw new PropertyNotWritableException("Variable '" + beanName + "' refers to a Spring bean which by definition is not writable");
                }
                context.setPropertyResolved(true);
            }
        }
    }

    public Class<?> getCommonPropertyType(ELContext context, Object arg) {
        return Object.class;
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object arg) {
        return null;
    }

    public Class<?> getType(ELContext elContext, Object base, Object property) {
        if (base == null) {
            String beanName = property.toString();
            if (applicationContext.containsBean(beanName)) {
                elContext.setPropertyResolved(true);
                return applicationContext.getType(beanName);
            }
        }
        return null;
    }
}
