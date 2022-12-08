package com.runflow.engine.bpmn.entity.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultDeploymentCache<T> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDeploymentCache.class);

    protected Map<String, T> cache;

    /** Cache with no limit */
    public DefaultDeploymentCache() {
        this.cache = Collections.synchronizedMap(new HashMap<String, T>());
    }

    /**
     * Cache which has a hard limit: no more elements will be cached than the limit.
     */
    public DefaultDeploymentCache(final int limit) {
        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, T>(limit + 1, 0.75f, true) { // +1 is needed, because the entry is inserted first, before it is removed
            // 0.75 is the default (see javadocs)
            // true will keep the 'access-order', which is needed to have a real LRU cache
            private static final long serialVersionUID = 1L;

            protected boolean removeEldestEntry(Map.Entry<String, T> eldest) {
                boolean removeEldest = size() > limit;
                if (removeEldest && logger.isTraceEnabled()) {
                    logger.trace("Cache limit is reached, {} will be evicted", eldest.getKey());
                }
                return removeEldest;
            }

        });
    }

    public T get(String id) {
        return cache.get(id);
    }

    public void add(String id, T obj) {
        cache.put(id, obj);
    }

    public void remove(String id) {
        cache.remove(id);
    }

    public boolean contains(String id) {
        return cache.containsKey(id);
    }

    public void clear() {
        cache.clear();
    }

    // For testing purposes only
    public int size() {
        return cache.size();
    }

}
