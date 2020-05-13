package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author lmd
 * @date 2020/4/22
 * @company
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/map")
public class MapController {
    @Value("${session.user.key}")
    private String sessionUser;
    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 获取excel表格并读取内容，返回经纬度
     *
     * @param multipartFile
     * @return
     */
    @PostMapping("/getMap")
    public Result<List<Map<String, Object>>> getMap(@RequestParam("excel") MultipartFile multipartFile) {
        return Result.buildOk(mapService.getlngAndLat(multipartFile));
    }

    /**
     * 获取所有商户的地址
     *
     * @param buyerReq
     * @return
     */
    @GetMapping("/buyerAddress")
    public Result<List<Map<String, Object>>> buyerAddress(BuyerReq buyerReq) {
        Specification<Buyer> buyerSpecification = (Specification<Buyer>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (buyerReq.getCreateTimeStart() != null && buyerReq.getCreateTimeEnd() != null) {
                predicateList.add(cb.between(root.get("createTime"), buyerReq.getCreateTimeStart(), buyerReq.getCreateTimeEnd()));
            }
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.isNotNull(root.get("saleUserId")));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("createTime")));
            return query.getRestriction();
        };
        return Result.buildQueryOk(mapService.findAlldefaultBuyerAddress(buyerSpecification));
    }

    /**
     * 获取所有商户地址，解析成经纬度并存入数据库
     *
     * @return
     */
    @GetMapping("/saveAllMap")
    public Result<Boolean> saveAllMap() {
        return Result.buildQueryOk(mapService.getAllMapToSave());
    }

    /**
     * 根据组编号查询商户默认地址
     *
     * @param groupNo
     * @return
     */
    @GetMapping("/mapTogroupNo")
    public Result<List<Map<String, Object>>> getMapBygroupNo(@RequestParam("groupNo") String groupNo) {
        return Result.buildOk(mapService.findAddressByGroupNo(groupNo.replaceAll("0*$", "") + "%"));
    }
}
