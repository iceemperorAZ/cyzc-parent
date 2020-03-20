package com.jingliang.mall.controller;

import com.jingliang.mall.common.Base64Image;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.TurntableDetailReq;
import com.jingliang.mall.resp.TurntableDetailResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.service.TurntableDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 转盘详情Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(tags = "转盘详情")
@RestController
@RequestMapping(value = "/back/turntableDetail")
@Slf4j
public class TurntableDetailController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final TurntableDetailService turntableDetailService;
    private final FastdfsService fastdfsService;

    public TurntableDetailController(TurntableDetailService turntableDetailService, FastdfsService fastdfsService) {
        this.turntableDetailService = turntableDetailService;
        this.fastdfsService = fastdfsService;
    }

    /**
     * 保存/修改转盘项
     */
    @PostMapping("/save")
    @ApiOperation("保存/修改转盘项")
    public Result<TurntableDetailResp> save(@RequestBody TurntableDetailReq turntableDetailReq, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(turntableDetailReq, user);
        if (turntableDetailReq.getId() == null) {
            turntableDetailReq.setIsShow(false);
        }
        MallUtils.addDateAndUser(turntableDetailReq, user);
        //保存或修改转盘图片
        if (StringUtils.isNotBlank(turntableDetailReq.getImgBase64())) {
            if (turntableDetailReq.getId() != null) {
                TurntableDetail turntableDetail = turntableDetailService.findById(turntableDetailReq.getId());
                //有就把之前的删除了
                if (turntableDetail != null && StringUtils.isNotBlank(turntableDetail.getImg())) {
                    fastdfsService.deleteFile(turntableDetail.getImg());
                }
            }
            Base64Image base64Image = Base64Image.build(turntableDetailReq.getImgBase64());
            assert base64Image != null;
            turntableDetailReq.setImg(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        TurntableDetail turntableDetail = turntableDetailService.save(BeanMapper.map(turntableDetailReq, TurntableDetail.class));
        return Result.buildSaveOk(BeanMapper.map(turntableDetail, TurntableDetailResp.class));
    }

    /**
     * 根据转盘Id查询转盘项
     */
    @GetMapping("/all/turntableId")
    @ApiOperation("根据转盘Id查询转盘项")
    public Result<List<TurntableDetailResp>> findAll(Long turntableId) {
        List<TurntableDetail> turntableDetails = turntableDetailService.findAll(turntableId);
        return Result.buildQueryOk(BeanMapper.mapList(turntableDetails, TurntableDetailResp.class));
    }

    /**
     * 上下架
     */
    @PostMapping("/show")
    @ApiOperation("上下架")
    public Result<TurntableDetailResp> show(@RequestBody TurntableDetailReq turntableDetailReq, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        TurntableDetail turntableDetail = turntableDetailService.show(user, BeanMapper.map(turntableDetailReq, TurntableDetail.class));
        return Result.buildSaveOk(BeanMapper.map(turntableDetail, TurntableDetailResp.class));
    }

    /**
     * 删除
     */
    @PostMapping("/id")
    @ApiOperation("删除")
    public Result<Boolean> delete(@RequestBody Map<String, Long> map, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        turntableDetailService.delete(map.get("id"), user.getId());
        return Result.buildDeleteOk(true);
    }
}