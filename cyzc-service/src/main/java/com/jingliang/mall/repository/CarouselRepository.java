package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Carousel;
import com.jingliang.mall.repository.base.BaseRepository;

/**
 * 轮播图配置Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:00:28
 */
public interface CarouselRepository extends BaseRepository<Carousel, Long> {

    /**
     * 根据类型查询配置
     *
     * @param type        类型
     * @param isAvailable 类型
     * @return 返回查询到的配置
     */
    Carousel findFirstByTypeAndIsAvailable(Integer type, Boolean isAvailable);
}