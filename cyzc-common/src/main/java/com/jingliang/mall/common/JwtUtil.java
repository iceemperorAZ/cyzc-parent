package com.jingliang.mall.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * jwt工具类
 *
 * @author Zhenfeng Li
 * @version 1.0
 * @date 2019-10-24 11:13
 */
public class JwtUtil {
    private static final String SECRET = "bIvzjavMFcFKfHcOOU9ERSl5oqJ0ZvDq";
    private static String ISSUER = "cleancode";

    /**
     * 生成token
     *
     * @param claims          参数
     * @param expireDatePoint 过期时间点
     * @return 返回token
     */
    public static String genToken(Map<String, String> claims, Date expireDatePoint) {
        try {
            //加入时间毫秒值，保证每次都不一样
            claims.put("time", System.currentTimeMillis() + "");
            //使用HMAC256进行加密
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //创建jwt
            JWTCreator.Builder builder = JWT.create()
                    //发行人
                    .withIssuer(ISSUER);
            //过期时间点
            if (Objects.nonNull(expireDatePoint)) {
                builder.withExpiresAt(expireDatePoint);
            }
            //传入参数
            claims.forEach(builder::withClaim);
            //签名加密
            return "Bearer " + builder.sign(algorithm);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成token
     *
     * @param claims 参数
     * @return 返回token
     */
    public static String genToken(Map<String, String> claims) {
        return genToken(claims, null);
    }

    /**
     * 解密jwt
     *
     * @param token token
     * @return 返回参数
     */
    public static Map<String, String> verifyToken(String token) {
        token = token.replaceFirst("Bearer ", "");
        //使用HMAC256进行加密
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        //解密
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> map = jwt.getClaims();
            Map<String, String> resultMap = new HashMap<>();
            map.forEach((k, v) -> resultMap.put(k, v.asString()));
            return resultMap;
        } catch (TokenExpiredException ignored) {
        }
        //token过期
        return null;
    }
}
