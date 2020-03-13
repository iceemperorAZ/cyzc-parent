package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Config;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.ConfigReq;
import com.jingliang.mall.resp.ConfigResp;
import com.jingliang.mall.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置文件Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-12-03 15:59:18
 */
@Api(tags = "配置文件")
@RequestMapping(value = "/back/config")
@RestController
@Slf4j
public class ConfigController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * 修改配置信息
     */
    @ApiOperation(value = "修改配置信息")
    @PostMapping("/update")
    public Result<ConfigResp> update(@RequestBody ConfigReq configReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", configReq);
        if (StringUtils.isBlank(configReq.getName()) || StringUtils.isBlank(configReq.getConfigValues())) {
            return Result.buildParamFail();
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(configReq, user);
        ConfigResp configResp = BeanMapper.map(configService.save(BeanMapper.map(configReq, Config.class)), ConfigResp.class);
        log.debug("返回参数：{}", configResp);
        return Result.buildSaveOk(configResp);
    }

    /**
     * 分页查询全部配置
     */
    @GetMapping("/page/all")
    @ApiOperation(value = "分页查询全部配置")
    public Result<MallPage<ConfigResp>> pageAllProduct(ConfigReq configReq) {
        log.debug("请求参数：{}", configReq);
        PageRequest pageRequest = PageRequest.of(configReq.getPage(), configReq.getPageSize());
        if (StringUtils.isNotBlank(configReq.getClause())) {
            pageRequest = PageRequest.of(configReq.getPage(), configReq.getPageSize(), Sort.by(MallUtils.separateOrder(configReq.getClause())));
        }
        Specification<Config> configSpecification = (Specification<Config>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(configReq.getName())) {
                predicateList.add(cb.like(root.get("menuName"), "%" + configReq.getName() + "%"));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            return query.getRestriction();
        };
        Page<Config> configPage = configService.findAll(configSpecification, pageRequest);
        MallPage<ConfigResp> configRespMallPage = MallUtils.toMallPage(configPage, ConfigResp.class);
        log.debug("返回结果：{}", configRespMallPage);
        return Result.buildQueryOk(configRespMallPage);
    }
}