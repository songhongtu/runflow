package com.runflow.engine.utils;

import com.runflow.engine.RunFlowException;
import com.runflow.engine.context.Context;
import com.runflow.engine.impl.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReflectUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectUtil.class);

    private static final Pattern GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].*");
    private static final Pattern SETTER_PATTERN = Pattern.compile("set[A-Z].*");

    public static ClassLoader getClassLoader() {
        ClassLoader loader = getCustomClassLoader();
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
        }
        return loader;
    }


    public static InputStream getResourceAsStream(String name) {
        InputStream resourceStream = null;
        ClassLoader classLoader = getCustomClassLoader();
        if (classLoader != null) {
            resourceStream = classLoader.getResourceAsStream(name);
        }

        if (resourceStream == null) {
            // Try the current Thread context classloader
            classLoader = Thread.currentThread().getContextClassLoader();
            resourceStream = classLoader.getResourceAsStream(name);
            if (resourceStream == null) {
                // Finally, try the classloader for this class
                classLoader = ReflectUtil.class.getClassLoader();
                resourceStream = classLoader.getResourceAsStream(name);
            }
        }
        return resourceStream;
    }

    public static URL getResource(String name) {
        URL url = null;
        ClassLoader classLoader = getCustomClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(name);
        }
        if (url == null) {
            // Try the current Thread context classloader
            classLoader = Thread.currentThread().getContextClassLoader();
            url = classLoader.getResource(name);
            if (url == null) {
                // Finally, try the classloader for this class
                classLoader = ReflectUtil.class.getClassLoader();
                url = classLoader.getResource(name);
            }
        }

        return url;
    }


    public static Object invoke(Object target, String methodName, Object[] args) {
        try {
            Class<? extends Object> clazz = target.getClass();
            Method method = findMethod(clazz, methodName, args);
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RunFlowException("couldn't invoke " + methodName + " on " + target, e);
        }
    }

    /**
     * Returns the field of the given object or null if it doesn't exist.
     */
    public static Field getField(String fieldName, Object object) {
        return getField(fieldName, object.getClass());
    }

    /**
     * Returns the field of the given class or null if it doesn't exist.
     */
    public static Field getField(String fieldName, Class<?> clazz) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            throw new RunFlowException("not allowed to access field " + field + " on class " + clazz.getCanonicalName());
        } catch (NoSuchFieldException e) {
            // for some reason getDeclaredFields doesn't search superclasses
            // (which getFields() does ... but that gives only public fields)
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getField(fieldName, superClass);
            }
        }
        return field;
    }

    public static void setField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalArgumentException e) {
            throw new RunFlowException("Could not set field " + field.toString(), e);
        } catch (IllegalAccessException e) {
            throw new RunFlowException("Could not set field " + field.toString(), e);
        }
    }

    /**
     * Returns the setter-method for the given field name or null if no setter exists.
     */
    public static Method getSetter(String fieldName, Class<?> clazz, Class<?> fieldType) {
        String setterName = "set" + Character.toTitleCase(fieldName.charAt(0)) + fieldName.substring(1, fieldName.length());
        try {
            // Using getMethods(), getMethod(...) expects exact parameter type
            // matching and ignores inheritance-tree.
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(setterName)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 1 && paramTypes[0].isAssignableFrom(fieldType)) {
                        return method;
                    }
                }
            }
            return null;
        } catch (SecurityException e) {
            throw new RunFlowException("Not allowed to access method " + setterName + " on class " + clazz.getCanonicalName());
        }
    }

    private static Method findMethod(Class<? extends Object> clazz, String methodName, Object[] args) {
        for (Method method : clazz.getDeclaredMethods()) {
            // TODO add parameter matching
            if (method.getName().equals(methodName) && matches(method.getParameterTypes(), args)) {
                return method;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            return findMethod(superClass, methodName, args);
        }
        return null;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> Constructor<T> findMatchingConstructor(Class<T> clazz, Object[] args) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) { // cannot
            // use
            // <?>
            // or
            // <T>
            // due
            // to
            // JDK
            // 5/6
            // incompatibility
            if (matches(constructor.getParameterTypes(), args)) {
                return constructor;
            }
        }
        return null;
    }

    private static boolean matches(Class<?>[] parameterTypes, Object[] args) {
        if ((parameterTypes == null) || (parameterTypes.length == 0)) {
            return ((args == null) || (args.length == 0));
        }
        if ((args == null) || (parameterTypes.length != args.length)) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if ((args[i] != null) && (!parameterTypes[i].isAssignableFrom(args[i].getClass()))) {
                return false;
            }
        }
        return true;
    }

    private static ClassLoader getCustomClassLoader() {
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration != null) {
            final ClassLoader classLoader = processEngineConfiguration.getClassLoader();
            if (classLoader != null) {
                return classLoader;
            }
        }
        return null;
    }


    public static boolean isGetter(Method method) {
        String name = method.getName();
        Class<?> type = method.getReturnType();
        Class<?> params[] = method.getParameterTypes();

        if (!GETTER_PATTERN.matcher(name).matches()) {
            return false;
        }

        // special for isXXX boolean
        if (name.startsWith("is")) {
            return params.length == 0 && type.getSimpleName().equalsIgnoreCase("boolean");
        }

        return params.length == 0 && !type.equals(Void.TYPE);
    }

    public static boolean isSetter(Method method, boolean allowBuilderPattern) {
        String name = method.getName();
        Class<?> type = method.getReturnType();
        Class<?> params[] = method.getParameterTypes();

        if (!SETTER_PATTERN.matcher(name).matches()) {
            return false;
        }

        return params.length == 1 && (type.equals(Void.TYPE) || (allowBuilderPattern && method.getDeclaringClass().isAssignableFrom(type)));
    }

    public static boolean isSetter(Method method) {
        return isSetter(method, false);
    }

    public static String getGetterShorthandName(Method method) {
        if (!isGetter(method)) {
            return method.getName();
        }

        String name = method.getName();
        if (name.startsWith("get")) {
            name = name.substring(3);
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        } else if (name.startsWith("is")) {
            name = name.substring(2);
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    public static String getSetterShorthandName(Method method) {
        if (!isSetter(method)) {
            return method.getName();
        }

        String name = method.getName();
        if (name.startsWith("set")) {
            name = name.substring(3);
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }
}
