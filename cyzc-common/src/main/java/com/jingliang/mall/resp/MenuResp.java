package com.jingliang.mall.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(name = "MenuResp", description = "资源表")
@Data
public class MenuResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    @ApiProperty(description = "主键Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 请求路径
     */
    @ApiProperty(description = "请求路径")
    private String url;

    /**
     * 菜单名称
     */
    @ApiProperty(description = "菜单名称")
    private String menuName;

    /**
     * 父菜单id
     */
    @ApiProperty(description = "父菜单id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 备注
     */
    @ApiProperty(description = "备注")
    private String remark;

    /**
     * 路由(前端自己匹配用)
     */
    @ApiProperty(description = "路由(前端自己匹配用)")
    private String urlPre;

    /**
     * 是否可用
     */
    @ApiProperty(description = "是否可用")
    private Boolean isAvailable;

    /**
     * 创建人
     */
    @ApiProperty(description = "创建人")
    private String createUserName;

    /**
     * 创建人Id
     */
    @ApiProperty(description = "创建人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiProperty(description = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiProperty(description = "修改人Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateUserId;

    /**
     * 当前角色是否拥有此资源
     */
    @ApiProperty(description = "当前角色是否拥有此资源")
    private Boolean isHave = false;

    /**
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}