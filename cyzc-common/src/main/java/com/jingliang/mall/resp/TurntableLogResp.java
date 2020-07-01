package com.jingliang.mall.resp;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.citrsw.annatation.Api;
import org.springframework.format.annotation.DateTimeFormat;
import com.citrsw.annatation.ApiProperty;
import java.io.Serializable;

/**
 * 转盘日志
 * 
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2020-04-01 09:39:37
 */
@Api(description = "转盘日志")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TurntableLogResp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@ApiProperty(description = "主键Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 商户Id
	 */
	@ApiProperty(description = "商户Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long buyerId;

	/**
	 * 商户
	 */
	@ApiProperty(description = "商户")
	private BuyerResp buyer;

	/**
	 * 消息内容
	 */
	@ApiProperty(description = "消息内容")
	private String msg;

	/**
	 * 类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)
	 */
	@ApiProperty(description = "类型(100:谢谢惠顾,200:金币，300:返币次数，400：商品[为商品时需配置奖品Id],)")
	private Integer type;

	/**
	 * 奖品Id
	 */
	@ApiProperty(description = "奖品Id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long prizeId;

	/**
	 * 奖品名称
	 */
	@ApiProperty(description = "奖品名称")
	private String prizeName;

	/**
	 * 奖品数量
	 */
	@ApiProperty(description = "奖品数量")
	private Integer prizeNum;

	/**
	 * 消耗金币数量
	 */
	@ApiProperty(description = "消耗金币数量")
	private Integer gold;

	/**
	 * 创建时间
	 */
	@ApiProperty(description = "创建时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 是否可用 0：否，1：是
	 */
	@ApiProperty(description = "是否可用 0：否，1：是")
	private Boolean isAvailable;

}