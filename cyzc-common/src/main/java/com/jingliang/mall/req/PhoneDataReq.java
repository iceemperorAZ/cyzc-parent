package com.jingliang.mall.req;

import com.citrsw.annatation.ApiProperty;
import lombok.Data;

/**
 * 写点注释
 *
 * @author Zhenfeng Li
 * @date 2020-01-15 11:22:19
 */
@Data
public class PhoneDataReq {
    @ApiProperty(description = "手机号加密算法的初始向量")
    private String iv;
    @ApiProperty(description = "手机号包括敏感数据在内的完整用户信息的加密数据")
    private String encryptedData;
}
