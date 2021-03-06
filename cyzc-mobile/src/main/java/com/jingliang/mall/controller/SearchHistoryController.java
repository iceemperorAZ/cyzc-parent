package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.SearchHistory;
import com.jingliang.mall.req.SearchHistoryReq;
import com.jingliang.mall.resp.SearchHistoryResp;
import com.jingliang.mall.service.SearchHistoryService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.ApiIgnore;

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
@Api(description = "历史搜索")
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
    @ApiOperation(description = "保存搜索词到历史记录")
    @PostMapping("/save")
    public Result<SearchHistoryResp> save(@RequestBody SearchHistoryReq searchHistoryReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", searchHistoryReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        MUtils.addDateAndBuyer(searchHistoryReq, buyer);
        SearchHistoryResp searchHistoryResp = BeanMapper.map(searchHistoryService.save(BeanMapper.map(searchHistoryReq, SearchHistory.class)), SearchHistoryResp.class);
        log.debug("返回结果：{}", searchHistoryResp);
        return Result.buildSaveOk(searchHistoryResp);
    }

    /**
     * 分页查询所有历史搜索
     */
    @ApiOperation(description = "分页查询所有历史搜索")
    @GetMapping("/page/all")
    public Result<MallPage<SearchHistoryResp>> pageAll(SearchHistoryReq searchHistoryReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", searchHistoryReq);
        PageRequest pageRequest = PageRequest.of(searchHistoryReq.getPage(), searchHistoryReq.getPageSize());
        if (StringUtils.isNotBlank(searchHistoryReq.getClause())) {
            pageRequest = PageRequest.of(searchHistoryReq.getPage(), searchHistoryReq.getPageSize(), Sort.by(MUtils.separateOrder(searchHistoryReq.getClause())));
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
        MallPage<SearchHistoryResp> searchHistoryRespMallPage = MUtils.toMallPage(searchHistoryPage, SearchHistoryResp.class);
        log.debug("返回结果：{}", searchHistoryRespMallPage);
        return Result.buildQueryOk(searchHistoryRespMallPage);
    }

}