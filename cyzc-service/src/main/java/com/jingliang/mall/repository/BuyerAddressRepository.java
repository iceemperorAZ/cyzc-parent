package com.jingliang.mall.repository;

import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 09:33:48
 */
public interface BuyerAddressRepository extends BaseRepository<BuyerAddress, Long> {

    /**
     * 根据会员Id查询默认收货地址
     *
     * @param buyerId     会员Id
     * @param isDefault   是否默认
     * @param isAvailable 是否可用
     * @return 返回查询到的地址信息
     */
    BuyerAddress findFirstByBuyerIdAndIsDefaultAndIsAvailable(Long buyerId, Boolean isDefault, Boolean isAvailable);

    /**
     * 根据会员Id和主键Id查询收货地址是否存在
     *
     * @param id          主键Id
     * @param buyerId     会员Id
     * @param isAvailable 是否可用
     * @return 返回查询到的地址信息
     */
    Integer countByIdAndBuyerIdAndIsAvailable(Long id, Long buyerId, Boolean isAvailable);

    /**
     * 根据id查询地址并更新经纬度
     *
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "UPDATE tb_buyer_address SET longitude=:longitude,latitude=:latitude WHERE id =:id", nativeQuery = true)
    Integer updateLngAndLatById(Long id, Double longitude, Double latitude);

    /**
     * 根据组编号查询商户默认地址
     *
     * @param groupNo
     * @return
     */
    @Query(value = "SELECT g.group_no AS groupNo,  " +
            " b.user_name AS buyerName,  " +
            " b.shop_name AS shopName," +
            " b.phone AS phone,  " +
            " CONCAT_WS('/',r1.`name`,r2.`name`,r3.`name`,baddr.detailed_address) AS address,  " +
            " baddr.longitude AS longitude,  " +
            " baddr.latitude As latitude  " +
            " FROM tb_group g LEFT JOIN tb_user u ON u.group_no = g.group_no  " +
            " JOIN tb_buyer b ON b.sale_user_id = u.id  " +
            " JOIN tb_buyer_address baddr ON baddr.buyer_id = b.id  " +
            " INNER JOIN tb_region r1 ON baddr.province_code = r1.`code`  " +
            " INNER JOIN tb_region r2 ON baddr.city_code = r2.`code`  " +
            " INNER JOIN tb_region r3 ON baddr.area_code = r3.`code`  " +
            " WHERE g.group_no like :groupNo AND baddr.is_available = 1 AND baddr.is_default = 1  " +
            " ORDER BY groupNo", nativeQuery = true)
    List<Map<String, Object>> findAddressByGroupNo(String groupNo);

    /**
     * '根据商户Id查询地址列表
     *
     * @param buyerId
     * @param isAvailable
     * @return
     */
    List<BuyerAddress> findAllByBuyerIdAndIsAvailableOrderByIsDefaultDesc(Long buyerId, Boolean isAvailable);
}