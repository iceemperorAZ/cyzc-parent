package com.jingliang.mall.controller;

import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.MapDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.MapDetailReq;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.citrsw.annatation.Api;
import com.jingliang.mall.service.MapPointService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 保存地图信息Controller
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-05-29 09:19:03
 */
@RestController
@Slf4j
@Api(description = "保存地图信息")
@RequestMapping(value = "/front/mapPoint")
public class MapPointController {

    @Value("${session.user.key}")
    private String sessionUser;
    private final MapPointService mapPointService;

    public MapPointController(MapPointService mapPointService) {
        this.mapPointService = mapPointService;
    }

    @PostMapping("/saveMap")
    @ApiOperation(description = "保存地图信息到数据库")
    public Result<List<Map<String, Object>>> saveMap(@RequestParam("mapNo") String mapNo, @RequestParam("excel") MultipartFile multipartFile, HttpSession session) {
        log.debug("请求参数:{}", mapNo);
        User user = (User) session.getAttribute(sessionUser);
        if (Objects.isNull(multipartFile)) {
            return Result.build(500, "请上传文件，老铁！");
        }
        List<Map<String, Object>> address = mapPointService.saveMapAndShowMap(StringUtils.isBlank(mapNo) ? "" : mapNo, multipartFile, user.getId());
        log.debug("返回参数:{}", address);
        return Result.buildSaveOk(address);
    }

    @GetMapping("/updateMap")
    @ApiOperation(description = "更新地图点位信息")
    public Result<Object> updateMap(@RequestBody MapDetailReq mapDetailReq) {
        log.debug("请求参数:{}", mapDetailReq);
        MapDetail mapDetail = BeanMapper.map(mapDetailReq, MapDetail.class);
        Boolean flg = mapPointService.updateMap(mapDetail.getId(), mapDetail.getAddressDetail());
        if (!flg) {
            return Result.build(500, "系统异常");
        }
        log.debug("返回参数:{}", flg);
        return Result.build(200, "更新成功！");
    }

    @GetMapping("/deleteMap")
    @ApiOperation(description = "清空点位信息")
    public Result<List<MapDetail>> deleteMap(@RequestParam("mapNo") String mapNo) {
        log.debug("请求参数:{}", mapNo);
        List<MapDetail> mapDetails = mapPointService.deleteMap(mapNo);
        log.debug("返回参数:{}", mapDetails);
        return Result.buildDeleteOk(mapDetails);
    }

    @GetMapping("/searchPointByMapNo")
    @ApiOperation(description = "根据地图编号查询点位")
    public Result<MapDetail> searchPoint(Long id) {
        log.debug("请求参数:{}", id);
        MapDetail mapDetail = mapPointService.searchPointByMapNo(id);
        log.debug("返回参数:{}", mapDetail);
        return Result.buildQueryOk(mapDetail);
    }
}