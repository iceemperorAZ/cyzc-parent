package com.jingliang.mall.req;

import java.util.Date;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

/**
 * 组与区域映射关系表
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-23 15:33:55
 */
@Data
public class GroupRegionReq implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 组Id
	 */
	private Long groupId;

	/**
	 * 区域Id
	 */
	private Long regionId;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	private Boolean isAvailable;

}