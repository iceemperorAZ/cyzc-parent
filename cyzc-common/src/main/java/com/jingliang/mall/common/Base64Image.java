package com.jingliang.mall.common;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import java.util.Base64;
import java.util.Objects;

/**
 * Base64图片类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-23 14:46
 */
@Data
public class Base64Image {
    /**
     * 图片数据
     */
    private byte[] bytes;

    /**
     * 图片后缀
     */
    private String extName;

    private Base64Image(byte[] bytes, String extName) {
        this.bytes = bytes;
        this.extName = extName;
    }

    public static Base64Image build(String base64) {
        byte[] bytes = base64ToByte(base64);
        String imgFormat = getImgFormat(base64);
        if (StringUtils.isBlank(imgFormat)) {
            return null;
        }
        return new Base64Image(bytes, imgFormat);
    }

    /**
     * 图片base64获取图片格式
     */
    private static String getImgFormat(String base64) {
        String[] split = base64.split(",");
        if (split.length < 2) {
            return null;
        }
        if (split[0].toLowerCase().contains("/png")) {
            return "png";
        } else if (split[0].toLowerCase().contains("/jpeg")) {
            return "jpeg";
        } else if (split[0].toLowerCase().contains("/jpg")) {
            return "jpg";
        } else if (split[0].toLowerCase().contains("/gif")) {
            return "gif";
        } else if (split[0].toLowerCase().contains("/bmp")) {
            return "bmp";
        } else if (split[0].toLowerCase().contains("/tiff")) {
            return "tiff";
        }
        return null;
    }

    /**
     * 转换图片为byte
     *
     * @param base64 base64字符串
     * @return 返回byte[]
     */
    private static byte[] base64ToByte(String base64) {
        byte[] dataByte = Base64.getDecoder().decode(base64.split(",")[1].replace("\n", "").replace("\r", ""));
        for (int i = 0; i < dataByte.length; ++i) {
            //调整异常数据
            if (dataByte[i] < 0) {
                dataByte[i] += 256;
            }
        }
        return dataByte;
    }
}
