package com.jingliang.mall.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件记录表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 11:18:02
 */
@Table(name = "tb_file_record")
@Entity
@Data
public class FileRecord implements Serializable {

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
	 * 文件名
	 */
	@Column(name = "file_name")
	private String fileName;

	/**
	 * 文件类型  100:上传，200：删除，300：下载
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 文件uri
	 */
	@Column(name = "file_uri")
	private String fileUri;

	/**
	 * 文件大小
	 */
	@Column(name = "file_size")
	private Long fileSize;

	/**
	 * 是否可用 0：否，1：是
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
	@Column(name = "create_user_Id")
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

}