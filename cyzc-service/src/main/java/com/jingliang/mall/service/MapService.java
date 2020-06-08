package com.jingliang.mall.service;

import com.jingliang.mall.entity.AddressUserHistory;
import com.jingliang.mall.entity.Buyer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author lmd
 * @date 2020/4/27
 * @company 晶粮
 */
public interface MapService {

    /**
     * 通过地址获取经纬度
     *
     * @return
     */
    List<Map<String, Object>> getlngAndLat(MultipartFile multipartFile);

    /**
     * 获取所有商户的地址
     *
     * @return
     */
    List<Map<String, Object>> findAlldefaultBuyerAddress(Specification<Buyer> buyerSpecification);

    /**
     * 保存用户经纬度记录
     *
     * @return
     */
    AddressUserHistory saveMap(AddressUserHistory addressUserHistory);

    /**
     * 通过用户id获取经纬度记录
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> readMap(Long userId);

    /**
     * 获取所有商户地址，解析成经纬度并存入数据库
     *
     * @return
     */
    Boolean getAllMapToSave();

    /**
     * 根据组编号查询商户默认地址
     *
     * @param groupNo
     * @return
     */
    List<Map<String, Object>> findAddressByGroupNo(String groupNo);

    /**
     * 查询销售员的最后一条记录
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> userAddressHistoryToEndTime();

    List<Map<String, Object>> searchSaleByGroup(String groupNo);
}
