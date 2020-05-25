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
     * 根据buyerId查询经纬度记录
     *
     * @param userId
     * @param isAvailable
     * @return
     */
    List<AddressUserHistory> findAllByUserIdAndIsAvailableOrderByCreateTimeAsc(Long userId, Boolean isAvailable);

    /**
     * 查询销售员的最后一条记录
     *
     * @return
     */
    @Query(value = "SELECT \n" +
            "ANY_VALUE(g.group_no) AS groupNo, \n" +
            "ANY_VALUE(u.id) AS userId, \n" +
            "ANY_VALUE(u.user_name) AS userName, \n" +
            "ANY_VALUE(u.phone) AS phone, \n" +
            "ANY_VALUE(auh.longitude) AS longitude, \n" +
            "ANY_VALUE(auh.latitude) AS latitude, \n" +
            "DATE_FORMAT(auh.create_time,'%Y-%m-%d %H:%i:%s') AS createTime \n" +
            "FROM tb_group g \n" +
            "LEFT JOIN tb_user u ON u.group_no = g.group_no AND u.`level` BETWEEN 0 AND 199 \n" +
            "JOIN tb_address_user_history auh ON auh.user_id = u.id \n" +
            "WHERE DATE_FORMAT(auh.create_time,'%Y-%m-%d') = DATE_FORMAT(:time,'%Y-%m-%d') \n" +
            "GROUP BY u.id,auh.create_time \n" +
            "ORDER BY userName,auh.create_time DESC", nativeQuery = true)
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
            "             LEFT JOIN tb_user u ON u.group_no = g.group_no AND u.`level`=100  " +
            "             INNER JOIN tb_address_user_history auh ON auh.user_id = u.id  " +
            "             WHERE u.group_no = :groupNo " +
            "             GROUP BY userId,auh.create_time  " +
            "             ORDER BY auh.create_time DESC  ", nativeQuery = true)
    List<Map<String, Object>> searchSaleByGroup(String groupNo);
}