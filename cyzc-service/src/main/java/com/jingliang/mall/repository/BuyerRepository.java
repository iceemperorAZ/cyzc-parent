package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
public interface BuyerRepository extends BaseRepository<Buyer, Long> {
    /**
     * 根据第三方唯一凭证和是否可用查询用户信息
     *
     * @param uniqueId    第三方唯一凭证
     * @param isAvailable 是否可用
     * @return 返回查询到的用户信息
     */
    Buyer findFirstByUniqueIdAndIsAvailable(String uniqueId, Boolean isAvailable);

    /**
     * 根据销售员Id查询绑定的会员列表
     *
     * @param saleUserId  销售员Id
     * @param isAvailable 是否可用
     * @return 返回查询到的会员列表
     */
    List<Buyer> findAllBySaleUserIdAndIsAvailable(Long saleUserId, Boolean isAvailable);

    /**
     * 分页查询商户信息
     *
     * @param saleUserId  绑定销售Id
     * @param isAvailable 是否可用
     * @param pageable    分页条件
     * @return 返回查询到的商户列表
     */
    Page<Buyer> findAllBySaleUserIdAndIsAvailable(Long saleUserId, Boolean isAvailable, Pageable pageable);

    /**
     * 根据销售Id分页查询
     *
     * @param saleUserId
     * @param isAvailable
     * @param pageRequest
     * @return
     */
    @Query("SELECT buyer FROM Buyer buyer  WHERE buyer.id in(SELECT DISTINCT buyerId  FROM BuyerSale WHERE saleId = :saleUserId and isAvailable=:isAvailable) and buyer.isAvailable=:isAvailable  ORDER BY buyer.id")
    Page<Buyer> findAllBySaleIdAndIsAvailable(@Param("saleUserId") Long saleUserId, @Param("isAvailable") Boolean isAvailable, Pageable pageRequest);

    Buyer findBuyersByIdAndAndIsAvailable(Long id, Boolean isAvailable);

    /**
     * 销售新增商户统计
     *
     * @return
     */
    @Query(value = "SELECT res.createTime,res.counts,res.userId,res.userName,tu.user_name AS manageName,   " +
            "res.groupName FROM (SELECT    " +
            "  ANY_VALUE(u.id) AS userId,   " +
            "  DATE_FORMAT(ANY_VALUE(b.create_time),'%Y-%m-%d') AS createTime,   " +
            "  count(b.id) AS counts,   " +
            "  ANY_VALUE(u.user_name) AS userName,   " +
            "  ANY_VALUE(g.group_name) AS groupName,   " +
            "  ANY_VALUE(g.group_no) AS groupNo   " +
            "  FROM tb_user u LEFT JOIN tb_buyer b ON b.sale_user_id = u.id   " +
            "  INNER JOIN tb_group g ON u.group_no = g.group_no AND g.parent_group_id=1   " +
            "  GROUP BY u.id,createTime   " +
            ") res INNER JOIN tb_user tu ON tu.group_no = res.groupNo AND tu.`level`=110   " +
            "ORDER BY res.createTime DESC", nativeQuery = true)
    List<Map<String, String>> countsByUserId();

    /**
     * 当前年销售下单量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    @Query(value = "SELECT a.days,temp.counts,temp.userId,temp.userName  " +
            "FROM  " +
            "  (SELECT 2019 AS days UNION SELECT 2020 UNION SELECT 2021 UNION SELECT 2022 UNION SELECT 2023 UNION  " +
            "  SELECT 2024 UNION SELECT 2025) a LEFT JOIN (  " +
            "    SELECT ANY_VALUE(u.id) AS userId,ANY_VALUE(u.user_name) AS userName,count(o.order_no) AS counts,  " +
            "    DATE_FORMAT(ANY_VALUE(o.create_time),'%Y') AS createTime  " +
            "    FROM tb_user u LEFT JOIN tb_buyer b ON b.sale_user_id=u.id  " +
            "    INNER JOIN tb_order o ON o.buyer_Id=b.id AND o.order_status BETWEEN 300 AND 700   " +
            "    AND o.create_time BETWEEN :startTime AND :endTime AND b.is_available = 1  " +
            "    GROUP BY userName ORDER BY counts DESC   " +
            "  ) AS temp ON a.days=temp.createTime AND a.days BETWEEN DATE_FORMAT(:startTime, '%Y')    " +
            "AND DATE_FORMAT(:endTime, '%Y')  " +
            "ORDER BY counts DESC LIMIT :topNum ", nativeQuery = true)
    List<Map<String, Object>> topOfOrderCountsByUser(Date startTime, Date endTime, Integer topNum);

    /**
     * 当前年商品销售量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    @Query(value = "SELECT a.days,temp.orderNo,temp.productName,temp.counts  " +
            "FROM (  " +
            "  SELECT 2020 AS days UNION SELECT 2021 UNION SELECT 2022 UNION SELECT 2023 UNION SELECT 2024 UNION  " +
            "  SELECT 2025) a LEFT JOIN (  " +
            "    SELECT ANY_VALUE(o.order_no) AS orderNo,ANY_VALUE(p.product_name) AS productName,count(p.id) AS counts,  " +
            "    DATE_FORMAT(ANY_VALUE(od.create_time),'%Y') AS createTime  " +
            "    FROM tb_order o INNER JOIN tb_order_detail od ON od.order_no=o.order_no AND  " +
            "    od.create_time BETWEEN :startTime AND :endTime AND o.order_status BETWEEN 300 AND 700  " +
            "    INNER JOIN tb_product p ON p.id=od.product_id GROUP BY productName,createTime ORDER BY counts DESC    " +
            "  ) AS temp ON a.days=temp.createTime AND a.days BETWEEN DATE_FORMAT(:startTime, '%Y')    " +
            "AND DATE_FORMAT(:endTime, '%Y')  " +
            "ORDER BY counts DESC LIMIT :topNum ", nativeQuery = true)
    List<Map<String, Object>> yeartopOfProductCountsByOrder(Date startTime, Date endTime, Integer topNum);

    /**
     * 当前月商品销售量top
     *
     * @param startTime
     * @param endTime
     * @param topNum
     * @return
     */
    @Query(value = "SELECT a.days,temp.orderNo,temp.productName,temp.counts  " +
            "FROM (  " +
            "  SELECT 1 AS days UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION  " +
            "  SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION   " +
            "  SELECT 11 UNION SELECT 12) a LEFT JOIN (  " +
            "    SELECT ANY_VALUE(o.order_no) AS orderNo,ANY_VALUE(p.product_name) AS productName,count(p.id) AS counts,  " +
            "    DATE_FORMAT(ANY_VALUE(od.create_time),'%m') AS createTime  " +
            "    FROM tb_order o INNER JOIN tb_order_detail od ON od.order_no=o.order_no AND  " +
            "    od.create_time BETWEEN :startTime AND :endTime AND o.order_status BETWEEN 300 AND 700  " +
            "    INNER JOIN tb_product p ON p.id=od.product_id GROUP BY productName,createTime ORDER BY counts DESC    " +
            "  ) AS temp ON a.days=temp.createTime AND a.days BETWEEN DATE_FORMAT(:startTime, '%m')    " +
            "AND DATE_FORMAT(:endTime, '%m')  " +
            "ORDER BY counts DESC LIMIT :topNum ", nativeQuery = true)
    List<Map<String, Object>> monthtopOfProductCountsByOrder(Date startTime, Date endTime, Integer topNum);
}