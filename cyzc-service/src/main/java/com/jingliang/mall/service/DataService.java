package com.jingliang.mall.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @date 2020-03-26 13:30:41
 */
public interface DataService {

    public List<Map<String, Object>> newBuyerCount(Date date);
}
