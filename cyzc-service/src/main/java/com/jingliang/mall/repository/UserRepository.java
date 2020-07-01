package com.jingliang.mall.repository;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 员工表Repository
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-22 14:40:54
 */
public interface UserRepository extends BaseRepository<User, Long> {
    /**
     * 根据员工编号查询可用用户信息
     *
     * @param userNo      员工编号
     * @param isAvailable 是否可用
     * @return 返回查询到的用户信息z
     */
    public User findFirstByUserNoAndIsAvailable(String userNo, Boolean isAvailable);

    /**
     * 根据会员Id查询员工信息
     *
     * @param buyerId     会员Id
     * @param isAvailable 是否可用
     * @return 员工Id
     */
    User findByBuyerIdAndIsAvailable(Long buyerId, Boolean isAvailable);

    /**
     * 根据用户名和密码查询可用用户信息
     *
     * @param loginName   登录名
     * @param isAvailable 是否可用
     * @return 返回查询到的用户信息
     */
    public User findFirstByLoginNameAndIsAvailable(String loginName, Boolean isAvailable);

    /**
     * 根据等级查询
     *
     * @param level
     * @param isAvailable
     * @return
     */
    List<User> findAllByLevelAndIsAvailable(Integer level, Boolean isAvailable);

    /**
     * 根据组编号分页查询用户
     *
     * @param groupNo 组编号
     * @return
     */
    public Page<User> findAllByGroupNoAndIsAvailable(String groupNo, Pageable pageable, Boolean isAvailable);

    /**
     * 根据用户id
     *
     * @param id          用户id
     * @param isAvailable 是否可用
     * @return
     */
    public User findFirstByIdAndIsAvailable(Long id, Boolean isAvailable);

    /**
     * 根据分组编号查询
     *
     * @param isAvailable
     * @param groupNo
     * @return
     */
    List<User> findAllByIsAvailableAndGroupNoLikeOrderByGroupNoAscLevelDesc(boolean isAvailable, String groupNo);

    /**
     * 查询所有未分组的用户
     *
     * @param isAvailable
     * @param groupNo
     * @return
     */
    List<User> findAllByIsAvailableAndGroupNo(boolean isAvailable, String groupNo);

    /**
     * 分配用户到组
     *
     * @param groupNo
     * @param userIds
     */
    @Modifying
    @Query(value = "UPDATE User u SET u.groupNo=:groupNo WHERE u.id IN(:userIds)")
    void distribution(String groupNo, List<Long> userIds);

    /**
     * 移除用户到未分配
     *
     * @param userIds
     */
    @Modifying
    @Query(value = "UPDATE User u SET u.groupNo='-1' WHERE u.id IN(:userIds)")
    void removeToUngrouped(List<Long> userIds);

    /**
     * 统计组下的成员
     *
     * @param groupNo
     * @param isAvailable
     * @return
     */
    Integer countByGroupNoAndIsAvailable(String groupNo, boolean isAvailable);

    /**
     * 查询组下的所有销售和其名下商户数
     *
     * @param groupNo
     * @return
     */
    @Query(value = "select  " +
            " ANY_VALUE(u.id) AS id,  " +
            " ANY_VALUE(u.user_name) AS userName,  " +
            " ANY_VALUE(u.group_no) AS groupNo,  " +
            " ANY_VALUE(u.`level`) AS `level`,  " +
            " count(*) AS counts  " +
            " from tb_group g  " +
            " left join tb_user u on  " +
            " u.group_no LIKE CONCAT(regexp_replace(g.group_no ,'0*$',''), '%' )  " +
            " and u.group_no = g.group_no  " +
            " inner join tb_buyer b on b.sale_user_id = u.id  " +
            " WHERE g.group_no = :groupNo AND u.is_available=1  " +
            " GROUP BY u.user_name", nativeQuery = true)
    List<Map<String, Object>> findAllBySaleIdAndGroupNo(String groupNo);

    /**
     * 根据手机号模糊查询
     *
     * @param phone
     * @return
     */
    @Query(value = "SELECT group_no AS groupNo,user_name AS userName,phone,id FROM tb_user WHERE phone LIKE :phone% AND is_available=1", nativeQuery = true)
    List<Map<String, Object>> findAllByPhoneLike(String phone);

    /**
     * 根据手机号查询
     *
     * @param isAvailable
     * @param phone
     * @return
     */
    User findAllByIsAvailableAndPhone(Boolean isAvailable, String phone);
}