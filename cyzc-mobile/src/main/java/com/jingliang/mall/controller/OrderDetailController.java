package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Cart;
import com.jingliang.mall.entity.OrderDetail;
import com.jingliang.mall.req.OrderDetailReq;
import com.jingliang.mall.service.CartService;
import com.jingliang.mall.service.OrderDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单详情表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:10:24
 */
@Api(tags = "订单详情")
@RestController
@RequestMapping("/front/orderDetail")
@Slf4j
public class OrderDetailController {
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;

    public OrderDetailController(OrderDetailService orderDetailService, CartService cartService) {
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
    }

    /**
     * 再来一单
     */
    @ApiOperation(value = "再来一单")
    @PostMapping("/again")
    public MallResult<?> again(@RequestBody OrderDetailReq orderDetailReq, @ApiIgnore HttpSession session) {
        if (Objects.isNull(orderDetailReq.getOrderId())) {
            return MallResult.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderDetailReq.getOrderId());
        for (OrderDetail orderDetail : orderDetails) {
            if(orderDetail.getSellingPrice()==0){
                //赠品不算
                continue;
            }
            Cart cart = new Cart();
            cart.setBuyerId(buyer.getId());
            cart.setProductId(orderDetail.getProductId());
            cart.setProductNum(orderDetail.getProductNum());
            cart.setIsAvailable(true);
            cart.setCreateTime(new Date());
            cartService.save(cart);
        }
        return MallResult.buildOk();
    }
}