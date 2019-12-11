package com.jingliang.mall.common;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BaseReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-19 15:19
 */
@Slf4j
public class MallUtils extends BaseMallUtils {

    /**
     * 添加时间和员工信息
     */
    public static void addDateAndUser(BaseReq baseReq, User user) {
        Date date = new Date();
        if (Objects.isNull(baseReq.getId())) {
            baseReq.setUserId(user.getId());
            baseReq.setCreateTime(date);
            baseReq.setCreateUserId(user.getId());
            baseReq.setCreateUserName(user.getUserName());
        }
        baseReq.setUpdateTime(date);
        baseReq.setUpdateUserId(user.getId());
        baseReq.setUpdateUserName(user.getUserName());
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
     * 校验密码安全度
     */
    public static boolean checkPasswordSecurity(String password) {
        //至少8个字符，至少1个大写字母，1个小写字母，1个数字和1个特殊字符：
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!#^%*?&~])[A-Za-z\\d$@$!#^%*?&~]{8,}";
        return Pattern.matches(regex, password);
    }
}
