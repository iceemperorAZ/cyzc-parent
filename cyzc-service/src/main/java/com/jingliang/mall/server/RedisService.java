package com.jingliang.mall.server;

import javax.websocket.Session;

/**
 * redis服务
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-21 11:21
 */
public interface RedisService {
    /**
     * 保存 键-值到Redis
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value);

    /**
     * 保存 键-值到Redis
     *
     * @param key     键
     * @param value   值
     * @param timeout 失效时间(秒)
     */
    public void setExpire(String key, Object value, long timeout);

    /**
     * 延长key失效时间
     *
     * @param key     键
     * @param timeout 失效时间(秒)
     */
    public void setExpire(String key, long timeout);

    /**
     * 删除
     *
     * @param key 键
     */
    public void remove(String key);


    /**
     * 获取唯一Id
     *
     * @return 返回得到的Id
     */
    public long getGlobalId();

    /**
     * 根据key查询数据
     *
     * @param key    键
     * @param aClass 泛型
     * @return 返回对象
     */
    public <T> T get(String key, Class<T> aClass);

    /**
     * 获得商品编号 编号规则yyyyMMdd+每天的创建数量
     *
     * @return 返回商品编号
     */
    String getProductNo();

    /**
     * 获得订单编号 编号规则yyyyMMdd+每天的订单数量
     *
     * @return 返回商品编号
     */
    String getOrderNo();

    /**
     * 获得订单编号 编号规则yyyyMMdd+每天的创建数量
     *
     * @return 返回商品编号
     */
    String getSkuNo();

    /**
     * 获取库存批号
     *
     * @return 返回库存批号
     */
    String getSkuBatchNumber();

    /**
     * 库存数量原子递增
     *
     * @param productId 商品Id
     * @param num       数量
     * @return 返回自增后的值
     */
    Long skuLineIncrement(String productId, Integer num);

    /**
     * redis中原子递增
     *
     * @param key key
     * @param num 值
     * @return 返回自增后的值
     */
    public Long increment(String key, Integer num);

    /**
     * 库存数量原子递减
     *
     * @param productId 商品Id
     * @param num       数量
     * @return 返回自增减的值
     */
    Long skuLineDecrement(String productId, Integer num);

    /**
     * 优惠券数量原子递增
     *
     * @param couponId 优惠券Id
     * @param num      数量
     * @return 返回自增后的值
     */
    Long couponIncrement(String couponId, Integer num);

    /**
     * 优惠券移除
     *
     * @param couponId 优惠券Id
     */
    public void removeCoupon(String couponId);

    /**
     * 优惠券数量原子递减
     *
     * @param couponId 优惠券Id
     * @param num      数量
     * @return 返回自增减的值
     */
    Long couponDecrement(String couponId, Integer num);

    /**
     * 添加进集合
     *
     * @param key key
     * @param obj 值
     */
    public Long addSet(String key, Object obj);
    /**
     * 根据key查询数据
     *
     * @param key    键
     * @param aClass 泛型
     * @return 返回对象
     */
    public <T> T getSet(String key, Class<T> aClass);

    /**
     * 移除集合中的元素
     * @param key 键
     * @param session  值
     */
    void removeSet(String key, Session session);
}
