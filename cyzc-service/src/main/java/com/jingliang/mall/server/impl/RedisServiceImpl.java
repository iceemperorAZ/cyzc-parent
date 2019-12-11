package com.jingliang.mall.server.impl;

import com.jingliang.mall.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * RedisServices实现
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-21 11:22
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Value("${default.redis.key}")
    String defaultPrefix;
    @Value("${pk.id.redis.key.prefix}")
    String pkIdPrefix;
    @Value("${product.sku.redis.key.prefix}")
    String productSkuPrefix;
    @Value("${coupon.redis.key.prefix}")
    String couponPrefix;
    @Value("${order.no.redis.key.prefix}")
    String orderNoPrefix;
    @Value("${order.no.redis.value.prefix:}")
    String orderNoValuePrefix;
    @Value("${prepay.id.redis.key.prefix}")
    String prepayIdPrefix;
    @Value("${pay.overtime}")
    Integer payOvertime;
    @Value("${product.no.redis.key.prefix}")
    String productNoPrefix;
    @Value("${sku.no.redis.key.prefix}")
    String skuNoPrefix;
    @Value("${sku.batch_number.redis.key.prefix}")
    String skuBatchNumberPrefix;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(defaultPrefix + key, value);
    }

    @Override
    public void setExpire(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(defaultPrefix + key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void setExpire(String key, long timeout) {
        redisTemplate.expire(defaultPrefix + key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(defaultPrefix + key);
    }


    @Override
    public long getGlobalId() {
        //每天重新开始增长
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = dateFormat.format(new Date());
        Long increment = redisTemplate.opsForValue().increment(pkIdPrefix + dateString, 1);
        //两天后过期
        redisTemplate.expire(pkIdPrefix + dateString, 2, TimeUnit.DAYS);
        long id = Long.parseLong(dateString + increment);
        log.debug("redis中Id自增序号为：[{}]", id);
        return id;
    }

    @Override
    public <T> T get(String key, Class<T> aClass) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Object value = redisTemplate.opsForValue().get(defaultPrefix + key);
        return Objects.isNull(value) ? null : aClass.cast(value);
    }

    @Override
    public String getProductNo() {
        return getNo(productNoPrefix);
    }

    @Override
    public String getOrderNo() {
        return orderNoValuePrefix + getNo(orderNoPrefix);
    }

    @Override
    public String getSkuNo() {
        return getNo(skuNoPrefix);
    }

    @Override
    public String getSkuBatchNumber() {
        return getNo(skuBatchNumberPrefix);
    }

    @Override
    public Long skuLineIncrement(String productId, Integer num) {
        Long increment = redisTemplate.opsForValue().increment(productSkuPrefix + productId, num);
        log.info("Id为:[{}]的商品，在redis中的库存数量为:[{}]", productId, increment);
        return increment;
    }

    @Override
    public Long incrementProduct(String key, Integer num) {
        Long increment = redisTemplate.opsForValue().increment(productSkuPrefix + key, num);
        log.info("key为:[{}]，在redis中自增值为:[{}]", key, increment);
        return increment;
    }
    @Override
    public Long increment(String key, Integer num) {
        Long increment = redisTemplate.opsForValue().increment(defaultPrefix + key, num);
        log.info("key为:[{}]，在redis中自增值为:[{}]", key, increment);
        return increment;
    }

    @Override
    public Long skuLineDecrement(String productId, Integer num) {
        Long decrement = redisTemplate.opsForValue().decrement(productSkuPrefix + productId, num);
        log.info("Id为:[{}]的商品，在redis中的库存数量为:[{}]", productId, decrement);
        return decrement;
    }

    @Override
    public Long couponIncrement(String couponId, Integer num) {
        Long increment = redisTemplate.opsForValue().increment(couponPrefix + couponId, num);
        log.info("Id为:[{}]的优惠券，在redis中的剩余数量为:[{}]", couponId, increment);
        return increment;
    }

    @Override
    public void removeCoupon(String couponId) {
        redisTemplate.delete(couponPrefix + couponId);
        log.info("Id为:[{}]的优惠券，从redis中删除", couponId);
    }

    @Override
    public Long couponDecrement(String couponId, Integer num) {
        Long decrement = redisTemplate.opsForValue().decrement(couponPrefix + couponId, num);
        log.info("Id为:[{}]的优惠券，在redis中的剩余数量为:[{}]", couponId, decrement);
        return decrement;
    }

    @Override
    public Long addSet(String key, Object obj) {
        return redisTemplate.opsForSet().add(defaultPrefix + key, obj);
    }

    @Override
    public <T> T getSet(String key, Class<T> aClass) {
        return aClass.cast(redisTemplate.opsForSet().members(defaultPrefix + key));
    }

    @Override
    public void removeSet(String key, Session value) {
        redisTemplate.opsForSet().remove(defaultPrefix + key,value);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(defaultPrefix + key);
    }

    private String getNo(String productNoPrefix) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String dateString = dateFormat.format(new Date());
        Long increment = redisTemplate.opsForValue().increment(productNoPrefix + dateString, 1);
        //两天后过期
        redisTemplate.expire(productNoPrefix + dateString, 2, TimeUnit.DAYS);
        DecimalFormat df = new DecimalFormat("00000");
        return dateString + df.format(increment);
    }
}
