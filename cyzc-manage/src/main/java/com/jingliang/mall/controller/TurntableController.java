package com.jingliang.mall.controller;

import com.jingliang.mall.common.Base64Image;
import com.jingliang.mall.common.BeanMapper;
import com.jingliang.mall.common.MallUtils;
import com.jingliang.mall.common.Result;
import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.TurntableReq;
import com.jingliang.mall.resp.TurntableResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.service.TurntableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 转盘Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-12 17:34:10
 */
@Api(tags = "转盘")
@RestController
@RequestMapping(value = "/back/turntable")
@Slf4j
public class TurntableController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;

    private final TurntableService turntableService;
    private final FastdfsService fastdfsService;

    public TurntableController(TurntableService turntableService, FastdfsService fastdfsService) {
        this.turntableService = turntableService;
        this.fastdfsService = fastdfsService;
    }

    /**
     * 保存/修改
     */
    @PostMapping("/save")
    @ApiOperation("保存/修改")
    public Result<TurntableResp> save(@RequestBody TurntableReq turntableReq, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(turntableReq, user);
        //保存或修改转盘图片
        if (StringUtils.isNotBlank(turntableReq.getImgBase64())) {
            if (turntableReq.getId() != null) {
                Turntable turntable = turntableService.findById(turntableReq.getId());
                //有就把之前的删除了
                if (turntable != null && StringUtils.isNotBlank(turntable.getImg())) {
                    fastdfsService.deleteFile(turntable.getImg());
                }
            }
            Base64Image base64Image = Base64Image.build(turntableReq.getImgBase64());
            assert base64Image != null;
            turntableReq.setImg(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        MallUtils.addDateAndUser(turntableReq, user);
        return Result.buildSaveOk(BeanMapper.map(turntableService.save(BeanMapper.map(turntableReq, Turntable.class)), TurntableResp.class));
    }

    /**
     * 全部转盘信息
     */
    @GetMapping("/all")
    @ApiOperation("全部转盘信息")
    public Result<List<TurntableResp>> all() {
        return Result.buildQueryOk(BeanMapper.mapList(turntableService.findAll(), TurntableResp.class));
    }

    /**
     * '删除
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public Result<Boolean> delete(@PathVariable Long id, @ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(sessionUser);
        turntableService.delete(id, user.getId());
        return Result.buildDeleteOk(true);
    }

}