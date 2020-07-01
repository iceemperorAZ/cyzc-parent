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
 * 员工表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:00:28
 */
@ApiModel(name = "UserResp", description = "员工表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class UserResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 员工编号
     */
    @ApiProperty(description = "员工编号")
    private String userNo;

    /**
     * 登录名
     */
    @ApiProperty(description = "登录名")
    private String loginName;

    /**
     * 用户名
     */
    @ApiProperty(description = "用户名")
    private String userName;

    /**
     * 毕业院校
     */
    @ApiProperty(description = "毕业院校")
    private String university;

    /**
     * 邮箱
     */
    @ApiProperty(description = "邮箱")
    private String mail;

    /**
     * 电话
     */
    @ApiProperty(description = "电话")
    private String phone;

    /**
     * 微信
     */
    @ApiProperty(description = "微信")
    private String wechat;

    /**
     * 职位
     */
    @ApiProperty(description = "职位")
    private String position;

    /**
     * 级别 9999:代表超级管理员
     */
    @ApiProperty(description = "级别 9999:代表超级管理员")
    private Integer level;

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
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 销售绑定的会员Id
     */
    @ApiProperty(description = "销售绑定的会员Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long buyerId;

    /**
     * 提成比率
     */
    @ApiProperty(description = "提成比率")
    private Integer ratio;

    /**
     * 区域经理Id
     */
    @ApiProperty(description = "区域经理Id")
    private Long managerId;

    /**
     * 负责区域
     */
    @ApiProperty(description = "负责区域")
    private String region;

    /**
     * 分组编码（-1：未分配）
     */
    @ApiProperty(description = "分组编码（-1：未分配）")
    private String groupNo;
}