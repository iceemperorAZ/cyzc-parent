package com.jingliang.mall.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */
@Data
@ApiModel(value = "MallPage", description = "分页")
public class MallPage<T> implements Serializable {
    private static final long serialVersionUID = -8270224786645662371L;
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer page;
    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Integer totalPages;
    /**
     * 总行数
     */
    @ApiModelProperty(value = "总行数")
    private Long totalNumber;
    /**
     * 是否为首页
     */
    @ApiModelProperty(value = "是否为首页")
    private Boolean first = false;
    /**
     * 是否为末页
     */
    @ApiModelProperty(value = "是否为末页")
    private Boolean last = false;
    /**
     * 数据列表
     */
    @ApiModelProperty(value = "数据列")
    private List<T> content;

    public Integer getPage() {
        return page + 1;
    }
}
