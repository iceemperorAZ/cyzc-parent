package com.jingliang.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.Buyer;
import com.jingliang.mall.entity.BuyerSale;
import com.jingliang.mall.entity.User;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.req.PhoneDataReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.BuyerSaleService;
import com.jingliang.mall.service.BuyerService;
import com.jingliang.mall.service.UserService;
import com.jingliang.mall.wx.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 会员表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
@Api(tags = "会员")
@RequestMapping("/front/buyer")
@RestController
@Slf4j
public class BuyerController {
    /**
     * session用户Key
     */
    @Value("${session.buyer.key}")
    private String sessionBuyer;
    @Value("${token.buyer.redis.prefix}")
    private String tokenBuyerPrefix;
    @Value("${token.user.redis.prefix}")
    private String tokenUserPrefix;
    /**
     * 用户session过期时间
     */
    @Value("${token.timeout}")
    private Integer tokenTimeOut;
    private final BuyerService buyerService;
    private final FastdfsService fastdfsService;
    private final WechatService wechatService;
    private final RedisService redisService;
    private final UserService userService;
    private final BuyerSaleService buyerSaleService;

    public BuyerController(BuyerService buyerService, FastdfsService fastdfsService, WechatService wechatService, RedisService redisService, UserService userService, BuyerSaleService buyerSaleService) {
        this.buyerService = buyerService;
        this.fastdfsService = fastdfsService;
        this.wechatService = wechatService;
        this.redisService = redisService;
        this.userService = userService;
        this.buyerSaleService = buyerSaleService;
    }

    /**
     * 微信登录
     */
    @ApiOperation(value = "微信登录")
    @PostMapping("/wechat/login")
    public MallResult<BuyerResp> login(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpServletResponse response) {
        log.debug("请求参数：{}", buyerReq);
        if (StringUtils.isBlank(buyerReq.getCode())) {
            log.debug("返回结果：{}", MallConstant.TEXT_PARAM_FAIL);
            return MallResult.buildParamFail();
        }
        //调用微信接口获取openid
        Map map = wechatService.getOpenId(buyerReq.getCode());
        if (Objects.isNull(map)) {
            log.debug("返回结果：{}", MallConstant.TEXT_WECHAT_LOGIN_FAIL);
            return MallResult.build(MallConstant.LOGIN_FAIL, MallConstant.TEXT_WECHAT_LOGIN_FAIL);
        }
        String openId = (String) map.get("openid");
        if (StringUtils.isBlank(openId)) {
            log.debug("返回结果：{}", MallConstant.TEXT_WECHAT_LOGIN_FAIL);
            return MallResult.build(MallConstant.LOGIN_FAIL, MallConstant.TEXT_WECHAT_LOGIN_FAIL);
        }
        Buyer buyer = buyerService.findByUniqueId(openId);
        if (Objects.isNull(buyer)) {
            buyer = new Buyer();
            buyer.setUniqueId(openId);
            Date date = new Date();
            buyer.setCreateTime(date);
            buyer.setUpdateTime(date);
            buyer.setLastOrderTime(date);
            buyer.setIsAvailable(true);
            buyer.setMemberIntegral(0);
            buyer.setIsSealUp(false);
            buyer.setIsNew(true);
            buyer.setRegisterSource("WX");
            buyer = buyerService.save(buyer);
        }
        //是否封停
        if (buyer.getIsSealUp()) {
            return MallResult.build(MallConstant.LOGIN_FAIL, MallConstant.TEXT_IS_SEAL_UP_FAIL);
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id", buyer.getId() + "");
        tokenMap.put("code", buyerReq.getCode());
        BuyerResp buyerResp = MallBeanMapper.map(buyer, BuyerResp.class);
        User user = userService.findByBuyerId(buyer.getId());
        log.debug("WX登录信息{}", user);
        assert buyerResp != null;
        if (Objects.nonNull(user)) {
            tokenMap.put("userId", user.getId() + "");
            log.debug("WX登录绑定的员工{}", user);
            //存入redis   有效时长为1800秒（半小时）
            redisService.setExpire(tokenUserPrefix + "FRONT-" + user.getId(), user, tokenTimeOut);
            Integer level = user.getLevel();
            buyerResp.setLevel(level);
            buyerResp.setUser(MallBeanMapper.map(user, UserResp.class));
        }
        //生成token
        String token = JwtUtil.genToken(tokenMap);
        buyer.setToken(token);
        buyer.setSessionKey(map.get("session_key") + "");
        //存入redis  有效时长为1800秒（半小时）
        redisService.setExpire(tokenBuyerPrefix + buyer.getId(), buyer, tokenTimeOut);
        response.setHeader("Authorization", token);
        log.debug("微信登录Token= {}", token);
        log.debug("返回结果：{}", buyerResp);
        return MallResult.build(MallConstant.OK, MallConstant.TEXT_LOGIN_OK, buyerResp);
    }

    /**
     * 登出
     */
    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public MallResult<Boolean> logout() {
        //暂未涉及
        return MallResult.buildOk(true);
    }

    /**
     * 解析微信手机号
     */
    @ApiOperation(value = "解析微信手机号")
    @GetMapping("/analysis/phone")
    public MallResult<String> analysisPhone(@RequestParam @ApiParam("手机号加密算法的初始向量") String iv,
                                            @RequestParam @ApiParam("手机号包括敏感数据在内的完整用户信息的加密数据") String encryptedData,
                                            @ApiIgnore HttpSession session) {
        log.debug("请求参数：iv={},encryptedData={}", iv, encryptedData);
        if (StringUtils.isBlank(iv) || StringUtils.isBlank(encryptedData)) {
            return MallResult.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        String sessionKey = buyer.getSessionKey();
        //解密用户手机号
        String decrypt = MallUtils.decrypt(sessionKey, iv, encryptedData);
        if (StringUtils.isNotBlank(decrypt) && StringUtils.isNotBlank(JSONObject.parseObject(decrypt).getString("purePhoneNumber"))) {
            String purePhoneNumber = JSONObject.parseObject(decrypt).getString("purePhoneNumber");
            log.debug("返回解析后的手机号：{}", purePhoneNumber);
            return MallResult.build(MallConstant.OK, MallConstant.TEXT_OK, purePhoneNumber);
        }
        log.debug("解析微信手机号失败");
        return MallResult.build(MallConstant.WECHAT_FAIL, MallConstant.TEXT_WECHAT_SESSION_KEY_TIMEOUT_FAIL);
    }

    /**
     * 修改当前用户的微信手机号
     */
    @ApiOperation(value = "修改当前用户的微信手机号")
    @PostMapping("/analysis/phone")
    public MallResult<String> analysisPhone(@RequestBody PhoneDataReq phoneDataReq,
                                            @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", phoneDataReq);
        if (StringUtils.isBlank(phoneDataReq.getIv()) || StringUtils.isBlank(phoneDataReq.getEncryptedData())) {
            return MallResult.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        String sessionKey = buyer.getSessionKey();
        //解密用户手机号
        String decrypt = MallUtils.decrypt(sessionKey, phoneDataReq.getIv(), phoneDataReq.getEncryptedData());
        if (StringUtils.isNotBlank(decrypt) && StringUtils.isNotBlank(JSONObject.parseObject(decrypt).getString("purePhoneNumber"))) {
            String purePhoneNumber = JSONObject.parseObject(decrypt).getString("purePhoneNumber");
            log.debug("返回解析后的手机号：{}", purePhoneNumber);
            buyer.setPhone(purePhoneNumber);
            buyerService.save(buyer);
            return MallResult.build(MallConstant.OK, MallConstant.TEXT_OK, purePhoneNumber);
        }
        log.debug("解析微信手机号失败");
        return MallResult.build(MallConstant.WECHAT_FAIL, MallConstant.TEXT_WECHAT_SESSION_KEY_TIMEOUT_FAIL);
    }

    /**
     * 修改会员信息
     */
    @ApiOperation(value = "修改会员信息")
    @PostMapping("/update")
    public MallResult<BuyerResp> update(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        buyer = buyerService.findById(buyer.getId());
        if (Objects.isNull(buyer)) {
            return MallResult.buildSaveFail();
        }
        buyerReq.setId(buyer.getId());
        if (StringUtils.isNotBlank(buyerReq.getHead())) {
            Base64Image base64Image = Base64Image.build(buyerReq.getHead());
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", MallConstant.TEXT_IMAGE_FAIL);
                return MallResult.build(MallConstant.IMAGE_FAIL, MallConstant.TEXT_IMAGE_FAIL);
            }
            if (StringUtils.isNotBlank(buyer.getHeadUri())) {
                //新头像保存成功后把之前的头像从图片服务器删除
                fastdfsService.deleteFile(buyer.getHeadUri());
            }
            //保存新头像
            buyerReq.setHeadUri(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        BuyerResp buyerResp = MallBeanMapper.map(buyerService.save(MallBeanMapper.map(buyerReq, Buyer.class)), BuyerResp.class);
        log.debug("返回结果：{}", buyerResp);
        return MallResult.buildSaveOk(buyerResp);
    }

    /**
     * 绑定销售
     */
    @ApiOperation(value = "绑定销售")
    @PostMapping("/binding/sale")
    public MallResult<BuyerResp> bindingSale(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        if (Objects.isNull(buyerReq.getSaleUserId()) || StringUtils.isBlank(buyerReq.getPhone()) || StringUtils.isBlank(buyerReq.getShopName())
                || StringUtils.isBlank(buyerReq.getUserName())) {
            return MallResult.buildParamFail();
        }
        User user = userService.findById(buyerReq.getSaleUserId());
        if (Objects.isNull(user)) {
            return MallResult.build(MallConstant.FAIL, MallConstant.TEXT_BUYER_FAIL);
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        buyer = buyerService.findById(buyer.getId());
        if (Objects.isNull(buyer)) {
            return MallResult.buildSaveFail();
        }
        Date date = new Date();
        buyerReq.setId(buyer.getId());
        BuyerResp buyerResp = MallBeanMapper.map(buyerService.save(MallBeanMapper.map(buyerReq, Buyer.class)), BuyerResp.class);
        BuyerSale buyerSale = new BuyerSale();
        buyerSale.setBuyerId(buyer.getId());
        buyerSale.setSaleId(buyerReq.getSaleUserId());
        buyerSale.setIsAvailable(true);
        buyerSale.setCreateTime(date);
        buyerSale.setUpdateTime(date);
        buyerSale = buyerSaleService.save(buyerSale);
        assert buyerResp != null;
        buyerResp.setSale(MallBeanMapper.map(user, UserResp.class));
        log.debug("返回结果：{}", buyerResp);
        return MallResult.buildSaveOk(buyerResp);
    }
}