package com.jingliang.mall.common;

import com.jingliang.mall.server.RedisService;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.data.mapping.MappingException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 主键Id生层策略
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-21 12:16
 */
public class IdGenerator implements IdentifierGenerator {
    private RedisService redisService;

    public IdGenerator() {
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws MappingException {
        if (Objects.isNull(redisService)) {
            redisService = SpringUtils.getApplicationContext().getBean(RedisService.class);
        }
        return redisService.getGlobalId();
    }
}