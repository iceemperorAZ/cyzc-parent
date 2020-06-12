package com.jingliang.mall.repository;

import com.jingliang.mall.entity.UnavailableName;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 不规范商铺名称合集Repository
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-06-11 15:45:35
 */
public interface UnavailableNameRepository extends BaseRepository<UnavailableName, Long> {

    @Query(value = " SELECT ANY_VALUE(IFNULL(shop_name,'-')) FROM tb_buyer GROUP BY ANY_VALUE(shop_name) HAVING COUNT(id) > 2 ORDER BY COUNT(id) DESC ", nativeQuery = true)
    List<String> findBuyerName();

}