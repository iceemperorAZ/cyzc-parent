package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallPage;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.SearchHistory;
import com.jingliang.mall.req.SearchHistoryReq;
import com.jingliang.mall.resp.SearchHistoryResp;
import com.jingliang.mall.service.SearchHistoryService;
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
 * 历史搜索表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-05 12:08:29
 */
@RestController
@RequestMapping("/front/searchHistory")
@Slf4j
@Api(tags = "历史搜索")
public class SearchHistoryController {
    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    private final SearchHistoryService searchHistoryService;

    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    /**
     * 保存搜索词到历史记录
     */
    @ApiOperation(value = "保存搜索词到历史记录")
    @PostMapping("/save")
    public MallResult<SearchHistoryResp> save(@RequestBody SearchHistoryReq searchHistoryReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", searchHistoryReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        MallUtils.addDateAndBuyer(searchHistoryReq, buyer);
        SearchHistoryResp searchHistoryResp = MallBeanMapper.map(searchHistoryService.save(MallBeanMapper.map(searchHistoryReq, SearchHistory.class)), SearchHistoryResp.class);
        log.debug("返回结果：{}", searchHistoryResp);
        return MallResult.buildSaveOk(searchHistoryResp);
    }

    /**
     * 分页查询所有历史搜索
     */
    @ApiOperation(value = "分页查询所有历史搜索")
    @GetMapping("/page/all")
    public MallResult<MallPage<SearchHistoryResp>> pageAll(SearchHistoryReq searchHistoryReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", searchHistoryReq);
        PageRequest pageRequest = PageRequest.of(searchHistoryReq.getPage(), searchHistoryReq.getPageSize());
        if (StringUtils.isNotBlank(searchHistoryReq.getClause())) {
            pageRequest = PageRequest.of(searchHistoryReq.getPage(), searchHistoryReq.getPageSize(), Sort.by(MallUtils.separateOrder(searchHistoryReq.getClause())));
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Specification<SearchHistory> searchHistorySpecification = (Specification<SearchHistory>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("buyerId"), buyer.getId()));
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.desc(root.get("createTime")));
            return query.getRestriction();
        };
        Page<SearchHistory> searchHistoryPage = searchHistoryService.findAll(searchHistorySpecification, pageRequest);
        MallPage<SearchHistoryResp> searchHistoryRespMallPage = MallUtils.toMallPage(searchHistoryPage, SearchHistoryResp.class);
        log.debug("返回结果：{}", searchHistoryRespMallPage);
        return MallResult.buildQueryOk(searchHistoryRespMallPage);
    }

}