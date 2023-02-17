package com.runflow.engine.cache.impl;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.utils.CollectionUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentHashMapCache<V extends ExecutionEntity> extends ConcurrentHashMap<String, Set<V>> {




    public Set<V> putSingle(V v) {
        return this.putSingle(v.getSerialNumber(), v);
    }


    public Set<V> putSingle(String key, V value) {
        Set<V> vs = this.get(key);
        if (CollectionUtil.isEmpty(vs)) {
            synchronized (key) {
                vs = this.get(key);
                if (CollectionUtil.isEmpty(vs)) {
                  Set<V> objects = ConcurrentHashMap.newKeySet();
                    objects.add(value);
                    return super.put(key, objects);
                }
            }

        }
         vs.add(value);
        return super.put(key, vs);
    }


}
