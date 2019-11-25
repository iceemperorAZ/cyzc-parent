package com.jingliang.mall.common;

import com.jingliang.mall.entity.User;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.req.BaseReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.dom4j.DocumentHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.*;

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
}
