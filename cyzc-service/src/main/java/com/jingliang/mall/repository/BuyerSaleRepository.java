package com.jingliang.mall.repository;

import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商户销售绑定表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
public interface BuyerSaleRepository extends BaseRepository<BuyerSale, Long> {

    /**
     * 根据销售Id查询（只有一条）
     *
     * @param saleUserId
     * @param buyerId
     * @param isAvailable
     * @return
     */
    BuyerSale findAllBySaleIdAndBuyerIdAndUntyingTimeIsNullAndIsAvailable(Long saleUserId, Long buyerId, boolean isAvailable);

    /**
     * 根据销售Id查询
     *
     * @param saleUserId
     * @param isAvailable
     * @return
     */
    List<BuyerSale> findAllBySaleIdAndIsAvailable(Long saleUserId, boolean isAvailable);

    /**
     * 根据销售Id查询
     *
     * @param saleUserId
     * @param buyerId
     * @param isAvailable
     * @return
     */
    List<BuyerSale> findAllBySaleIdAndBuyerIdAndIsAvailable(Long saleUserId, Long buyerId, boolean isAvailable);

    /*
    *根据时间查询商户信息和绑定的销售以及所在大区
    * */
    @Query(value = " SELECT " +
            "  ex.time as '时间', " +
            "  ex.id as'商户id', " +
            "  ex.sname as '商户名称', " +
            "  r.name as '商户所在区', " +
            "  ex.buyerAddress as '商户地址', " +
            "  ex.buyerName as '收货人', " +
            "  ex.buyerPhone as '手机号', " +
            "  ex.uid '销售员id', " +
            "  ex.uname '销售员', " +
            "  u.user_name AS '区域经理', " +
            "  ex.groupname as '销售员所在区' " +
            " FROM " +
            "  ( " +
            "  SELECT " +
            "   DATE_FORMAT( b.create_time, '%Y-%m-%d' ) AS time, " +
            "   ANY_VALUE(b.id) AS id, " +
            "   ANY_VALUE(b.shop_name) AS sname, " +
            "   ANY_VALUE(b.sale_user_id) AS uid, " +
            "   ANY_VALUE(u.user_name) AS uname, " +
            "   ANY_VALUE(u.manager_id) AS mid, " +
            "   ANY_VALUE(g.group_name) AS groupname, " +
            "   ANY_VALUE(ba.area_code) AS areacode, " +
            "   ANY_VALUE(ba.detailed_address) AS buyerAddress, " +
            "   ANY_VALUE(ba.consignee_name) AS buyerName, " +
            "   ANY_VALUE(ba.phone) AS buyerPhone " +
            "  FROM " +
            "   tb_buyer b " +
            "   JOIN tb_user u ON u.id = b.sale_user_id  " +
            "   JOIN tb_group g on g.group_no = u.group_no " +
            "   JOIN tb_buyer_address ba ON b.id = ba.buyer_id " +
            "   WHERE b.is_available = 1 " +
            "   AND ba.is_default = 1 " +
            "  ) ex " +
            "  JOIN tb_user u ON u.id = ex.mid " +
            "  JOIN tb_region r ON r.code = ex.areacode " +
            "  WHERE ex.time BETWEEN DATE_FORMAT( :startTime , '%Y-%m-%d') AND DATE_FORMAT( :endTime , '%Y-%m-%d') " +
            "  ORDER BY ex.time ASC  ",nativeQuery = true)
    List<Map<String,Object>> findBuyerSaleByTime(Date startTime ,Date endTime);

    /*
     *根据时间查询商户信息和绑定的销售以及所在大区----html
     * */
    @Query(value = " SELECT " +
            "  ex.time as time, " +
            "  ex.id as id, " +
            "  ex.sname as shopName, " +
            "  r.name as shopArea, " +
            "  ex.buyerAddress AS buyerAddress, " +
            "  ex.buyerName as buyerName, " +
            "  ex.buyerPhone as buyerPhone, " +
            "  ex.uid saleId, " +
            "  ex.uname saleName, " +
            "  u.user_name AS manager, " +
            "  ex.groupname as saleArea " +
            " FROM " +
            "  ( " +
            "  SELECT " +
            "   DATE_FORMAT( b.create_time, '%Y-%m-%d' ) AS time, " +
            "   ANY_VALUE(b.id) AS id, " +
            "   ANY_VALUE(b.shop_name) AS sname, " +
            "   ANY_VALUE(b.sale_user_id) AS uid, " +
            "   ANY_VALUE(u.user_name) AS uname, " +
            "   ANY_VALUE(u.manager_id) AS mid, " +
            "   ANY_VALUE(g.group_name) AS groupname, " +
            "   ANY_VALUE(ba.area_code) AS areacode, " +
            "   ANY_VALUE(ba.detailed_address) AS buyerAddress, " +
            "   ANY_VALUE(ba.consignee_name) AS buyerName, " +
            "   ANY_VALUE(ba.phone) AS buyerPhone " +
            "  FROM " +
            "   tb_buyer b " +
            "   JOIN tb_user u ON u.id = b.sale_user_id  " +
            "   JOIN tb_group g on g.group_no = u.group_no " +
            "   JOIN tb_buyer_address ba ON b.id = ba.buyer_id " +
            "   WHERE b.is_available = 1 " +
            "   AND ba.is_default = 1 " +
            "  ) ex " +
            "  JOIN tb_user u ON u.id = ex.mid " +
            "  JOIN tb_region r ON r.code = ex.areacode " +
            "  WHERE ex.time BETWEEN DATE_FORMAT( :startTime , '%Y-%m-%d' ) AND DATE_FORMAT( :endTime ,'%Y-%m-%d' ) " +
            "  ORDER BY ex.time ASC  ",nativeQuery = true)
    List<Map<String,Object>> findBuyerSaleByTimeToHtml(Date startTime ,Date endTime);
}