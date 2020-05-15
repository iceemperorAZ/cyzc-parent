package com.jingliang.mall.controller;

import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.AddressUserHistory;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.MapReq;
import com.jingliang.mall.service.MapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 地图记录表controller
 *
 * @author lmd
 * @date 2020/5/10
 * @company 晶粮
 */
@RequestMapping("/wx/map")
@RestController
@Slf4j
public class MapController {

    @Value("${session.buyer.key}")
    private String sessionBuyer;
    @Value("${session.user.key}")
    private String sessionUser;
    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    /**
     * 保存前端传递的经纬度
     *
     * @return
     */
    @PostMapping("/saveMap")
    public Result<AddressUserHistory> addressTolngAndlat(@RequestBody MapReq mapReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", mapReq);
        User user = (User) session.getAttribute(sessionUser);
        if (Objects.isNull(mapReq.getLongitude()) || Objects.isNull(mapReq.getLatitude())) {
            return Result.buildOk();
        }
        //创建经纬度记录表
        AddressUserHistory addressUserHistory = new AddressUserHistory();
        addressUserHistory.setUserId(user.getId());
        addressUserHistory.setLongitude(mapReq.getLongitude());
        addressUserHistory.setLatitude(mapReq.getLatitude());
        addressUserHistory.setAddress(mapReq.getAddress());
        addressUserHistory.setCreateTime(new Date());
        addressUserHistory.setIsAvailable(true);
        addressUserHistory.setLevel(user.getLevel());
        //储存经纬度记录表
        mapService.saveMap(addressUserHistory);
        return Result.buildOk();
    }


}
