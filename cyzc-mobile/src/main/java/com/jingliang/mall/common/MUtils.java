package com.jingliang.mall.common;

import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.Turntable;
import com.jingliang.mall.entity.TurntableDetail;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BaseReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.DocumentHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-19 15:19
 */
@Slf4j
public class MUtils extends BaseMallUtils {

    /**
     * 添加时间和用户信息
     */
    public static void addDateAndBuyer(BaseReq baseReq, Buyer buyer) {
        Date date = new Date();
        if (Objects.isNull(baseReq.getId())) {
            baseReq.setBuyerId(buyer.getId());
            baseReq.setCreateTime(date);
            baseReq.setCreateUserId(buyer.getId());
            baseReq.setCreateUserName(buyer.getUserName());
        }
        baseReq.setBuyerId(buyer.getId());
        baseReq.setUpdateTime(date);
        baseReq.setUpdateUserId(buyer.getId());
        baseReq.setUpdateUserName(buyer.getUserName());
        baseReq.setIsAvailable(true);
    }
    /**
     * 获取真实Ip地址
     *
     * @param request 请求
     * @return 返回得到的真实Ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return 验证通过返回<code>true</code>,失败<code>false</code>
     */
    public static Boolean phoneCheck(String phone) {
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";
        // 验证手机号
        Pattern p = Pattern.compile(s2);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 验证邮箱
     *
     * @param phone 邮箱
     * @return 验证通过返回<code>true</code>,失败<code>false</code>
     */
    public static Boolean mailCheck(String phone) {
        String s2 = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        // 验证邮箱
        Pattern p = Pattern.compile(s2);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 获取MultipartFile的文件格式
     */
    public static String getMultipartFileType(MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
    }
}
