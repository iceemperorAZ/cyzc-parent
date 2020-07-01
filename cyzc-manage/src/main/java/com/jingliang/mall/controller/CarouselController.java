package com.jingliang.mall.controller;

import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Carousel;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.CarouselReq;
import com.jingliang.mall.resp.CarouselResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.service.CarouselService;
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
import java.util.Objects;

/**
 * 轮播图配置Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:00:28
 */
@RestController
@Slf4j
@RequestMapping(value = "/back/carousel")
@Api(description = "轮播图配置")
public class CarouselController {
    /**
     * session用户Key
     */
    @Value("${session.user.key}")
    private String sessionUser;
    private final CarouselService carouselService;
    private final FastdfsService fastdfsService;

    public CarouselController(CarouselService carouselService, FastdfsService fastdfsService) {
        this.carouselService = carouselService;
        this.fastdfsService = fastdfsService;
    }

    /**
     * 保存/修改轮播图配置
     */
    @ApiOperation(description = "保存/修改轮播图配置")
    @PostMapping("/save")
    public Result<CarouselResp> save(@RequestBody CarouselReq carouselReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", carouselReq);
        if (StringUtils.isBlank(carouselReq.getImg()) || Objects.isNull(carouselReq.getType()) || Objects.isNull(carouselReq.getCarouselOrder())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        Base64Image base64Image = Base64Image.build(carouselReq.getImg());
        if (Objects.isNull(base64Image)) {
            log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
            return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
        }
        if (Objects.nonNull(carouselReq.getId())) {
            //如果是修改则删除原来的图片
            Carousel carousel = carouselService.findById(carouselReq.getId());
            if (!fastdfsService.deleteFile(carousel.getImgUri())) {
                log.error("图片删除失败：{}", carousel.getImgUri());
            }
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(carouselReq, user);
        Carousel carousel = BeanMapper.map(carouselReq, Carousel.class);
        assert carousel != null;
        carousel.setImgUri(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        CarouselResp carouselResp = BeanMapper.map(carouselService.save(carousel), CarouselResp.class);
        log.debug("返回结果：{}", carouselResp);
        return Result.buildSaveOk(carouselResp);
    }

    /**
     * 分页查询全部轮播图配置
     */
    @ApiOperation(description = "分页查询全部轮播图配置")
    @GetMapping("/page/all")
    public Result<MallPage<CarouselResp>> pageAllCoupon(CarouselReq carouselReq) {
        log.debug("请求参数：{}", carouselReq);
        PageRequest pageRequest = PageRequest.of(carouselReq.getPage(), carouselReq.getPageSize());
        if (StringUtils.isNotBlank(carouselReq.getClause())) {
            pageRequest = PageRequest.of(carouselReq.getPage(), carouselReq.getPageSize(), Sort.by(MallUtils.separateOrder(carouselReq.getClause())));
        }
        Specification<Carousel> carouselSpecification = (Specification<Carousel>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            predicateList.add(cb.greaterThan(root.get("type"), 0));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("carouselOrder")));
            return query.getRestriction();
        };
        Page<Carousel> carouselPage = carouselService.findAll(carouselSpecification, pageRequest);
        MallPage<CarouselResp> carouselRespMallPage = MallUtils.toMallPage(carouselPage, CarouselResp.class);
        log.debug("返回结果：{}", carouselRespMallPage);
        return Result.buildQueryOk(carouselRespMallPage);
    }

    /**
     * 删除轮播图配置
     */
    @ApiOperation(description = "删除轮播图配置")
    @PostMapping("/delete")
    public Result<CarouselResp> delete(@RequestBody CarouselReq carouselReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", carouselReq);
        User user = (User) session.getAttribute(sessionUser);
        Carousel carousel = carouselService.findById(carouselReq.getId());
        if (Objects.isNull(carousel)) {
            return Result.buildDeleteOk(null);
        }
        if (StringUtils.isNotBlank(carousel.getImgUri())) {
            //如果是修改则删除原来的图片
            if (!fastdfsService.deleteFile(carousel.getImgUri())) {
                log.error("图片删除失败：{}", carousel.getImgUri());
            }
        }
        MallUtils.addDateAndUser(carouselReq, user);
        carouselReq.setIsAvailable(false);
        carousel = BeanMapper.map(carouselReq, Carousel.class);
        CarouselResp carouselResp = BeanMapper.map(carouselService.save(carousel), CarouselResp.class);
        log.debug("返回结果：{}", carouselResp);
        return Result.buildDeleteOk(carouselResp);
    }

    /**
     * 保存/修改首页图配置
     */
    @ApiOperation(description = "保存/修改首页图配置")
    @PostMapping("/index/img/save")
    public Result<CarouselResp> indexImg(@RequestBody CarouselReq carouselReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", carouselReq);
        if (StringUtils.isBlank(carouselReq.getImg())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        Base64Image base64Image = Base64Image.build(carouselReq.getImg());
        if (Objects.isNull(base64Image)) {
            log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
            return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
        }
        if (Objects.nonNull(carouselReq.getId())) {
            //如果是修改则删除原来的图片
            Carousel carousel = carouselService.findById(carouselReq.getId());
            if (!fastdfsService.deleteFile(carousel.getImgUri())) {
                log.error("图片删除失败：{}", carousel.getImgUri());
            }
        } else {
            Carousel carousel = carouselService.findByType(-100);
            if (Objects.nonNull(carousel)) {
                return Result.build(Msg.DATA_FAIL, Msg.TEXT_DATA_REPEAT_FAIL);
            }
        }
        User user = (User) session.getAttribute(sessionUser);
        MallUtils.addDateAndUser(carouselReq, user);
        carouselReq.setCarouselOrder(-100);
        carouselReq.setType(-100);
        Carousel carousel = BeanMapper.map(carouselReq, Carousel.class);
        assert carousel != null;
        carousel.setImgUri(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        CarouselResp carouselResp = BeanMapper.map(carouselService.save(carousel), CarouselResp.class);
        log.debug("返回结果：{}", carouselResp);
        return Result.buildSaveOk(carouselResp);
    }

    /**
     * 查询首页图配置
     */
    @ApiOperation(description = "查询首页图配置")
    @GetMapping("/index/img")
    public Result<CarouselResp> indexImg() {
        return Result.buildQueryOk(BeanMapper.map(carouselService.findByType(-100), CarouselResp.class));
    }
}