package com.jingliang.mall.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.ApiModel;
import com.citrsw.annatation.ApiProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@ApiModel(name = "UserReq", description = "员工表")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserReq extends BaseReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiProperty(description = "主键")
    private Long id;

    /**
     * 主键-开始
     */
    @ApiProperty(description = "主键-开始")
    private Long idStart;

    /**
     * 主键-结束
     */
    @ApiProperty(description = "主键-结束")
    private Long idEnd;

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
     * 查询用户名
     */
    @ApiProperty(description = "查询用户名")
    private String searchName;

    /**
     * 员工编号
     */
    @ApiProperty(description = "员工编号")
    private String userNo;

    /**
     * 密码
     */
    @ApiProperty(description = "密码")
    private String password;

    /**
     * 旧密码
     */
    @ApiProperty(description = "旧密码")
    private String oldPassword;

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
     * 级别 9999:代表超级管理员-开始
     */
    @ApiProperty(description = "级别 9999:代表超级管理员-开始")
    private Integer levelStart;

    /**
     * 级别 9999:代表超级管理员-结束
     */
    @ApiProperty(description = "级别 9999:代表超级管理员-结束")
    private Integer levelEnd;

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
    private Long createUserId;

    /**
     * 创建人Id-开始
     */
    @ApiProperty(description = "创建人Id-开始")
    private Long createUserIdStart;

    /**
     * 创建人Id-结束
     */
    @ApiProperty(description = "创建人Id-结束")
    private Long createUserIdEnd;

    /**
     * 创建时间
     */
    @ApiProperty(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建时间-开始
     */
    @ApiProperty(description = "创建时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeStart;

    /**
     * 创建时间-结束
     */
    @ApiProperty(description = "创建时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTimeEnd;

    /**
     * 修改人
     */
    @ApiProperty(description = "修改人")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @ApiProperty(description = "修改人Id")
    private Long updateUserId;

    /**
     * 修改人Id-开始
     */
    @ApiProperty(description = "修改人Id-开始")
    private Long updateUserIdStart;

    /**
     * 修改人Id-结束
     */
    @ApiProperty(description = "修改人Id-结束")
    private Long updateUserIdEnd;

    /**
     * 修改时间
     */
    @ApiProperty(description = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 修改时间-开始
     */
    @ApiProperty(description = "修改时间-开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeStart;

    /**
     * 修改时间-结束
     */
    @ApiProperty(description = "修改时间-结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTimeEnd;

    /**
     * 销售绑定的会员Id
     */
    @ApiProperty(description = "销售绑定的会员Id")
    private Long buyerId;

    /**
     * 提成比率
     */
    @ApiProperty(description = "提成比率")
    private Integer ratio;

    /**
     * 是否为初始密码
     */
    @ApiProperty(description = "是否为初始密码")
    private Boolean isInitPassword;

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