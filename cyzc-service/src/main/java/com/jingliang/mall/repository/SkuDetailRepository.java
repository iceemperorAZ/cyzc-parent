package com.jingliang.mall.repository;

import com.jingliang.mall.entity.SkuDetail;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 库存详情Repository
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-24 19:00:48
 */
public interface SkuDetailRepository extends BaseRepository<SkuDetail, Long> {
    /**
     * 根据库存详情Id更新实际库存
     *
     * @param skuDetail 库存详情对象
     */
    @Modifying
    @Query("update SkuDetail as s set s.skuResidueNum = (s.skuResidueNum + :#{#skuDetail.skuResidueNum}), s.updateTime = :#{#skuDetail.updateTime}" +
            ", s.updateUserId = :#{#skuDetail.updateUserId}, s.updateUserName = :#{#skuDetail.updateUserName} where s.id = :#{#skuDetail.id}")
    void updateSkuDetailById(SkuDetail skuDetail);
}