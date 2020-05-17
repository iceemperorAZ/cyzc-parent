package com.jingliang.mall.service;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.entity.BuyerSale;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商户销售绑定表Service
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
public interface BuyerSaleService {

    /**
     * 保存
     *
     * @param buyerSale
     * @return
     */
    BuyerSale save(BuyerSale buyerSale);

    /**
     * 保存
     *
     * @param buyerSale
     * @return
     */
    List<BuyerSale> saveAll(List<BuyerSale> buyerSale);

    /**
     * 根据销售Id查询
     *
     * @param saleUserId
     * @param buyerId
     * @return
     */
    public List<BuyerSale> findAllBySaleIdAndBuyerIdAndIsAvailable(Long saleUserId, Long buyerId);

    /**
     * 根据条件查询绑定信息
     *
     * @param buyerSaleSpecification
     */
    List<BuyerSale> finAll(Specification<BuyerSale> buyerSaleSpecification);

    /**
     * 绑定销售
     *
     * @param buyerSale
     * @param buyer
     * @param address
     * @return
     */
    Buyer bindingSale(BuyerSale buyerSale, Buyer buyer, BuyerAddress address);


    public Boolean findBuyerSaleByTime(Date startTime, Date endTime);

    public List<Map<String,Object>> findBuyerSaleByTimeToHtml(Date startTime, Date endTime);
}