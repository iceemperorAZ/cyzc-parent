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
 * 文件记录表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-29 11:18:02
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(name = "FileRecordReq", description = "文件记录")
@Data
public class FileRecordReq extends BaseReq implements Serializable {

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
	 * 文件名
	 */
	@ApiProperty(description = "文件名")
	private String fileName;

	/**
	 * 文件类型  100:上传，200：删除，300：下载
	 */
	@ApiProperty(description = "件类型  100:上传，200：删除，300：下载")
	private Integer type;

	/**
	 * 文件uri
	 */
	@ApiProperty(description = "文件uri")
	private String fileUri;

	/**
	 * 文件大小
	 */
	@ApiProperty(description = "文件大小")
	private Long fileSize;

	/**
	 * 文件大小-开始
	 */
	@ApiProperty(description = "文件大小-开始")
	private Long fileSizeStart;

	/**
	 * 文件大小-结束
	 */
	@ApiProperty(description = "文件大小-结束")
	private Long fileSizeEnd;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
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
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTimeStart;

	/**
	 * 创建时间-结束
	 */
	@ApiProperty(description = "创建时间-结束")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

}