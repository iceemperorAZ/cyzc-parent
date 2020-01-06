package com.jingliang.mall.repository;

import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.repository.base.BaseRepository;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
}