package com.jingliang.mall.common;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.req.BaseReq;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 工具类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-19 15:19
 */
@Slf4j
public class MallUtils extends BaseMallUtils {


    /**
     * 抽奖算法
     *
     * @param map
     * @return
     */
    public static TurntableDetail weightRandom(Map<Long, TurntableDetail> map) {
        Set<Long> keySet = map.keySet();
        List<Long> weights = new ArrayList<>();
        for (Long weightKey : keySet) {
            TurntableDetail weight = map.get(weightKey);
            //奖品数量为0的不参与抽奖
            if (weight.getPrizeNum() > 0) {
                for (int i = 0; i <= weight.getProbability(); i++) {
                    weights.add(weightKey);
                }
            }
        }
        int idx = new Random().nextInt(weights.size());
        return map.get(weights.get(idx));
    }
}
