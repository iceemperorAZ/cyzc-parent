package com.jingliang.mall.service;

import com.jingliang.mall.entity.UnavailableName;

import java.util.List;

/**
 * 不规范商铺名称合集Service
 *
 * @author Xiaobing Li
 * @version 1.0.0
 * @date 2020-06-11 15:45:35
 */
public interface UnavailableNameService {

    List<UnavailableName> update();

    List<UnavailableName> findAll();

    Boolean findNameCount(String name);
}