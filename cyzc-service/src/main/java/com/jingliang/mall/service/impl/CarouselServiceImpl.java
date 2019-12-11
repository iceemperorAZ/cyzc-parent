package com.jingliang.mall.service.impl;

import com.jingliang.mall.entity.Carousel;
import com.jingliang.mall.repository.CarouselRepository;
import com.jingliang.mall.service.CarouselService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图配置ServiceImpl
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:00:28
 */
@Service
@Slf4j
public class CarouselServiceImpl implements CarouselService {

    private final CarouselRepository carouselRepository;

    public CarouselServiceImpl(CarouselRepository carouselRepository) {
        this.carouselRepository = carouselRepository;
    }

    @Override
    public Carousel save(Carousel carousel) {
        return carouselRepository.save(carousel);
    }

    @Override
    public Page<Carousel> findAll(Specification<Carousel> carouselSpecification, PageRequest pageRequest) {
        return carouselRepository.findAll(carouselSpecification, pageRequest);
    }

    @Override
    public Carousel findById(Long id) {
        return carouselRepository.findAllByIdAndIsAvailable(id, true);
    }

    @Override
    public List<Carousel> findAll(Specification<Carousel> carouselSpecification) {
        return carouselRepository.findAll(carouselSpecification);
    }

    @Override
    public Carousel findByType(Integer type) {
        return carouselRepository.findFirstByTypeAndIsAvailable(type, true);
    }
}