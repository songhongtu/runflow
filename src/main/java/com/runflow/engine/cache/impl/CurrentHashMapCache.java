package com.runflow.engine.cache.impl;

import com.runflow.engine.ExecutionEntity;
import com.runflow.engine.cache.Session;
import com.runflow.engine.utils.CollectionUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CurrentHashMapCache<V extends ExecutionEntity> extends ConcurrentHashMap<String, Set<V>> {


    @Override
    public Set<V> put(String key, Set<V> value) {
        return super.put(key, value);
    }

    public Set<V> putSingle(V v) {
        return this.putSingle(v.getSerialNumber(), v);
    }


    public Set<V> putSingle(String key, V value) {
        Set<V> vs = this.get(key);
        if (CollectionUtil.isEmpty(vs)) {
            synchronized (CurrentHashMapCache.class) {
                if (CollectionUtil.isEmpty(vs)) {
                  Set<V> objects = ConcurrentHashMap.newKeySet();
                    //               Set<V> objects =new HashSet<>();
                    objects.add(value);
                    return super.put(key, objects);
                }
            }

        }
        V byId = this.getById(key, value.getId());
        if (byId != null) {
            return null;
        } else {
            vs.add(value);
        }
        return super.put(key, vs);
    }

    public synchronized V findInCache(String key, String id) {
        return this.getById(key, id);
    }


    public Set<V> findInCache(String key) {
        return this.get(key);
    }

    protected V getById(String uuid, String id) {
        return this.getById(this.get(uuid), id);

    }


    protected V getById(Set<V> vs, String id) {
        for (V v : vs) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }


}
