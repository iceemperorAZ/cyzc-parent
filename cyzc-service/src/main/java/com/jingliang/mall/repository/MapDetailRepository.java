package com.jingliang.mall.repository;

import com.jingliang.mall.repository.base.BaseRepository;
import com.jingliang.mall.entity.MapDetail;

import java.util.List;

/**
 * 保存地图详情信息Repository
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
public interface MapDetailRepository extends BaseRepository<MapDetail, Long> {

    /*
     * 根据地图编号查询地图详细信息
     * */
    List<MapDetail> findAllByMapNoAndIsAvailable(String mapNo, Boolean isAvailable);

    /*
     * 根据ID和是否可用查询地图详细信息
     * */
    MapDetail findByIdAndIsAvailable(Long id, Boolean isAvailable);

    /*
     * 根据经纬度删除地图详细信息
     * */
    Boolean deleteByLatitudeAndLongitude(Double longitude, Double latitude);
    /*
    * 根据地图编号删除地图
    * */
    Integer deleteByMapNo(String mapNo);
}