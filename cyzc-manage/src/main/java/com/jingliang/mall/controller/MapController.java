package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.service.MapService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jingliang.mall.utils.StaticField.LATITUDE;
import static com.jingliang.mall.utils.StaticField.LONGITUDE;


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

    /**
     * 通过用户id获取经纬度记录
     *
     * @return
     */
    @GetMapping("/readMapToUserId")
    public Result<List<List<Double>>> readMap(@RequestParam("userId") Long userId) {
        log.debug("请求参数：{}", userId);
        List<List<Double>> lists = new ArrayList<>();
        for (Map<String, Object> map : mapService.readMap(userId)) {
            List<Double> list = new ArrayList<>();
            list.add((Double) map.get(LATITUDE));
            list.add((Double) map.get(LONGITUDE));
            lists.add(list);
        }
        log.debug("返回参数：{}", lists);
        return Result.buildQueryOk(lists);
    }

    /**
     * 查询销售员的最后一条记录
     *
     * @param
     * @return
     */
    @GetMapping("/readMapToEndtime")
    public Result<?> userAddressHistoryToEndTime() {
        List<Map<String, Object>> mapList = mapService.userAddressHistoryToEndTime();
        Map<String, Map<String, Object>> map = new HashMap<>(156);
        mapList.forEach(stringObjectMap -> {
            if (map.containsKey(String.valueOf(stringObjectMap.get("userId")))) {
                return;
            }
            map.put(String.valueOf(stringObjectMap.get("userId")), stringObjectMap);

        });
        mapList = new ArrayList<>();
        List<Map<String, Object>> finalTest = mapList;
        map.forEach((s, stringObjectMap) -> {
            finalTest.add(stringObjectMap);
        });
        return Result.buildOk(finalTest);
    }

    /*
     * 根据分区查询销售员定位的最后一条记录
     * */
    @GetMapping("/searchSaleByGroup")
    @ApiOperation("根据分区查询销售员定位的最后一条记录")
    public Result<?> searchSaleByGroup(String groupNo) {
        List<Map<String, Object>> mapList = mapService.searchSaleByGroup(groupNo);
        Map<String, Map<String, Object>> map = new HashMap<>(156);
        mapList.forEach(stringObjectMap -> {
            if (map.containsKey(String.valueOf(stringObjectMap.get("userId")))) {
                return;
            }
            map.put(String.valueOf(stringObjectMap.get("userId")), stringObjectMap);

        });
        mapList = new ArrayList<>();
        List<Map<String, Object>> finalTest = mapList;
        map.forEach((s, stringObjectMap) -> {
            finalTest.add(stringObjectMap);
        });
        return Result.buildOk(finalTest);
    }
}
