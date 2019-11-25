package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.BuyerAddress;
import com.jingliang.mall.repository.BuyerAddressRepository;
import com.jingliang.mall.service.BuyerAddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 会员收货地址表ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-23 09:33:48
 */
@Service
@Slf4j
public class BuyerAddressServiceImpl implements BuyerAddressService {

    private final BuyerAddressRepository buyerAddressRepository;

    public BuyerAddressServiceImpl(BuyerAddressRepository buyerAddressRepository) {
        this.buyerAddressRepository = buyerAddressRepository;
    }

    @Override
    public BuyerAddress save(BuyerAddress buyerAddress) {
        if (Objects.equals(buyerAddress.getIsDefault(), true)) {
            //如果当前地址为默认则把之前的默认清除
            BuyerAddress defaultAddress = buyerAddressRepository.findFirstByBuyerIdAndIsDefaultAndIsAvailable(buyerAddress.getBuyerId(), true, true);
            if (Objects.nonNull(defaultAddress) && !Objects.equals(defaultAddress.getId(), buyerAddress.getId())) {
                //修改的不是默认则把之前的默认清除，否则不做操作
                defaultAddress.setIsDefault(false);
                buyerAddressRepository.save(defaultAddress);
            }
        } else {
            buyerAddress.setIsDefault(false);
        }
        if (StringUtils.isNotBlank(buyerAddress.getPostCode())) {
            buyerAddress.setPostCode("0000");
        }
        return buyerAddressRepository.save(buyerAddress);
    }

    @Override
    public Page<BuyerAddress> findAll(Specification<BuyerAddress> buyerAddressSpecification, PageRequest pageRequest) {
        return buyerAddressRepository.findAll(buyerAddressSpecification, pageRequest);
    }

    @Override
    public Integer countByIdAndBuyerId(Long id, Long buyerId) {
        return buyerAddressRepository.countByIdAndBuyerIdAndIsAvailable(id, buyerId, true);
    }
}