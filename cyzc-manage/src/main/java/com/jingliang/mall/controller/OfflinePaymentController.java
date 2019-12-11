package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.OfflinePayment;
import com.jingliang.mall.entity.Order;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.OfflinePaymentReq;
import com.jingliang.mall.resp.OfflinePaymentResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.service.OfflinePaymentService;
import com.jingliang.mall.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 线下支付Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-06 15:30:50
 */
@RestController
@RequestMapping(value = "/back/offlinePayment")
@Slf4j
@Api(tags = "线下支付")
public class OfflinePaymentController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final OfflinePaymentService offlinePaymentService;
    private final OrderService orderService;
    private final FastdfsService fastdfsService;

    public OfflinePaymentController(OfflinePaymentService offlinePaymentService, OrderService orderService, FastdfsService fastdfsService) {
        this.offlinePaymentService = offlinePaymentService;
        this.orderService = orderService;
        this.fastdfsService = fastdfsService;
    }

    /**
     * 保存/更新支付凭证
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存/更新支付凭证")
    public MallResult<OfflinePaymentResp> save(@RequestBody OfflinePaymentReq offlinePaymentReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", offlinePaymentReq);
        if (Objects.isNull(offlinePaymentReq.getOrderId()) || StringUtils.isBlank(offlinePaymentReq.getRemark()) || Objects.isNull(offlinePaymentReq.getImgBase64s())) {
            return MallResult.buildParamFail();
        }
        //确认订单是否存在
        Order order = orderService.findById(offlinePaymentReq.getOrderId());
        if (Objects.isNull(order)) {
            return MallResult.build(MallConstant.DATA_FAIL, MallConstant.TEXT_ORDER_DATA_FAIL);
        }
        if (Objects.nonNull(offlinePaymentService.findByOrderId(offlinePaymentReq.getOrderId()))) {
            return MallResult.build(MallConstant.DATA_FAIL, MallConstant.TEXT_DATA_REPEAT_FAIL);
        }
        offlinePaymentReq.setOrderNo(order.getOrderNo());
        if (Objects.nonNull(offlinePaymentReq.getId())) {
            //将之前所有的图片删除重新上传一份新的
            OfflinePayment offlinePayment = offlinePaymentService.findAllById(offlinePaymentReq.getId());
            String productImgUris = offlinePayment.getUrls();
            if (StringUtils.isNotBlank(productImgUris)) {
                String[] imgUris = productImgUris.split(";");
                for (String imgUri : imgUris) {
                    if (!fastdfsService.deleteFile(imgUri)) {
                        log.error("图片删除失败：{}", imgUri);
                    }
                }
            }
        }
        List<Base64Image> base64Images = new ArrayList<>();
        for (int i = 0; i < (Math.min(offlinePaymentReq.getImgBase64s().size(), 5)); i++) {
            String imgBase = offlinePaymentReq.getImgBase64s().get(i);
            Base64Image base64Image = Base64Image.build(imgBase);
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", MallConstant.TEXT_IMAGE_FAIL);
                return MallResult.build(MallConstant.IMAGE_FAIL, MallConstant.TEXT_IMAGE_FAIL);
            }
            base64Images.add(base64Image);
        }
        StringBuilder builder = new StringBuilder();
        for (Base64Image base64Image : base64Images) {
            builder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(offlinePaymentReq, user);
        OfflinePayment offlinePayment = MallBeanMapper.map(offlinePaymentReq, OfflinePayment.class);
        assert offlinePayment != null;
        offlinePayment.setUrls(builder.substring(1));
        OfflinePaymentResp offlinePaymentResp = MallBeanMapper.map(offlinePaymentService.save(offlinePayment), OfflinePaymentResp.class);
        log.debug("返回参数：{}", offlinePaymentResp);
        return MallResult.buildSaveOk(offlinePaymentResp);
    }

    /**
     * 根据订单Id查询支付凭证
     */
    @GetMapping("/findByOrderId")
    @ApiOperation(value = "根据订单Id查询支付凭证")
    public MallResult<OfflinePaymentResp> save(Long orderId) {
        log.debug("请求参数：orderId= {}", orderId);
        if (Objects.isNull(orderId)) {
            return MallResult.buildParamFail();
        }
        OfflinePayment offlinePayment = offlinePaymentService.findByOrderId(orderId);
        OfflinePaymentResp offlinePaymentResp = MallBeanMapper.map(offlinePayment, OfflinePaymentResp.class);
        log.debug("返回参数：{}", offlinePaymentResp);
        return MallResult.buildSaveOk(offlinePaymentResp);
    }

}