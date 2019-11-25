package com.jingliang.mall.controller;

import com.jingliang.mall.common.MallBeanMapper;
import com.jingliang.mall.common.MallResult;
import com.jingliang.mall.entity.Carousel;
import com.jingliang.mall.resp.CarouselResp;
import com.jingliang.mall.service.CarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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
@Api(tags = "轮播图配置")
public class CarouselController {
    private final CarouselService carouselService;

    public CarouselController(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    /**
     * 查询全部轮播图配置
     */
    @ApiOperation(value = "查询全部轮播图配置")
    @GetMapping("/all")
    public MallResult<List<CarouselResp>> pageAllCoupon() {
        log.debug("请求参数：无");
        Specification<Carousel> carouselSpecification = (Specification<Carousel>) (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get("isAvailable"), true));
            query.where(cb.and(predicateList.toArray(new Predicate[0])));
            query.orderBy(cb.asc(root.get("carouselOrder")));
            return query.getRestriction();
        };
        List<Carousel> carousels = carouselService.findAll(carouselSpecification);
        List<CarouselResp> carouselResps = MallBeanMapper.mapList(carousels, CarouselResp.class);
        log.debug("返回结果：{}", carouselResps);
        return MallResult.buildQueryOk(carouselResps);
    }
}