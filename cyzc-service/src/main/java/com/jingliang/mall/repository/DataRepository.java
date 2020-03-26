package com.jingliang.mall.repository;

import com.jingliang.mall.entity.Coupon;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @date 2020-03-26 13:25:58
 */
public interface DataRepository extends BaseRepository<Coupon, Long> {

    /**
     * 查询所有的区域
     */
    @Query(value = "SELECT DISTINCT  u.region FROM  `tb_user` u;", nativeQuery = true)
    public List<String> allRegion();

    /**
     * 根据天统计全部商户
     */
    @Query(value = "SELECT DATE_FORMAT(b.create_time,'%Y-%m-%d') as datetime, count(1) as 'day' FROM `tb_buyer` as b WHERE DATE_FORMAT(b.create_time,'%Y-%m-%d')=DATE_FORMAT(:date,'%Y-%m-%d') GROUP BY DATE_FORMAT(b.create_time,'%Y-%m-%d');", nativeQuery = true)
    public Map<String,Object> countAllBuyerByDay(Date date);

    /**
     * 根据月份统计全部商户
     */
    @Query(value = "SELECT count(1) as  'month' FROM `tb_buyer` as b WHERE DATE_FORMAT(b.create_time,'%Y-%m')=DATE_FORMAT(:date,'%Y-%m')", nativeQuery = true)
    public Integer countAllBuyerByMonth(Date date);

    /**
     * 统计全部商户
     */
    @Query(value = "SELECT count(1) as  total FROM `tb_buyer` as b", nativeQuery = true)
    public Integer countAllBuyer();

    /**
     * 根据天和区域统计商户
     */
    @Query(value = "SELECT DATE_FORMAT(b.create_time,'%Y-%m-%d') as datetime ,count(1) as  'day' FROM `tb_buyer` as b JOIN tb_user as u ON b.sale_user_id=u.id JOIN tb_user m_u ON  u.manager_id=m_u.id  WHERE DATE_FORMAT(b.create_time,'%Y-%m-%d')=DATE_FORMAT(:date,'%Y-%m-%d') AND m_u.region=:region GROUP BY DATE_FORMAT(b.create_time,'%Y-%m-%d');", nativeQuery = true)
    public Map<String,Object>  countAllBuyerByRegionAndDay(Date date, String region);

    /**
     * 根据月分和区域统计商户
     */
    @Query(value = "SELECT count(1) as  'month' FROM `tb_buyer` as b JOIN tb_user as u ON b.sale_user_id=u.id JOIN tb_user m_u ON  u.manager_id=m_u.id  WHERE DATE_FORMAT(b.create_time,'%Y-%m') = DATE_FORMAT(:date,'%Y-%m') AND m_u.region=:region", nativeQuery = true)
    public Integer countAllBuyerByRegionAndMonth(Date date, String region);

    /**
     * 根据区域统计商户
     */
    @Query(value = "SELECT count(1) as  tatal FROM `tb_buyer` as b JOIN tb_user as u ON b.sale_user_id=u.id JOIN tb_user m_u ON  u.manager_id=m_u.id  WHERE  m_u.region=:region ;", nativeQuery = true)
    public Integer countAllBuyerByRegion(String region);

}
