package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Group;
import com.jingliang.mall.repository.base.BaseRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 组Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 10:14:57
 */
public interface GroupRepository extends BaseRepository<Group, Long> {

    public Group findByParentGroupId(Long parentGroupId);

    public Group findGroupByParentGroupIdAndIsAvailable(@Param("parentGroupId") Long parentGroupId, @Param("isAvailable") Boolean isAvailable);

    public List<Group> findGroupsByParentGroupIdAndIsAvailable(Long parentGroupId, Boolean isAvailable);

    /**
     * 统计子节点数量
     *
     * @param parentGroupId
     * @param isAvailable
     * @return
     */
    public Integer countAllByParentGroupIdAndIsAvailable(Long parentGroupId, Boolean isAvailable);

    /**
     * 根据父组ID编号查询可用组信息
     *
     * @param parentGroupId 父组ID
     * @return 返回查询到的组信息z
     */
    public Group findFirstGroupById(Long parentGroupId);

    /**
     * 查询所有可用的组
     *
     * @param isAvailable
     * @return
     */
    List<Group> findGroupsByIsAvailable(boolean isAvailable);

    /**
     * 根据组名和父组Id查询
     *
     * @param groupName
     * @param parentGroupId
     * @param isAvailable
     * @return
     */
    Group findFirstByGroupNameAndParentGroupIdAndIsAvailable(String groupName, Long parentGroupId, boolean isAvailable);

    /**
     * 根据组编号查询
     *
     * @param groupNo
     * @param isAvailable
     * @return
     */
    Group findFirstByGroupNoAndIsAvailable(String groupNo, boolean isAvailable);

    /**
     * 根据分组模糊查询
     *
     * @param groupNo
     * @param isAvailable
     * @return
     */
    List<Group> findAllByGroupNoLikeAndIsAvailable(String groupNo, boolean isAvailable);

    /*
     * 柱状图-查询每个大区下的绩效
     * */
    @Query(value = "SELECT  " +
            "g.group_name AS groupName, " +
            "g.id AS id, " +
            "g.group_no AS groupNo, " +
            "g.parent_group_id AS parentGroupId, " +
            "CONVERT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,decimal(12,2)) AS totalPrice " +
            "FROM tb_order o " +
            "JOIN tb_group g " +
            "ON o.group_no  " +
            "LIKE CONCAT(regexp_replace(g.group_no ,'0*$',''), '%' ) " +
            "AND o.order_status " +
            "BETWEEN 300 " +
            "AND 700 " +
            "AND o.is_available = 1 " +
            "WHERE o.create_time  " +
            "BETWEEN :startTime  " +
            "AND :endTime " +
            "AND g.parent_group_id = -1", nativeQuery = true)
    List<Map<String, Object>> findAchievementsByMyself(Date startTime, Date endTime);

    /*
     * 柱状图-查询每个大区下的子分组绩效
     * */
    @Query(value = "SELECT " +
            "g.group_name AS groupName, " +
            "g.id AS id, " +
            "g.group_no AS groupNo, " +
            "g.parent_group_id AS parentGroupId, " +
            "CONVERT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,decimal(12,2)) AS totalPrice " +
            "FROM tb_order o " +
            "LEFT JOIN tb_group g " +
            "ON o.group_no " +
            "LIKE CONCAT(regexp_replace(g.group_no,'0*$',''), '%' ) " +
            "AND o.order_status " +
            "BETWEEN 300 AND 700 " +
            "AND o.is_available = 1 " +
            "AND o.create_time " +
            "BETWEEN :startTime " +
            "AND :endTime " +
            "WHERE g.parent_group_id = :parentGroupId " +
            "AND g.is_available = 1 " +
            "GROUP BY  g.id", nativeQuery = true)
    List<Map<String, Object>> findGroupAchievement(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 柱状图-查询子分组下各个销售的绩效
     *
     * */
    @Query(value = "SELECT " +
            "u.user_name AS userName, " +
            "CONVERT( IFNULL( SUM( o.total_price ), 0 ) * 0.01 ,decimal(12,2)) AS totalPrice " +
            "FROM tb_order o " +
            "JOIN tb_user u " +
            "ON o.sale_user_id = u.id " +
            "JOIN tb_group g " +
            "ON o.group_no = g.group_no " +
            "AND o.order_status " +
            "BETWEEN 300 AND 700 " +
            "AND o.is_available = 1 " +
            "AND o.create_time " +
            "BETWEEN :startTime AND :endTime " +
            "WHERE g.group_no = :groupNo " +
            "GROUP BY u.user_name", nativeQuery = true)
    List<Map<String, Object>> findUserAchievement(String groupNo, Date startTime, Date endTime);


    /*
     * 根据年份进行查询-折线图（总绩效）
     * */
    @Query(value = " SELECT  " +
            " a.eday AS date," +
            " IF(temp.id IS NULL, 0, temp.id) AS id," +
            " a.group_name AS groupName," +
            " IF(temp.totalPrice IS NULL, 0, temp.totalPrice) AS totalPrice " +
            " FROM ( SELECT dd.eday , tg.id , tg.group_name , tg.parent_group_id, tg.group_no FROM  " +
            " ( SELECT 2010 AS eday UNION" +
            " SELECT 2011 UNION" +
            " SELECT 2012 UNION" +
            " SELECT 2013 UNION" +
            " SELECT 2014 UNION" +
            " SELECT 2015 UNION" +
            " SELECT 2016 UNION" +
            " SELECT 2017 UNION" +
            " SELECT 2018 UNION" +
            " SELECT 2019 UNION" +
            " SELECT 2020 UNION" +
            " SELECT 2021) dd ,tb_group tg) a LEFT JOIN ( SELECT  " +
            " g.id as id , g.group_name as group_name , DATE_FORMAT( o.create_time, '%Y' ) as create_time , SUM(CONVERT( IFNULL( o.total_price , 0 ) * 0.01 ,decimal(12,2))) AS totalPrice" +
            " FROM  tb_group g  JOIN  tb_order o  ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' ) " +
            " WHERE " +
            " g.parent_group_id = :parentGroupId " +
            " AND o.create_time BETWEEN :startTime  AND :endTime " +
            " AND o.order_status BETWEEN 300 AND 700   " +
            " GROUP BY g.group_name,g.group_no," +
            " DATE_FORMAT( o.create_time, '%Y' )) as temp ON a.eday = temp.create_time " +
            " AND a.id = temp.id" +
            " WHERE  a.parent_group_id = :parentGroupId " +
            " AND a.eday BETWEEN DATE_FORMAT( :startTime , '%Y' )  " +
            " AND DATE_FORMAT( :endTime , '%Y' ) ", nativeQuery = true)
    List<Map<String, Object>> findGroupAchievementWithTimeByYear(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 根据月份进行查询-折线图（总绩效）
     * */
    @Query(value = " SELECT  " +
            " a.eday AS date," +
            " IF(temp.id IS NULL, 0, temp.id) AS id, " +
            " a.group_name AS groupName, " +
            " IF(temp.totalPrice IS NULL, 0, temp.totalPrice) AS totalPrice " +
            " FROM ( SELECT dd.eday, tg.id , tg.group_name , tg.parent_group_id , tg.group_no " +
            " FROM ( SELECT 1 AS eday UNION " +
            " SELECT 2 UNION " +
            " SELECT 3 UNION " +
            " SELECT 4 UNION " +
            " SELECT 5 UNION " +
            " SELECT 6 UNION " +
            " SELECT 7 UNION " +
            " SELECT 8 UNION " +
            " SELECT 9 UNION " +
            " SELECT 10 UNION " +
            " SELECT 11 UNION " +
            " SELECT 12 ) dd ,tb_group tg ) a LEFT JOIN ( SELECT  " +
            " g.id as id , g.group_name as group_name , DATE_FORMAT( o.create_time, '%c' ) as create_time , SUM(CONVERT( IFNULL( o.total_price , 0 ) * 0.01 ,decimal(12,2))) AS totalPrice" +
            " FROM  tb_group g JOIN  tb_order o  ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' ) " +
            " WHERE " +
            " g.parent_group_id = :parentGroupId " +
            " AND o.create_time BETWEEN :startTime AND :endTime " +
            " AND o.order_status BETWEEN 300 AND 700   " +
            " GROUP BY g.group_name,g.group_no," +
            " DATE_FORMAT( o.create_time, '%c' )) as temp ON a.eday = temp.create_time " +
            " AND a.id = temp.id" +
            " WHERE  a.parent_group_id = :parentGroupId " +
            " AND a.eday BETWEEN DATE_FORMAT( :startTime , '%c' )  " +
            " AND DATE_FORMAT( :endTime , '%c' ) ;", nativeQuery = true)
    List<Map<String, Object>> findGroupAchievementWithTimeByMonth(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 根据天数进行查询-折线图（总绩效）
     * */
    @Query(value = " SELECT  " +
            " a.eday AS date , " +
            " IF(temp.id IS NULL, 0, temp.id) AS id, " +
            " a.group_name AS groupName , " +
            " IF(temp.totalPrice IS NULL, 0, temp.totalPrice) AS totalPrice " +
            " FROM ( SELECT dd.eday , tg.id , tg.group_name , tg.parent_group_id, tg.group_no FROM  " +
            " ( SELECT 1 AS eday UNION " +
            " SELECT 2 UNION  " +
            " SELECT 3 UNION  " +
            " SELECT 4 UNION  " +
            " SELECT 5 UNION  " +
            " SELECT 6 UNION  " +
            " SELECT 7 UNION  " +
            " SELECT 8 UNION  " +
            " SELECT 9 UNION  " +
            " SELECT 10 UNION  " +
            " SELECT 11 UNION  " +
            " SELECT 12 UNION  " +
            " SELECT 13 UNION  " +
            " SELECT 14 UNION  " +
            " SELECT 15 UNION  " +
            " SELECT 16 UNION  " +
            " SELECT 17 UNION  " +
            " SELECT 18 UNION  " +
            " SELECT 19 UNION  " +
            " SELECT 20 UNION  " +
            " SELECT 21 UNION  " +
            " SELECT 22 UNION  " +
            " SELECT 23 UNION  " +
            " SELECT 24 UNION  " +
            " SELECT 25 UNION  " +
            " SELECT 26 UNION  " +
            " SELECT 27 UNION  " +
            " SELECT 28 UNION  " +
            " SELECT 29 UNION  " +
            " SELECT 30 UNION  " +
            " SELECT 31 ) dd ,tb_group tg) a LEFT JOIN ( SELECT  " +
            " g.id as id , g.group_name as group_name , DATE_FORMAT( o.create_time, '%e' ) as create_time , SUM(CONVERT( IFNULL( o.total_price , 0 ) * 0.01 ,decimal(12,2))) AS totalPrice " +
            " FROM  tb_group g  JOIN  tb_order o  ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' ) " +
            " WHERE " +
            " g.parent_group_id = :parentGroupId " +
            " AND o.create_time BETWEEN :startTime AND :endTime " +
            " AND o.order_status BETWEEN 300 AND 700   " +
            " GROUP BY g.group_name,g.group_no," +
            " DATE_FORMAT( o.create_time, '%e' )) as temp ON a.eday = temp.create_time " +
            " AND a.id = temp.id" +
            " WHERE  a.parent_group_id = :parentGroupId " +
            " AND a.eday BETWEEN DATE_FORMAT( :startTime , '%e' )  " +
            " AND DATE_FORMAT( :endTime, '%e' ) ;", nativeQuery = true)
    List<Map<String, Object>> findGroupAchievementWithTimeByDay(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 查询总销售量-柱-饼
     * */
    @Query(value = "SELECT " +
            "g.group_name AS groupName, " +
            "count( o.id ) AS counts, " +
            "g.id AS id " +
            "FROM tb_order o " +
            "JOIN tb_group g ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' )  " +
            "AND o.order_status BETWEEN 300  " +
            "AND 700  " +
            "AND o.is_available = 1  " +
            "AND o.create_time BETWEEN :startTime  " +
            "AND :endTime  " +
            "WHERE g.is_available = 1  " +
            "AND g.parent_group_id = :parentGroupId " +
            "GROUP BY g.id;", nativeQuery = true)
    List<Map<String, Object>> findOrdersTotalByGroup(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 根据年份查询订单数量
     * */
    @Query(value = " SELECT  " +
            " a.eday AS date, " +
            " IF(temp.id IS NULL, 0, temp.id) AS id, " +
            " a.group_name AS groupName, " +
            " IF(temp.counts IS NULL, 0,temp.counts) AS counts " +
            " FROM ( SELECT dd.eday , tg.id , tg.group_name , tg.parent_group_id, tg.group_no FROM  " +
            " (SELECT 2010 AS eday UNION " +
            " SELECT 2011 UNION " +
            " SELECT 2012 UNION " +
            " SELECT 2013 UNION " +
            " SELECT 2014 UNION " +
            " SELECT 2015 UNION " +
            " SELECT 2016 UNION " +
            " SELECT 2017 UNION " +
            " SELECT 2018 UNION " +
            " SELECT 2019 UNION " +
            " SELECT 2020 UNION " +
            " SELECT 2021) dd ,tb_group tg) a LEFT JOIN ( SELECT  " +
            " g.id as id , g.group_name as group_name , DATE_FORMAT( o.create_time, '%Y' ) as create_time , COUNT(o.id) AS counts " +
            " FROM  tb_group g  JOIN  tb_order o  ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' ) " +
            " WHERE " +
            " g.parent_group_id = :parentGroupId " +
            " AND o.create_time BETWEEN :startTime AND :endTime " +
            " AND o.order_status BETWEEN 300 AND 700   " +
            " GROUP BY g.group_name,g.group_no," +
            " DATE_FORMAT( o.create_time, '%Y' )) as temp ON a.eday = temp.create_time " +
            " AND a.id = temp.id" +
            " WHERE  a.parent_group_id = :parentGroupId " +
            " AND a.eday BETWEEN DATE_FORMAT( :startTime , '%Y' )  " +
            " AND DATE_FORMAT( :endTime , '%Y' ) ", nativeQuery = true)
    List<Map<String, Object>> findOrdersTotalByGroupAndYear(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 根据月份查询订单量
     * */
    @Query(value = " SELECT  " +
            " a.eday AS date," +
            " IF(temp.id IS NULL, 0, temp.id) AS id, " +
            " a.group_name AS groupName , "+
            " IF(temp.counts IS NULL, 0, temp.counts) AS counts " +
            " FROM ( SELECT dd.eday , tg.id , tg.group_name , tg.parent_group_id, tg.group_no FROM  " +
            " ( SELECT 1 AS eday UNION " +
            " SELECT 2 UNION " +
            " SELECT 3 UNION " +
            " SELECT 4 UNION " +
            " SELECT 5 UNION " +
            " SELECT 6 UNION " +
            " SELECT 7 UNION " +
            " SELECT 8 UNION " +
            " SELECT 9 UNION " +
            " SELECT 10 UNION " +
            " SELECT 11 UNION " +
            " SELECT 12) dd ,tb_group tg) a LEFT JOIN ( SELECT  " +
            " g.id as id , g.group_name as group_name , DATE_FORMAT( o.create_time, '%c' ) as create_time , COUNT(o.id) AS counts " +
            " FROM  tb_group g  JOIN  tb_order o  ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' ) " +
            " WHERE " +
            " g.parent_group_id = :parentGroupId " +
            " AND o.create_time BETWEEN :startTime AND :endTime " +
            " AND o.order_status BETWEEN 300 AND 700   " +
            " GROUP BY g.group_name,g.group_no, " +
            " DATE_FORMAT( o.create_time, '%c' )) as temp ON a.eday = temp.create_time " +
            " AND a.id = temp.id " +
            " WHERE  a.parent_group_id = :parentGroupId " +
            " AND a.eday BETWEEN DATE_FORMAT( :startTime, '%c' )  " +
            " AND DATE_FORMAT( :endTime , '%c' ) ", nativeQuery = true)
    List<Map<String, Object>> findOrdersTotalByGroupAndMonth(Long parentGroupId, Date startTime, Date endTime);

    /*
     * 根据日期查询订单量
     * */
    @Query(value = "SELECT   " +
            "a.eday AS date, " +
            "IF(temp.id IS NULL, 0, temp.id) AS id, " +
            "a.group_name AS groupName, " +
            "IF(temp.counts IS NULL, 0,temp.counts) AS counts" +
            "FROM ( SELECT dd.eday , tg.id , tg.group_name , tg.parent_group_id, tg.group_no FROM   " +
            "(SELECT 1 AS eday UNION  " +
            "SELECT 2 UNION  " +
            "SELECT 3 UNION   " +
            "SELECT 4 UNION   " +
            "SELECT 5 UNION   " +
            "SELECT 6 UNION   " +
            "SELECT 7 UNION   " +
            "SELECT 8 UNION   " +
            "SELECT 9 UNION   " +
            "SELECT 10 UNION   " +
            "SELECT 11 UNION   " +
            "SELECT 12 UNION   " +
            "SELECT 13 UNION   " +
            "SELECT 14 UNION   " +
            "SELECT 15 UNION   " +
            "SELECT 16 UNION   " +
            "SELECT 17 UNION   " +
            "SELECT 18 UNION   " +
            "SELECT 19 UNION   " +
            "SELECT 20 UNION   " +
            "SELECT 21 UNION   " +
            "SELECT 22 UNION   " +
            "SELECT 23 UNION   " +
            "SELECT 24 UNION   " +
            "SELECT 25 UNION   " +
            "SELECT 26 UNION   " +
            "SELECT 27 UNION   " +
            "SELECT 28 UNION   " +
            "SELECT 29 UNION   " +
            "SELECT 30 UNION   " +
            "SELECT 31) dd ,tb_group tg) a LEFT JOIN ( SELECT   " +
            "g.id as id , g.group_name as group_name , DATE_FORMAT( o.create_time, '%e' ) as create_time , COUNT(o.id) AS counts " +
            "FROM  tb_group g  JOIN  tb_order o  ON o.group_no LIKE CONCAT( regexp_replace ( g.group_no, '0*$', '' ), '%' )  " +
            "WHERE  " +
            "g.parent_group_id = :parentGroupId " +
            "AND o.create_time BETWEEN :startTime  AND :endTime  " +
            "AND o.order_status BETWEEN 300 AND 700    " +
            "GROUP BY g.group_name,g.group_no, " +
            "DATE_FORMAT( o.create_time, '%e' )) as temp ON a.eday = temp.create_time  " +
            "AND a.id = temp.id " +
            "WHERE  a.parent_group_id = :parentGroupId " +
            "AND a.eday BETWEEN DATE_FORMAT( :startTime , '%e' )   " +
            "AND DATE_FORMAT( :endTime , '%e' ) ", nativeQuery = true)
    List<Map<String, Object>> findOrdersTotalByGroupAndDay(Long parentGroupId, Date startTime, Date endTime);
}