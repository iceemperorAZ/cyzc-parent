package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-11-03 19:58:43
 */
@ApiModel(value = "MenuReq", description = "资源表")
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;


    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    /**
     * 主键Id-开始
     */
    @ApiModelProperty(value = "主键Id-开始")
    private Long idStart;

    /**
     * 主键Id-结束
     */
    @ApiModelProperty(value = "主键Id-结束")
    private Long idEnd;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    private String url;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    /**
     * 父菜单id
     */
    @ApiModelProperty(value = "父菜单id")
    private Long parentId;

    /**
     * 父菜单id-开始
     */
    @ApiModelProperty(value = "父菜单id-开始")
    private Long parentIdStart;

    /**
     * 父菜单id-结束
     */
    @ApiModelProperty(value = "父菜单id-结束")
    private Long parentIdEnd;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 路由(前端自己匹配用)
     */
    @ApiModelProperty(value = "路由(前端自己匹配用)")
    private String urlPre;

    /**
     * 是否可用
     */
    @ApiModelProperty(value = "是否可用")
    private Boolean isAvailable;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUserName;

    /**
     * 创建人Id
     */
    @ApiModelProperty(value = "创建人Id")
    private Long createUserId;

    /**
     * 创建人Id-开始
     */
    @ApiModelProperty(value = "创建人Id-开始")
    private Long createUserIdStart;

    /**
     * 创建人Id-结束
     */
    @ApiModelProperty(value = "创建人Id-结束")
    private Long createUserIdEnd;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建时间-开始
     */
    @ApiModelProperty(value = "创建时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    @ApiModelProperty(value = "创建时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiModelProperty(value = "修改人Id")
    private Long updateUserId;

    /**
     * 修改人Id-开始
     */
    @ApiModelProperty(value = "修改人Id-开始")
    private Long updateUserIdStart;

    /**
     * 修改人Id-结束
     */
    @ApiModelProperty(value = "修改人Id-结束")
    private Long updateUserIdEnd;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改时间-开始
     */
    @ApiModelProperty(value = "修改时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeStart;

    /**
     * 修改时间-结束
     */
    @ApiModelProperty(value = "修改时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeEnd;

}