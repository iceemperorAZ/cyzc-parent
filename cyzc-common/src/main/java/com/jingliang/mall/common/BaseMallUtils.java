package com.jingliang.mall.common;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抽象工具类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-09-19 15:19
 */
@Slf4j
public abstract class BaseMallUtils {

    /**
     * 拆分排序
     *
     * @param clause 排序
     * @return 分割后的排序
     */
    public static List<Sort.Order> separateOrder(String clause) {
        clause = clause.replaceAll(" +", " ")
                .replace(", ", ",")
                .replace(" ,", ",");
        List<Sort.Order> orders = new ArrayList<>();
        if (StringUtils.isNotBlank(clause)) {
            if (clause.contains(",")) {
                String[] sorts = clause.split(",");
                for (String sort : sorts) {
                    spaceSplit(sort, orders);
                }
            } else {
                spaceSplit(clause, orders);
            }
        }
        return orders;
    }

    public static void spaceSplit(String clause, List<Sort.Order> orders) {
        String[] order = clause.split(" ");
        String field = order[0];
        String rule = order[1];
        if ("desc".equals(rule.toLowerCase())) {
            orders.add(Sort.Order.desc(field));
        } else {
            orders.add(Sort.Order.asc(field));
        }
    }

    /**
     * Page装MallPage
     *
     * @param page page
     * @return 返回转换后的MallPage
     */
    public static <T> MallPage<T> toMallPage(Page page, Class<T> aClass) {
        MallPage<T> mallPage = new MallPage<>();
        MallBeanMapper.map(page, mallPage);
        mallPage.setContent(MallBeanMapper.mapList(page.getContent(), aClass));
        return mallPage;
    }

    /**
     * 获取文件后缀
     *
     * @param filename 文件名
     * @return 返回文件后缀
     */
    public static String getExtName(String filename) {
        if (StringUtils.isBlank(filename)) {
            return null;
        }
        int index = filename.lastIndexOf(".");
        return index == -1 ? null : filename.substring(index + 1);
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < length; i++) {
            builder.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return builder.toString();
    }

    /**
     * 微信签名MD5计算
     */
    public static String sign(Map<String, String> map, String key) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        builder.append("&key=").append(key);
        log.debug(builder.substring(1));
        return DigestUtils.md5DigestAsHex(builder.substring(1).getBytes()).toUpperCase();
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     */
    public static String mapToXml(Map<String, String> data) {
        StringBuilder builder = new StringBuilder("<xml>");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            builder.append("<").append(entry.getKey()).append(">").append(entry.getValue()).append("</").append(entry.getKey()).append(">");
        }
        return builder.append("</xml>").toString();
    }

    /**
     * xml转map
     *
     * @param xml XML格式的字符串
     * @return Map类型数据
     */
    public static Map<String, String> xmlToMap(String xml) {
        try {
            Map<String, String> map = new TreeMap<>();
            org.dom4j.Document document = DocumentHelper.parseText(xml);
            org.dom4j.Element nodeElement = document.getRootElement();
            List node = nodeElement.elements();
            for (Object o : node) {
                org.dom4j.Element elm = (org.dom4j.Element) o;
                map.put(elm.getName(), elm.getText());
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机IP
     *
     * @return 返回本机Ip
     */
    public static String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解密用户手机号
     *
     * @param sessionKey    sessionkey
     * @param iv            ivData
     * @param encryptedData 带解密数据
     * @return 返回解密后的json数据
     */
    public static String decrypt(String sessionKey, String iv, String encryptedData) {
        // 被加密的数据
        byte[] dataByte = Base64.getDecoder().decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.getDecoder().decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.getDecoder().decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + 1;
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new String(resultByte, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
