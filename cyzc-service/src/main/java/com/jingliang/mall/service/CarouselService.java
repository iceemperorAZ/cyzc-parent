package com.jingliang.mall.service;

import com.jingliang.mall.entity.Carousel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 轮播图配置Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:00:28
 */
public interface CarouselService {

    /**
     * 保存轮播图配置
     *
     * @param carousel 轮播图配置
     * @return 返回保存后的轮播图配置
     */
    Carousel save(Carousel carousel);

    /**
     * 分页查询全部轮播图配置
     *
     * @param carouselSpecification 查询条件
     * @param pageRequest           分页条件
     * @return 返回查询到的轮播图配置列表
     */
    Page<Carousel> findAll(Specification<Carousel> carouselSpecification, PageRequest pageRequest);

    /**
     * 根据主键查询轮播图信息
     *
     * @param id 主键Id
     * @return 返回查询到的轮播图信息
     */
    Carousel findById(Long id);

    /**
     * 查询全部轮播图配置
     *
     * @param carouselSpecification 查询条件
     * @return 返回查询到的轮播图配置列表
     */
    List<Carousel> findAll(Specification<Carousel> carouselSpecification);

    /**
     * 根据类型查询配置信息
     *
     * @param type
     * @return
     */
    Carousel findByType(Integer type);
}