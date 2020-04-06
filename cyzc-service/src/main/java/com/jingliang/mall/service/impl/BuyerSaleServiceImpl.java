package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.repository.BuyerAddressRepository;
import com.jingliang.mall.repository.BuyerRepository;
import com.jingliang.mall.repository.BuyerSaleRepository;
import com.jingliang.mall.service.BuyerSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BuyerRepository buyerRepository;
    private final BuyerAddressRepository buyerAddressRepository;

    public BuyerSaleServiceImpl(BuyerSaleRepository buyerSaleRepository, BuyerRepository buyerRepository, BuyerAddressRepository buyerAddressRepository) {
        this.buyerSaleRepository = buyerSaleRepository;
        this.buyerRepository = buyerRepository;
        this.buyerAddressRepository = buyerAddressRepository;
    }

    @Override
    public BuyerSale save(BuyerSale buyerSale) {
        return buyerSaleRepository.save(buyerSale);
    }

    @Override
    public List<BuyerSale> saveAll(List<BuyerSale> buyerSale) {
        return buyerSaleRepository.saveAll(buyerSale);
    }

    @Override
    public List<BuyerSale> findAllBySaleIdAndBuyerIdAndIsAvailable(Long saleUserId, Long buyerId) {
        return buyerSaleRepository.findAllBySaleIdAndBuyerIdAndIsAvailable(saleUserId, buyerId, true);
    }

    @Override
    public List<BuyerSale> finAll(Specification<BuyerSale> buyerSaleSpecification) {
        return buyerSaleRepository.findAll(buyerSaleSpecification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Buyer bindingSale(BuyerSale buyerSale, Buyer buyer, BuyerAddress address) {
        buyerSaleRepository.save(buyerSale);
        BuyerAddress buyerAddress = buyerAddressRepository.findFirstByBuyerIdAndIsDefaultAndIsAvailable(buyer.getId(), true, true);
        if (buyerAddress != null) {
            buyerAddress.setIsDefault(false);
            buyerAddressRepository.save(buyerAddress);
        }
        buyerAddressRepository.save(address);
        return buyerRepository.saveAndFlush(buyer);
    }
}