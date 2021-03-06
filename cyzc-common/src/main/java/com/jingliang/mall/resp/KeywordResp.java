package com.jingliang.mall.resp;

import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
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
@ApiModel(name = "keywordRsp", description = "搜索词")
public class KeywordResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索词
     */
    @ApiProperty(description = "搜索词")
    private String keyword;
}
