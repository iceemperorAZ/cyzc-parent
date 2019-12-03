package com.jingliang.mall.repository;


import com.jingliang.mall.entity.Sku;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 非懒加载 商品库存表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 16:01:46
 */
public interface SkuRepository extends BaseRepository<Sku, Long> {
    /**
     * 非懒加载 根据商品Id查询库存信息
     *
     * @param productId   商品信息
     * @param isAvailable 是否可用
     * @return 返回查询到的商品信息
     */
    Sku findFirstByProductIdAndIsAvailable(Long productId, Boolean isAvailable);

    /**
     * 根据库存Id追加库存数量  实际库存，线上库存，总库存都要追加
     *
     * @param sku 库存对象
     */
    @Modifying
    @Query("update Sku as s set s.skuRealityNum = (s.skuRealityNum + :#{#sku.skuRealityNum}), s.skuLineNum = (s.skuLineNum + :#{#sku.skuLineNum})" +
            ", s.skuHistoryTotalNum = (s.skuHistoryTotalNum + :#{#sku.skuHistoryTotalNum}), s.updateTime = :#{#sku.updateTime}" +
            ", s.updateUserId = :#{#sku.updateUserId}, s.updateUserName = :#{#sku.updateUserName} where s.id = :#{#sku.id}")
    void appendSkuById(Sku sku);

    /**
     * 根据库存Id更新库存数量  实际库存，线上库存都要更新
     *
     * @param sku 库存对象
     */
    @Modifying
    @Query("update Sku as s set s.skuRealityNum = (s.skuRealityNum + :#{#sku.skuRealityNum}), s.skuLineNum = (s.skuLineNum + :#{#sku.skuLineNum})" +
            ", s.updateTime = :#{#sku.updateTime}, s.updateUserId = :#{#sku.updateUserId}, s.updateUserName = :#{#sku.updateUserName} where s.id = :#{#sku.id}")
    void updateSkuById(Sku sku);

    /**
     * 根据商品Id更新线上库存
     *
     * @param sku 库存对象
     */
    @Modifying
    @Query("update Sku as s set s.skuLineNum = (s.skuLineNum + :#{#sku.skuLineNum}), s.updateTime = :#{#sku.updateTime}" +
            ", s.updateUserId = :#{#sku.updateUserId}, s.updateUserName = :#{#sku.updateUserName} where s.productId = :#{#sku.productId}")
    public void updateLineSkuByProductId(Sku sku);
    /**
     * 根据商品Id更新实际库存
     *
     * @param sku 库存对象
     */
    @Modifying
    @Query("update Sku as s set s.skuRealityNum = (s.skuRealityNum + :#{#sku.skuRealityNum}), s.updateTime = :#{#sku.updateTime}" +
            ", s.updateUserId = :#{#sku.updateUserId}, s.updateUserName = :#{#sku.updateUserName} where s.productId = :#{#sku.productId}")
    public void updateRealitySkuByProductId(Sku sku);
}