package com.jingliang.mall.repository;

import com.jingliang.mall.entity.AddressUserHistory;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 经纬度记录表Repository
 *
 * @author Mengde Liu
 * @version 1.0.0
 * @date 2020-04-28 13:30:05
 */
public interface AddressUserHistoryRepository extends BaseRepository<AddressUserHistory, Long> {

    /**
     * 根据userId查询经纬度记录
     *
     * @param userId
     * @return
     */
    @Query(value = "SELECT DISTINCT auh.user_id,auh.longitude,auh.latitude,DATE_FORMAT(auh.create_time,'%Y-%m-%d') AS createTime FROM tb_address_user_history auh WHERE DATE_FORMAT(auh.create_time,'%Y-%m-%d') " +
            " = DATE_FORMAT(:dateTime,'%Y-%m-%d') AND auh.user_id = :userId ORDER BY createTime DESC LIMIT 500 ", nativeQuery = true)
    List<Map<String, Object>> findAllTimeAndUserId(Date dateTime, Long userId);

    /**
     * 查询销售员的最后一条记录
     *
     * @return
     */
    @Query(value = "SELECT   " +
            " ANY_VALUE(g.group_no) AS groupNo,   " +
            " ANY_VALUE(u.id) AS userId,   " +
            " ANY_VALUE(u.user_name) AS userName,   " +
            " ANY_VALUE(u.phone) AS phone,   " +
            " ANY_VALUE(auh.longitude) AS longitude,   " +
            " ANY_VALUE(auh.latitude) AS latitude,   " +
            " DATE_FORMAT(auh.create_time,'%Y-%m-%d %H:%i:%s') AS createTime   " +
            " FROM tb_group g   " +
            " LEFT JOIN tb_user u ON u.group_no = g.group_no AND u.`level` BETWEEN 0 AND 199   " +
            " JOIN tb_address_user_history auh ON auh.user_id = u.id   " +
            " WHERE DATE_FORMAT(auh.create_time,'%Y-%m-%d') = DATE_FORMAT(:time,'%Y-%m-%d')   " +
            " GROUP BY u.id,auh.create_time AND DATE_FORMAT(auh.create_time,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')   " +
            " ORDER BY userName,auh.create_time DESC", nativeQuery = true)
    List<Map<String, Object>> userAddressHistoryToEndTime(Date time);

    @Query(value = "SELECT  " +
            "             ANY_VALUE(g.group_name) AS groupName,  " +
            "             ANY_VALUE(g.group_no) AS groupNo,  " +
            "             ANY_VALUE(u.id) AS userId,  " +
            "             ANY_VALUE(u.user_name) AS userName,  " +
            "             ANY_VALUE(u.phone) AS phone,  " +
            "             ANY_VALUE(auh.longitude) AS longitude,  " +
            "             ANY_VALUE(auh.latitude) AS latitude,  " +
            "             ANY_VALUE(auh.create_time) AS createTime  " +
            "             FROM tb_group g  " +
            "             LEFT JOIN tb_user u ON u.group_no = g.group_no AND u.`level` BETWEEN 0 AND 199  " +
            "             INNER JOIN tb_address_user_history auh ON auh.user_id = u.id  " +
            "             WHERE u.group_no = :groupNo AND DATE_FORMAT(auh.create_time,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') " +
            "             GROUP BY userId,auh.create_time  " +
            "             ORDER BY auh.create_time DESC  ", nativeQuery = true)
    List<Map<String, Object>> searchSaleByGroup(String groupNo);
}