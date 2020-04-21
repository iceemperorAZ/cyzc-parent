package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.ContactRecord;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.ContactRecordReq;
import com.jingliang.mall.resp.ContactRecordResp;
import com.jingliang.mall.service.ContactRecordService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 商户联系记录Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-01-15 10:17:00
 */
@Api(tags = "商户联系记录")
@RestController
@Slf4j
@RequestMapping(value = "/back/contactRecord")
public class ContactRecordController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final ContactRecordService contactRecordService;

    public ContactRecordController(ContactRecordService contactRecordService) {
        this.contactRecordService = contactRecordService;
    }


    @PostMapping("/save")
    public Result<ContactRecordResp> save(ContactRecordReq contactRecordReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", contactRecordReq);
        if (StringUtils.isBlank(contactRecordReq.getRemark()) || contactRecordReq.getResult() == null || contactRecordReq.getBuyerId() == null) {
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(contactRecordReq, user);
        contactRecordReq.setCreateTime(new Date());
        ContactRecord contactRecord = contactRecordService.save(BeanMapper.map(contactRecordReq, ContactRecord.class));
        ContactRecordResp contactRecordResp = BeanMapper.map(contactRecord, ContactRecordResp.class);
        log.debug("返回参数：{}", contactRecordResp);
        return Result.buildSaveOk(contactRecordResp);
    }

    @GetMapping("/find/buyer_id")
    public Result<List<ContactRecordResp>> find(Long buyerId) {
        log.debug("请求参数：{}", buyerId);
        if (buyerId == null) {
            return Result.buildParamFail();
        }
        List<ContactRecord> contactRecords = contactRecordService.findByBuyerId(buyerId);
        List<ContactRecordResp> contactRecordResps = BeanMapper.mapList(contactRecords, ContactRecordResp.class);
        log.debug("返回参数：{}", contactRecordResps);
        return Result.buildSaveOk(contactRecordResps);
    }
}