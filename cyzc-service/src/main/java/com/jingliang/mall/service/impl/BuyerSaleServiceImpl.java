package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.repository.BuyerSaleRepository;
import com.jingliang.mall.service.BuyerSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户销售绑定表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-03 13:10:34
 */
@Service
@Slf4j
public class BuyerSaleServiceImpl implements BuyerSaleService {

    private final BuyerSaleRepository buyerSaleRepository;

    public BuyerSaleServiceImpl(BuyerSaleRepository buyerSaleRepository) {
        this.buyerSaleRepository = buyerSaleRepository;
    }

    @Override
    public BuyerSale save(BuyerSale buyerSale) {
        return buyerSaleRepository.save(buyerSale);
    }

    @Override
    public BuyerSale findBySaleIdAndBuyerId(Long saleUserId, Long buyerId) {
        return buyerSaleRepository.findAllBySaleIdAndBuyerIdAndUntyingTimeIsNullAndIsAvailable(saleUserId, buyerId, true);
    }

    @Override
    public List<BuyerSale> finAll(Specification<BuyerSale> buyerSaleSpecification) {
        return buyerSaleRepository.findAll(buyerSaleSpecification);
    }

}