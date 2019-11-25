package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 历史搜索表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-10-05 12:21:02
 */
@Entity
@Table(name = "tb_search_history")
@Data
public class SearchHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "com.jingliang.mall.common.IdGenerator")
	@Id
	private Long id;

	/**
	 * 搜索词
	 */
	@Column(name = "keyword")
	private String keyword;

	/**
	 * 是否可用 0：否，1：是
	 */
	@Column(name = "is_available")
	private Boolean isAvailable;

	/**
	 * 用户Id（创建人Id）
	 */
	@Column(name = "buyer_Id")
	private Long buyerId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}