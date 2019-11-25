package com.jingliang.mall.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索词
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-05 13:56
 */
@Data
@ApiModel(value = "keywordRsp", description = "搜索词")
public class KeywordResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String keyword;
}
