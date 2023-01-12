package com.runflow.engine.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {

    // No need to instantiate
    private CollectionUtil() {
    }

    /**
     * Helper method that creates a singleton map.
     *
     * Alternative for Collections.singletonMap(), since that method returns a generic typed map <K,T> depending on the input type, but we often need a <String, Object> map.
     */
    public static Map<String, Object> singletonMap(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return map;
    }

    /**
     * Helper method to easily create a map.
     *
     * Takes as input a varargs containing the key1, value1, key2, value2, etc. Note: although an Object, we will cast the key to String internally.
     */
    public static Map<String, Object> map(Object... objects) {

        if (objects.length % 2 != 0) {
            throw new RuntimeException("The input should always be even since we expect a list of key-value pairs!");
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length; i += 2) {
            map.put((String) objects[i], objects[i + 1]);
        }

        return map;
    }

    public static boolean isEmpty(@SuppressWarnings("rawtypes") Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(@SuppressWarnings("rawtypes") Collection collection) {
        return !isEmpty(collection);
    }

}
