package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.citrsw.annatation.ApiModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jingliang.mall.req.BaseReq;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 转盘
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-03-17 10:04:21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(name = "TurntableReq", description = "转盘")
@Data
public class TurntableReq extends BaseReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	private Long id;

	/**
	 * 转盘图片
	 */
	@ApiProperty(description = "转盘图片")
	private String img;

	/**
	 * 转盘图片
	 */
	@ApiProperty(description = "转盘图片")
	private String imgBase64;

	/**
	 * 描述
	 */
	@ApiProperty(description = "描述")
	private String remark;

	/**
	 * 转盘单次抽取需要的金币
	 */
	@ApiProperty(description = "转盘单次抽取需要的金币")
	private Integer gold;

	/**
	 * 更新时间
	 */
	@ApiProperty(description = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 更新人Id
	 */
	@ApiProperty(description = "更新人Id")
	private Long updateUserId;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}