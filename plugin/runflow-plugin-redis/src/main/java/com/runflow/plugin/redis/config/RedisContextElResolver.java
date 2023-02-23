package com.runflow.plugin.redis.config;

import com.runflow.plugin.redis.constant.RedisTaskConstant;
import com.runflow.spring.boot.SpringContextUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.el.ELContext;
import javax.el.ELResolver;
import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.Iterator;

public class RedisContextElResolver extends ELResolver {
    protected RedisTemplate redisTemplate;

    public RedisContextElResolver(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public    Object getObjByEnumString(String name) {
        Object o;
        RedisTaskConstant.TypeEnum typeEnum = RedisTaskConstant.TypeEnum.valueOf(name);
        switch (typeEnum) {
            case STRING:
                o = redisTemplate.opsForValue();
                break;
            case SET:
                o = redisTemplate.opsForSet();
                break;
            case HASH:
                o = redisTemplate.opsForHash();
                break;
            case LIST:
                o = redisTemplate.opsForList();
                break;
            case ZSET:
                o = redisTemplate.opsForZSet();
                break;
            default:
                o = redisTemplate;
        }
        return o;

    }


    public Object getValue(ELContext context, Object base, Object property) {
        if (base == null) {
            String key = property.toString();
            if (RedisTaskConstant.TypeEnum.existName(key)) {
                context.setPropertyResolved(true);
                return this.getObjByEnumString(key);
            }
        }

        return null;
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        if (base == null) {
            if (RedisTaskConstant.TypeEnum.existName(property.toString())) {
                return true;
            }
        }

        return false;
    }

    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (base == null) {
            String key = (String) property;
            if (Arrays.stream(RedisTaskConstant.TypeEnum.values()).anyMatch(c -> c.name().equals(key))) {
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

    public Class<?> getType(ELContext elContext, Object base, Object property) {
        if (base == null) {
            if (RedisTaskConstant.TypeEnum.existName(property.toString())) {
                elContext.setPropertyResolved(true);
                return this.getObjByEnumString(property.toString()).getClass();
            }
        }
        return null;
    }
}
