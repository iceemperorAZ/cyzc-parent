package com.jingliang.mall.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 员工表
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-25 14:00:28
 */
@Entity
@Table(name = "tb_user")
@Data
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
    @Id
    private Long id;

    /**
     * 员工编号
     */
    @Column(name = "user_no")
    private String userNo;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 毕业院校
     */
    @Column(name = "university")
    private String university;

    /**
     * 邮箱
     */
    @Column(name = "mail")
    private String mail;

    /**
     * 电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 微信
     */
    @Column(name = "wechat")
    private String wechat;

    /**
     * 职位
     */
    @Column(name = "position")
    private String position;

    /**
     * 级别 9999:代表超级管理员  100：销售，200:领导
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 是否为初始密码 0：否，1：是
     */
    @Column(name = "is_init_password")
    private Boolean isInitPassword;

    /**
     * 是否可用
     */
    @Column(name = "is_available")
    private Boolean isAvailable;

    /**
     * 创建人
     */
    @Column(name = "create_user_name")
    private String createUserName;

    /**
     * 创建人Id
     */
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    @Column(name = "update_user_name")
    private String updateUserName;

    /**
     * 修改人Id
     */
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;


    /**
     * 销售绑定的会员Id
     */
    @Column(name = "buyer_id")
    private Long buyerId;

    /**
     * 提成比率
     */
    @Column(name = "ratio")
    private Integer ratio;

    /**
     * 区域经理Id
     */
    @Column(name = "manager_id")
    private Long managerId;

    public Long getManagerId() {
        return managerId == null ? -1 : managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId == null ? -1 : managerId;
    }

    /**
     * 负责区域
     */
    @Column(name = "region")
    private String region;

    /**
     * 分组编码（-1：未分配）
     */
    @Column(name = "group_no")
    private String groupNo;

    public String getRegion() {
        return StringUtils.isBlank(region) ? "-" : region;
    }

    @Transient
    private String token;
}