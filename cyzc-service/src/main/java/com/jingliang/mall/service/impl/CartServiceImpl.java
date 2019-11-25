package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Cart;
import com.jingliang.mall.repository.CartRepository;
import com.jingliang.mall.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 用户购物车ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 11:25:09
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Cart save(Cart cart) {
        Integer productNum = cart.getProductNum();
        Cart oldCart = cartRepository.findFirstByBuyerIdAndIsAvailableAndProductId(cart.getBuyerId(), true, cart.getProductId());
        //添加过的产品就只更新数量
        if (Objects.nonNull(oldCart)) {
            oldCart.setProductNum(oldCart.getProductNum() + productNum);
            cart = oldCart;
            if (cart.getProductNum() < 1) {
                cart.setIsAvailable(false);
            }
        }
        return cartRepository.save(cart);
    }

    @Override
    public Page<Cart> findAll(Specification<Cart> cartSpecification, PageRequest pageRequest) {
        return cartRepository.findAll(cartSpecification, pageRequest);
    }

    @Override
    public void emptyCart(Long buyerId) {
        List<Cart> carts = cartRepository.findAllByBuyerIdAndIsAvailable(buyerId, true);
        carts.forEach(cart -> {
            cart.setIsAvailable(false);
        });
        cartRepository.saveAll(carts);
    }

    @Override
    public void emptyCartItem(Long buyerId, List<Long> productIds) {
        List<Cart> carts = cartRepository.findAllByBuyerIdAndIsAvailableAndProductIdIsIn(buyerId, true, productIds);
        carts.forEach(cart -> {
            cart.setIsAvailable(false);
        });
        cartRepository.saveAll(carts);
    }
}