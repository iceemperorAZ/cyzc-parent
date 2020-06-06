package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Order;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
public interface OrderRepository extends BaseRepository<Order, Long> {
    /**
     * 根据订单Id和会员Id查询订单信息
     *
     * @param id      主键Id
     * @param buyerId 会员Id
     * @return 返回查询到的订单信息
     */
    Order findFirstByIdAndBuyerId(Long id, Long buyerId);

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo 订单编号
     * @return 返回查询到的订单信息
     */
    Order findFirstByOrderNo(String orderNo);

    /**
     * 查询分组下的所有订单
     *
     * @param isAvailable
     * @param groupNo
     * @return
     */
    List<Order> findAllByIsAvailableAndGroupNoLike(boolean isAvailable, String groupNo);

    /**
     * 根据组编号统计组下的业绩
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT g.id, g.group_no AS groupNo, g.group_name AS groupName, FORMAT(IFNULL( SUM( o.total_price ), 0 ) * 0.01,2) AS totalPrice, FORMAT(IFNULL( SUM( o.payable_fee ), 0 ) * 0.01,2) AS payableFee, FORMAT(IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01,2) AS preferentialFee, FORMAT(IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01 ,2) AS royaltyPrice\n" +
            "FROM tb_group g LEFT JOIN tb_order o ON o.group_no LIKE CONCAT(regexp_replace(g.group_no ,'0*$',''), '%' )  AND o.order_status BETWEEN 300  AND 700 AND o.is_available = 1  AND o.create_time BETWEEN :startTime AND :endTime \n" +
            "WHERE g.group_no = :groupNo   AND g.is_available = 1 " +
            "GROUP BY g.id, g.group_name;", nativeQuery = true)
    List<Map<String, Object>> bossONeGroupAchievement(String groupNo, Date startTime, Date endTime);


    /**
     * 根据父分组分组统计组下业绩
     *
     * @param parentGroupId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT g.id, g.group_no AS groupNo, g.group_name AS groupName,  FORMAT( IFNULL( SUM( o.total_price ), 0 ) * 0.01  ,2) AS totalPrice,  FORMAT( IFNULL( SUM( o.payable_fee ), 0 ) * 0.01 ,2) AS payableFee,  FORMAT( IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01 ,2) AS preferentialFee,  FORMAT( IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01 ,2) AS royaltyPrice \n" +
            "FROM  tb_group g  LEFT JOIN tb_order o ON o.group_no LIKE CONCAT( REGEXP_REPLACE( g.group_no, '0*$', '' ), '%' ) AND o.order_status BETWEEN 300   AND 700  AND o.is_available = 1 AND o.create_time BETWEEN :startTime   AND :endTime \n" +
            "WHERE  g.parent_group_id = :parentGroupId   AND g.is_available = 1   GROUP BY  g.id,g.group_name;", nativeQuery = true)
    List<Map<String, Object>> bossGroupAchievement(Long parentGroupId, Date startTime, Date endTime);

    /**
     * 根据分组查询分组下的用户（销售、区域经理、老板）业绩
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT u.id, u.group_no AS groupNo, u.user_name AS userName, u.phone, FORMAT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL( SUM( o.payable_fee ), 0 ) * 0.01,2) AS payableFee, FORMAT( IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01,2) AS preferentialFee, FORMAT( IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01,2)  AS royaltyPrice FROM tb_user u LEFT JOIN tb_order o " +
            "ON u.id = o.sale_user_id AND o.group_no LIKE :groupNo AND o.order_status BETWEEN 300 AND 700 AND o.create_time BETWEEN :startTime AND :endTime AND o.is_available = 1 WHERE u.group_no like :groupNo  AND u.is_available = 1 GROUP BY u.group_no, u.id, u.user_name, u.phone ORDER BY u.group_no ASC", nativeQuery = true)
    List<Map<String, Object>> bossGroupUserAchievement(String groupNo, Date startTime, Date endTime);

    /**
     * 统计指定销售在自己组下所有商户产生的绩效
     *
     * @param groupNo
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT b.id, b.user_name AS userName, b.phone, b.shop_name AS shopName, FORMAT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL( SUM( o.payable_fee ), 0 ) * 0.01 ,2) AS payableFee, FORMAT( IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01 ,2) AS preferentialFee, FORMAT( IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_buyer b LEFT JOIN tb_order o ON b.id = o.buyer_id  AND o.group_no LIKE :groupNo AND o.order_status BETWEEN 300  AND 700  AND o.create_time BETWEEN :startTime AND :endTime \n" +
            "WHERE o.sale_user_id=:saleUserId AND o.is_available = 1  AND b.is_available = 1 \n" +
            "GROUP BY b.id, b.shop_name, b.user_name, b.phone", nativeQuery = true)
    List<Map<String, Object>> bossUserBuyerAchievement(String groupNo, Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询销售自己所有商户产生的绩效
     *
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT b.id, b.user_name AS userName, b.shop_name AS shopName, b.phone, FORMAT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL( SUM( o.payable_fee ), 0 ) * 0.01 ,2) AS payableFee, FORMAT( IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01 ,2) AS preferentialFee, FORMAT( IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_buyer b LEFT JOIN tb_order o ON b.id = o.buyer_id   AND o.order_status BETWEEN 300  AND 700  AND o.create_time BETWEEN :startTime AND :endTime \n" +
            "WHERE o.sale_user_id=:saleUserId AND o.is_available = 1  AND b.is_available = 1  \n" +
            "GROUP BY b.id, b.shop_name, b.phone, b.user_name", nativeQuery = true)
    List<Map<String, Object>> bossSelfBuyerAchievement(Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询销售自己所有商户产生的总绩效
     *
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT  FORMAT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL( SUM( o.payable_fee ), 0 ) * 0.01 ,2) AS payableFee, FORMAT( IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01 ,2) AS preferentialFee, FORMAT( IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_order o\n" +
            "WHERE o.order_status BETWEEN 300  AND 700  AND o.is_available = 1  AND o.create_time BETWEEN :startTime AND :endTime  AND o.sale_user_id = :saleUserId", nativeQuery = true)
    Map<String, Object> bossSelfAchievement(Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询指定销售在自己组下所有商户产生的总绩效
     *
     * @param groupNo
     * @param saleUserId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT  FORMAT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL( SUM( o.payable_fee ), 0 ) * 0.01 ,2) AS payableFee, FORMAT( IFNULL( SUM( o.preferential_fee ), 0 ) * 0.01 ,2) AS preferentialFee, FORMAT( IFNULL( SUM( o.total_price * o.ratio * 0.001 ), 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_order o\n" +
            "WHERE o.order_status BETWEEN 300  AND 700  AND o.is_available = 1  AND o.create_time BETWEEN :startTime AND :endTime AND o.group_no LIKE :groupNo  AND o.sale_user_id = :saleUserId", nativeQuery = true)
    Map<String, Object> bossUserAchievement(String groupNo, Long saleUserId, Date startTime, Date endTime);

    /**
     * 查询销售自己的商户所有订单产生的绩效
     *
     * @param saleUserId
     * @param buyerId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT  o.id, o.order_no AS orderNo, FORMAT( IFNULL(  o.total_price, 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL(  o.payable_fee , 0 ) * 0.01 ,2) AS payableFee, FORMAT( IFNULL( o.preferential_fee , 0 ) * 0.01 ,2) AS preferentialFee, FORMAT( IFNULL(  o.total_price * o.ratio * 0.001 , 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_order o\n" +
            "WHERE o.order_status BETWEEN 300  AND 700  AND o.is_available = 1  AND o.create_time BETWEEN :startTime AND :endTime  AND o.sale_user_id=:saleUserId AND o.buyer_id= :buyerId ", nativeQuery = true)
    List<Map<String, Object>> bossSelfBuyerOrderAchievement(Long saleUserId, Long buyerId, Date startTime, Date endTime);

    /**
     * 查询指定销售在自己组下的商户所有订单产生的绩效
     *
     * @param groupNo
     * @param buyerId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT  o.id, o.order_no AS orderNo, FORMAT( IFNULL(  o.total_price, 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL(  o.payable_fee , 0 ) * 0.01 ,2) AS payableFee, FORMAT( IFNULL( o.preferential_fee , 0 ) * 0.01 ,2) AS preferentialFee, FORMAT( IFNULL(  o.total_price * o.ratio * 0.001 , 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_order o\n" +
            "WHERE o.order_status BETWEEN 300  AND 700  AND o.is_available = 1  AND o.create_time BETWEEN :startTime AND :endTime  AND o.buyer_id = :buyerId AND o.group_no LIKE :groupNo ", nativeQuery = true)
    List<Map<String, Object>> bossUserBuyerOrderAchievement(String groupNo, Long buyerId, Date startTime, Date endTime);

    /**
     * 查询订单详情产生的绩效
     *
     * @param orderId
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT  p.product_name AS productName, FORMAT( IFNULL(  o_d.selling_price * o_d.product_num, 0 ) * 0.01 ,2) AS totalPrice, FORMAT( IFNULL(  o_d.selling_price * o_d.product_num, 0 ) * 0.01 ,2) AS payableFee, 0 AS preferentialFee, FORMAT( IFNULL(  o_d.selling_price * o_d.product_num * o.ratio * 0.001 , 0 ) * 0.01  ,2)  AS royaltyPrice \n" +
            "FROM tb_order_detail o_d LEFT JOIN tb_order o ON o_d.order_id = o.id LEFT JOIN tb_product p ON o_d.product_id= p.id " +
            "WHERE o.order_status BETWEEN 300  AND 700  AND o.is_available = 1  AND o.create_time BETWEEN :startTime AND :endTime  AND o.id = :orderId", nativeQuery = true)
    List<Map<String, Object>> bossBuyerOrderDetailAchievement(Long orderId, Date startTime, Date endTime);

    /**
     * 查询分组统计各个组下的商品大类所产生的绩效
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT " +
            "p_t.product_type_name AS productTypeName, " +
            "FORMAT( SUM( IFNULL(  o_d.selling_price * o_d.product_num, 0 )) * 0.01 ,2) AS totalPrice," +
            "FORMAT( SUM( IFNULL(  o_d.selling_price * o_d.product_num, 0 )) * 0.01 ,2) AS payableFee," +
            "0 AS preferentialFee," +
            "FORMAT( SUM(IFNULL(  o_d.selling_price * o_d.product_num * o.ratio * 0.001 , 0 )) * 0.01  ,2)  AS royaltyPrice,\n" +
            "SUM(o_d.product_num) AS salesVolume " +
            "FROM tb_order_detail AS o_d  JOIN tb_order o ON o_d.order_id = o.id JOIN tb_group g ON o.group_no LIKE :groupNo JOIN tb_product p ON o_d.product_id = p.id JOIN tb_product_type p_t ON p.product_type_id = p_t.id " +
            "WHERE o.order_status BETWEEN 300 AND 700 AND o.create_time BETWEEN  :startTime AND :endTime AND o.is_available = 1 AND o_d.is_available = 1 AND  p.is_available = 1 AND p_t.is_available = 1 GROUP BY p_t.product_type_name ORDER BY SUM(o_d.product_num) DESC", nativeQuery = true)
    List<Map<String, Object>> bossGroupProductTypeAchievement(String groupNo, Date startTime, Date endTime);

    /**
     * 查询分组统计各个组下的商品所产生的绩效
     *
     * @param groupNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "SELECT  p.product_name AS productName, FORMAT( SUM( IFNULL(  o_d.selling_price * o_d.product_num, 0 )) * 0.01,2) AS totalPrice, FORMAT( SUM( IFNULL(  o_d.selling_price * o_d.product_num, 0 )) * 0.01 ,2) AS payableFee, 0 AS preferentialFee, FORMAT( SUM(IFNULL(  o_d.selling_price * o_d.product_num * o.ratio * 0.001 , 0 )) * 0.01  ,2)  AS royaltyPrice, SUM(o_d.product_num) AS salesVolume \n" +
            "FROM tb_order_detail AS o_d  JOIN tb_order o ON o_d.order_id = o.id JOIN tb_group g ON o.group_no LIKE :groupNo  JOIN tb_product p ON o_d.product_id = p.id  " +
            "WHERE o.order_status BETWEEN 300 AND 700 AND o.create_time BETWEEN :startTime AND :endTime AND  o.is_available = 1 AND o_d.is_available = 1 AND  p.is_available = 1 GROUP BY p.product_name ORDER BY SUM(o_d.product_num) DESC", nativeQuery = true)
    List<Map<String, Object>> bossGroupProductAchievement(String groupNo, Date startTime, Date endTime);

    @Query(value = "SELECT @cdate \\:= date_add(@cdate,interval -1 day) days from   \n" +
            "(SELECT @cdate \\:= CURDATE() from tb_order limit 10) t1  ", nativeQuery = true)
    List<Map<String, Object>> x();

    @Query(value = "SELECT o.id as id ,o.order_no as orderNo,o.buyer_id as bid ,p.product_name FROM tb_order o JOIN tb_order_detail od ON o.order_no = od.order_no JOIN tb_product p ON p.id = od.product_id JOIN tb_product_type pt ON p.product_type_id = 2020030121 WHERE o.id = :id ", nativeQuery = true)
    List<Map<String, Object>> orderHasDrunk(Integer id);

    /**
     * 导出订单优化
     *
     * @return
     */
    @Query(value = "SELECT o.create_time AS createTime,o.id AS orderId,o.order_no AS orderNo,g.group_name AS groupName,b.id AS buyerId,b.user_name AS buyerName,b.shop_name AS shopName,u.user_name AS userName,o.preferential_fee AS preferentialFee,o.total_price AS totalPrice,o.payable_fee AS payableFee,o.is_gold AS isGold,o.gold AS gold,o.order_status AS orderStatus,o.return_gold AS returnGold,o.pay_way AS payWay,p.product_no AS productNo,p.product_name AS productName,p.specs,pt.product_type_name AS productTypeName,od.product_num AS productNum,od.selling_price AS sellingPrice,o.storehouse,o.detail_address AS detailAddress,o.receiver_name AS receiverName,o.receiver_phone AS receiverPhone  \n" +
            "FROM tb_order o INNER JOIN tb_order_detail od ON od.order_no=o.order_no INNER JOIN tb_group g ON g.group_no=o.group_no INNER JOIN tb_product p ON p.id=od.product_id INNER JOIN tb_buyer b ON b.id=o.buyer_id INNER JOIN tb_user u ON u.id=b.sale_user_id INNER JOIN tb_product_type pt ON pt.id=p.product_type_id WHERE o.order_status BETWEEN 300 AND 599  \n" +
            "UNION ALL \n" +
            "SELECT o.create_time AS createTime,o.id AS orderId,o.order_no AS orderNo,g.group_name AS groupName,b.id AS buyerId,b.user_name AS buyerName,b.shop_name AS shopName,u.user_name AS userName,o.preferential_fee AS preferentialFee,o.total_price AS totalPrice,o.payable_fee AS payableFee,o.is_gold AS isGold,o.gold AS gold,o.order_status AS orderStatus,o.return_gold AS returnGold,o.pay_way AS payWay,p.product_no AS productNo,p.product_name AS productName,p.specs,pt.product_type_name AS productTypeName,od.product_num AS productNum,od.selling_price AS sellingPrice,o.storehouse,o.detail_address AS detailAddress,o.receiver_name AS receiverName,o.receiver_phone AS receiverPhone  \n" +
            "FROM tb_order o INNER JOIN tb_order_detail od ON od.order_no=o.order_no INNER JOIN tb_group g ON g.group_no=o.group_no INNER JOIN tb_product p ON p.id=od.product_id INNER JOIN tb_buyer b ON b.id=o.buyer_id INNER JOIN tb_user u ON u.id=b.sale_user_id INNER JOIN tb_product_type pt ON pt.id=p.product_type_id WHERE o.order_status BETWEEN 600 AND 999\n" +
            "UNION ALL\n" +
            "SELECT o.create_time AS createTime,o.id AS orderId,o.order_no AS orderNo,g.group_name AS groupName,b.id AS buyerId,b.user_name AS buyerName,b.shop_name AS shopName,u.user_name AS userName,o.preferential_fee AS preferentialFee,o.total_price AS totalPrice,o.payable_fee AS payableFee,o.is_gold AS isGold,o.gold AS gold,o.order_status AS orderStatus,o.return_gold AS returnGold,o.pay_way AS payWay,p.product_no AS productNo,p.product_name AS productName,p.specs,pt.product_type_name AS productTypeName,od.product_num AS productNum,od.selling_price AS sellingPrice,o.storehouse,o.detail_address AS detailAddress,o.receiver_name AS receiverName,o.receiver_phone AS receiverPhone  \n" +
            "FROM tb_order o INNER JOIN tb_order_detail od ON od.order_no=o.order_no INNER JOIN tb_group g ON g.group_no=o.group_no INNER JOIN tb_product p ON p.id=od.product_id INNER JOIN tb_buyer b ON b.id=o.buyer_id INNER JOIN tb_user u ON u.id=b.sale_user_id INNER JOIN tb_product_type pt ON pt.id=p.product_type_id WHERE o.order_status BETWEEN 0 AND 299", nativeQuery = true)
    List<Map<String, String>> orderExcel();

    /**
     * 查询总商户
     *
     * @return
     */
    @Query(value = "SELECT COUNT(1) as count FROM tb_buyer", nativeQuery = true)
    Integer countBuyerAll();

    /**
     * 查询总活跃商户
     *
     * @return
     */
    @Query(value = "SELECT COUNT(1) as count FROM tb_buyer WHERE sale_user_id IS NOT NULL", nativeQuery = true)
    Integer countActiveBuyerAll();

    /**
     * 查询总待激活商户
     *
     * @return
     */
    @Query(value = "SELECT COUNT(1) as count FROM tb_buyer WHERE sale_user_id IS NULL", nativeQuery = true)
    Integer countInactiveBuyerAll();

    /**
     * 总月新增
     *
     * @param groupNo
     * @param date
     * @return
     */
    @Query(value = "SELECT g.group_no as groupNo,g.group_name as groupName,COUNT(1) as count FROM tb_buyer b JOIN tb_user u ON b.sale_user_id = u.id JOIN tb_group g ON g.group_no LIKE CONCAT(regexp_replace(u.group_no ,'0*$',''), '%' ) WHERE g.group_no like :groupNo AND DATE_FORMAT(b.create_time,'%Y-%m') = DATE_FORMAT(:date,'%Y-%m') GROUP BY g.group_no,g.group_name;\n", nativeQuery = true)
    List<Map<String, Object>> monthIncrease(String groupNo, Date date);

    /**
     * 总日新增
     *
     * @param groupNo
     * @param date
     * @return
     */
    @Query(value = "SELECT g.group_no as groupNo,g.group_name as groupName,COUNT(1) as count FROM tb_buyer b JOIN tb_user u ON b.sale_user_id = u.id JOIN tb_group g ON g.group_no LIKE CONCAT(regexp_replace(u.group_no ,'0*$',''), '%' ) WHERE g.group_no like :groupNo AND DATE_FORMAT(b.create_time,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d') GROUP BY g.group_no,g.group_name;", nativeQuery = true)
    List<Map<String, Object>> dayIncrease(String groupNo, Date date);

    /**
     * 总新增
     *
     * @param groupNo
     * @return
     */
    @Query(value = "SELECT g.group_no as groupNo,g.group_name as groupName,COUNT(1) as count FROM tb_buyer b JOIN tb_user u ON b.sale_user_id = u.id JOIN tb_group g ON g.group_no LIKE CONCAT(regexp_replace(u.group_no ,'0*$',''), '%' ) WHERE g.group_no like :groupNo  GROUP BY g.group_no,g.group_name;", nativeQuery = true)
    List<Map<String, Object>> allIncrease(String groupNo);

    /**
     * 查询总的月新增商户
     *
     * @param date
     * @return
     */
    @Query(value = "SELECT COUNT(1) as count FROM tb_buyer b WHERE DATE_FORMAT(b.create_time,'%Y-%m') = DATE_FORMAT(:date,'%Y-%m')", nativeQuery = true)
    Integer totalMonthBuyerAll(Date date);

    /**
     * 查询总的日新增商户
     *
     * @param date
     * @return
     */
    @Query(value = "SELECT COUNT(1) as count FROM tb_buyer b WHERE DATE_FORMAT(b.create_time,'%Y-%m-%d') = DATE_FORMAT(:date,'%Y-%m-%d')", nativeQuery = true)
    Integer totalDayBuyerAll(Date date);
}