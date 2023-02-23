package com.runflow.plugin.redis.constant;

import org.springframework.data.redis.core.*;

import java.util.Arrays;

public interface RedisTaskConstant {


    String ELEMENT_TASK_USER = "redisTask";

    String DEFAULT_TYPE = "STRING";

    enum TypeEnum {
        STRING(ValueOperations.class),
        LIST(ListOperations.class), SET(SetOperations.class),
        ZSET(ZSetOperations.class), HASH(HashOperations.class);

        private Class<?> typeClass;

        TypeEnum(Class<?> typeClass) {
            this.typeClass = typeClass;
        }


        public Class<?> getTypeClass() {
            return typeClass;
        }

        public void setTypeClass(Class<?> typeClass) {
            this.typeClass = typeClass;
        }

        public static boolean existName(String name) {
            return Arrays.stream(RedisTaskConstant.TypeEnum.values()).anyMatch(c -> c.name().equals(name));
        }

    }


}
