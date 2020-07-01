package com.jingliang.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.citrsw.annatation.ApiIgnore;
import com.citrsw.annatation.ApiParam;
import com.jingliang.mall.common.*;
import com.jingliang.mall.entity.*;
import com.jingliang.mall.req.BuyerReq;
import com.jingliang.mall.req.PhoneDataReq;
import com.jingliang.mall.resp.BuyerResp;
import com.jingliang.mall.resp.SignInResp;
import com.jingliang.mall.resp.UserResp;
import com.jingliang.mall.server.FastdfsService;
import com.jingliang.mall.server.RedisService;
import com.jingliang.mall.service.*;
import com.jingliang.mall.wx.service.WechatService;
import com.citrsw.annatation.Api;
import com.citrsw.annatation.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 会员表Controller
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-26 09:12:45
 */
@Api(description = "会员")
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
    private final SignInService signInService;
    private final GoldLogService goldLogService;
    private final TechargeService techargeService;
    private final UnavailableNameService unavailableNameService;
    private final RedisTemplate<String, Object> redisTemplate;

    public BuyerController(BuyerService buyerService, FastdfsService fastdfsService, WechatService wechatService, RedisService redisService, UserService userService, BuyerSaleService buyerSaleService, SignInService signInService, GoldLogService goldLogService, TechargeService techargeService, UnavailableNameService unavailableNameService, RedisTemplate<String, Object> redisTemplate) {
        this.buyerService = buyerService;
        this.fastdfsService = fastdfsService;
        this.wechatService = wechatService;
        this.redisService = redisService;
        this.userService = userService;
        this.buyerSaleService = buyerSaleService;
        this.signInService = signInService;
        this.goldLogService = goldLogService;
        this.techargeService = techargeService;
        this.unavailableNameService = unavailableNameService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 微信登录
     */
    @ApiOperation(description = "微信登录")
    @PostMapping("/wechat/login")
    public Result<BuyerResp> login(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpServletResponse response) {
        log.debug("请求参数：{}", buyerReq);
        if (StringUtils.isBlank(buyerReq.getCode())) {
            log.debug("返回结果：{}", Msg.TEXT_PARAM_FAIL);
            return Result.buildParamFail();
        }
        //调用微信接口获取openid
        Map map = wechatService.getOpenId(buyerReq.getCode());
        if (Objects.isNull(map)) {
            log.debug("返回结果：{}", Msg.TEXT_WECHAT_LOGIN_FAIL);
            return Result.build(Msg.LOGIN_FAIL, Msg.TEXT_WECHAT_LOGIN_FAIL);
        }
        String openId = (String) map.get("openid");
        if (StringUtils.isBlank(openId)) {
            log.debug("返回结果：{}", Msg.TEXT_WECHAT_LOGIN_FAIL);
            return Result.build(Msg.LOGIN_FAIL, Msg.TEXT_WECHAT_LOGIN_FAIL);
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
            buyer.setMemberLevel(100);
            buyer.setOrderSpecificNum(1);
            buyer.setIsNew(true);
            //新用户注册，赠送30金币。
            buyer.setGold(30);
            buyer.setRegisterSource("WX");
            buyer = buyerService.save(buyer);
        }
        //是否封停
        if (buyer.getIsSealUp()) {
            return Result.build(Msg.LOGIN_FAIL, Msg.TEXT_IS_SEAL_UP_FAIL);
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id", buyer.getId() + "");
        tokenMap.put("code", buyerReq.getCode());
        BuyerResp buyerResp = BeanMapper.map(buyer, BuyerResp.class);
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
            buyerResp.setUser(BeanMapper.map(user, UserResp.class));
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
        return Result.build(Msg.OK, Msg.TEXT_LOGIN_OK, buyerResp);
    }

    /**
     * 登出
     */
    @ApiOperation(description = "登出")
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        //暂未涉及
        return Result.buildOk(true);
    }

    /**
     * 解析微信手机号
     */
    @ApiOperation(description = "解析微信手机号")
    @GetMapping("/analysis/phone")
    public Result<String> analysisPhone(@RequestParam @ApiParam(description = "手机号加密算法的初始向量") String iv,
                                        @RequestParam @ApiParam(description = "手机号包括敏感数据在内的完整用户信息的加密数据") String encryptedData,
                                        @ApiIgnore HttpSession session) {
        log.debug("请求参数：iv={},encryptedData={}", iv, encryptedData);
        if (StringUtils.isBlank(iv) || StringUtils.isBlank(encryptedData)) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        String sessionKey = buyer.getSessionKey();
        //解密用户手机号
        String decrypt = MUtils.decrypt(sessionKey, iv, encryptedData);
        if (StringUtils.isNotBlank(decrypt) && StringUtils.isNotBlank(JSONObject.parseObject(decrypt).getString("purePhoneNumber"))) {
            String purePhoneNumber = JSONObject.parseObject(decrypt).getString("purePhoneNumber");
            log.debug("返回解析后的手机号：{}", purePhoneNumber);
            return Result.build(Msg.OK, Msg.TEXT_OK, purePhoneNumber);
        }
        log.debug("解析微信手机号失败");
        return Result.build(Msg.WECHAT_FAIL, Msg.TEXT_WECHAT_SESSION_KEY_TIMEOUT_FAIL);
    }

    /**
     * 修改当前用户的微信手机号
     */
    @ApiOperation(description = "修改当前用户的微信手机号")
    @PostMapping("/analysis/phone")
    public Result<String> analysisPhone(@RequestBody PhoneDataReq phoneDataReq,
                                        @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", phoneDataReq);
        if (StringUtils.isBlank(phoneDataReq.getIv()) || StringUtils.isBlank(phoneDataReq.getEncryptedData())) {
            return Result.buildParamFail();
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        String sessionKey = buyer.getSessionKey();
        //解密用户手机号
        String decrypt = MUtils.decrypt(sessionKey, phoneDataReq.getIv(), phoneDataReq.getEncryptedData());
        if (StringUtils.isNotBlank(decrypt) && StringUtils.isNotBlank(JSONObject.parseObject(decrypt).getString("purePhoneNumber"))) {
            String purePhoneNumber = JSONObject.parseObject(decrypt).getString("purePhoneNumber");
            log.debug("返回解析后的手机号：{}", purePhoneNumber);
            buyer = buyerService.findById(buyer.getId());
            buyer.setPhone(purePhoneNumber);
            buyerService.save(buyer);
            return Result.build(Msg.OK, Msg.TEXT_OK, purePhoneNumber);
        }
        log.debug("解析微信手机号失败");
        return Result.build(Msg.WECHAT_FAIL, Msg.TEXT_WECHAT_SESSION_KEY_TIMEOUT_FAIL);
    }

    /**
     * 修改会员信息
     */
    @ApiOperation(description = "修改会员信息")
    @PostMapping("/update")
    public Result<BuyerResp> update(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        buyer = buyerService.findById(buyer.getId());
        if (Objects.isNull(buyer)) {
            return Result.buildSaveFail();
        }
        buyerReq.setId(buyer.getId());
        if (StringUtils.isNotBlank(buyerReq.getHead())) {
            Base64Image base64Image = Base64Image.build(buyerReq.getHead());
            if (Objects.isNull(base64Image)) {
                log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
                return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
            }
            if (StringUtils.isNotBlank(buyer.getHeadUri())) {
                //新头像保存成功后把之前的头像从图片服务器删除
                fastdfsService.deleteFile(buyer.getHeadUri());
            }
            //保存新头像
            buyerReq.setHeadUri(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
        }
        BuyerResp buyerResp = BeanMapper.map(buyerService.save(BeanMapper.map(buyerReq, Buyer.class)), BuyerResp.class);
        log.debug("返回结果：{}", buyerResp);
        return Result.buildSaveOk(buyerResp);
    }

    /**
     * 绑定销售
     */
    @ApiOperation(description = "绑定销售")
    @PostMapping("/binding/sale")
    public Result<BuyerResp> bindingSale(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpSession session) {
        log.debug("请求参数：{}", buyerReq);
        if (Objects.isNull(buyerReq.getSaleUserId()) || StringUtils.isBlank(buyerReq.getPhone()) || StringUtils.isBlank(buyerReq.getShopName())
                || StringUtils.isBlank(buyerReq.getUserName())) {
            return Result.buildParamFail();
        }
//        if (Objects.isNull(buyerReq.getBuyerTypeLabelList()) || buyerReq.getBuyerTypeLabelList().isEmpty()) {
//            return Result.build(Msg.FAIL, "标签不能为空");
//        }
        if (!unavailableNameService.findNameCount(buyerReq.getShopName())) {
            return Result.build(Msg.FAIL, Msg.TEXT_SHOP_NAME_FAIL);
        }
        User user = userService.findById(buyerReq.getSaleUserId());
        if (Objects.isNull(user)) {
            return Result.build(Msg.FAIL, Msg.TEXT_BUYER_FAIL);
        }
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        buyer = buyerService.findById(buyer.getId());
        if (Objects.isNull(buyer)) {
            return Result.buildSaveFail();
        }
//        //判断图片是否为空
//        if ((Objects.isNull(buyerReq.getBuyerImgBase64s()) || buyerReq.getBuyerImgBase64s().isEmpty()) &&
//                ((Objects.isNull(buyerReq.getBuyerImdUrlsList())) || buyerReq.getBuyerImdUrlsList().isEmpty())) {
//            return Result.build(Msg.FAIL, "图片不能为空");
//        }
////        //判断营业执照是否为空
////        if ((Objects.isNull(buyerReq.getBusinessLicenseBase64s()) || buyerReq.getBusinessLicenseBase64s().isEmpty()) &&
////                ((Objects.isNull(buyerReq.getBusinessLicenseUrlsList())) || buyerReq.getBusinessLicenseUrlsList().isEmpty())) {
////            return Result.build(Msg.FAIL, "营业执照不能为空");
////        }
//        StringBuilder builder = new StringBuilder();
//        //获取本地url
//        String oldBuyerImgUrls = StringUtils.isBlank(buyer.getBuyerImdUrls()) ? "" : buyer.getBuyerImdUrls();
//        //获取要保存的url
//        List<String> buyerImgUrls = buyerReq.getBuyerImdUrlsList();
//        //如果没有要保存的url
//        if (buyerImgUrls.isEmpty()) {
//            if (StringUtils.isNotBlank(oldBuyerImgUrls)) {
//                String[] imgUris = oldBuyerImgUrls.split(";");
//                for (String imgUri : imgUris) {
//                    if (!fastdfsService.deleteFile(imgUri)) {
//                        log.error("图片删除失败：{}", imgUri);
//                    }
//                }
//            }
//        }
//        for (String newBuyerImgUrls : buyerImgUrls) {
//            builder.append(";").append(newBuyerImgUrls);
//        }
//        //获取要删除的url
//        if (builder.length() <= 1) {
//            String delBuyerImgUrls = oldBuyerImgUrls.replaceAll(builder.substring(0), "");
//            if (StringUtils.isNotBlank(delBuyerImgUrls)) {
//                String[] imgUris = delBuyerImgUrls.split(";");
//                for (String imgUri : imgUris) {
//                    if (!fastdfsService.deleteFile(imgUri)) {
//                        log.error("图片删除失败：{}", imgUri);
//                    }
//                }
//            }
//        } else {
//            String delBuyerImgUrls = oldBuyerImgUrls.replaceAll(builder.substring(1), "");
//            if (StringUtils.isNotBlank(delBuyerImgUrls)) {
//                String[] imgUris = delBuyerImgUrls.split(";");
//                for (String imgUri : imgUris) {
//                    if (!fastdfsService.deleteFile(imgUri)) {
//                        log.error("图片删除失败：{}", imgUri);
//                    }
//                }
//            }
//        }
//        //新上传的base64图片处理
//        List<Base64Image> base64Images = new ArrayList<>();
//        for (String buyerImg : buyerReq.getBuyerImgBase64s()) {
//            Base64Image base64Image = Base64Image.build(buyerImg);
//            if (Objects.isNull(base64Image)) {
//                log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
//                return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
//            }
//            base64Images.add(base64Image);
//        }
//        for (Base64Image base64Image : base64Images) {
//            builder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
//        }
//        //营业执照处理
//        StringBuilder businessLicenseBuilder = new StringBuilder();
//        //获取本地url
//        String oldBusinessLicenseUrls = StringUtils.isBlank(buyer.getBusinessLicenseUrls()) ? "" : buyer.getBusinessLicenseUrls();
//        //获取要保存的url
//        List<String> businessLicenseUrlsUrls = buyerReq.getBusinessLicenseUrlsList();
//        //如果没有要保存的url
//        if (businessLicenseUrlsUrls.isEmpty()) {
//            if (StringUtils.isNotBlank(oldBusinessLicenseUrls)) {
//                String[] imgUris = oldBusinessLicenseUrls.split(";");
//                for (String imgUri : imgUris) {
//                    if (!fastdfsService.deleteFile(imgUri)) {
//                        log.error("图片删除失败：{}", imgUri);
//                    }
//                }
//            }
//        }
//        for (String newBuyerImgUrls : businessLicenseUrlsUrls) {
//            businessLicenseBuilder.append(";").append(newBuyerImgUrls);
//        }
//        //获取要删除的url
//        if (businessLicenseBuilder.length() <= 1) {
//            String delBuyerImgUrls = oldBusinessLicenseUrls.replaceAll(businessLicenseBuilder.substring(0), "");
//            if (StringUtils.isNotBlank(delBuyerImgUrls)) {
//                String[] imgUris = delBuyerImgUrls.split(";");
//                for (String imgUri : imgUris) {
//                    if (!fastdfsService.deleteFile(imgUri)) {
//                        log.error("图片删除失败：{}", imgUri);
//                    }
//                }
//            }
//        } else {
//            String delBuyerImgUrls = oldBusinessLicenseUrls.replaceAll(businessLicenseBuilder.substring(1), "");
//            if (StringUtils.isNotBlank(delBuyerImgUrls)) {
//                String[] imgUris = delBuyerImgUrls.split(";");
//                for (String imgUri : imgUris) {
//                    if (!fastdfsService.deleteFile(imgUri)) {
//                        log.error("图片删除失败：{}", imgUri);
//                    }
//                }
//            }
//        }
//        //新上传的base64图片处理
//        List<Base64Image> businessLicenseBase64Images = new ArrayList<>();
//        for (String businessLicenseImg : buyerReq.getBusinessLicenseBase64s()) {
//            Base64Image base64Image = Base64Image.build(businessLicenseImg);
//            if (Objects.isNull(base64Image)) {
//                log.debug("返回结果：{}", Msg.TEXT_IMAGE_FAIL);
//                return Result.build(Msg.IMAGE_FAIL, Msg.TEXT_IMAGE_FAIL);
//            }
//            businessLicenseBase64Images.add(base64Image);
//        }
//        for (Base64Image base64Image : businessLicenseBase64Images) {
//            businessLicenseBuilder.append(";").append(fastdfsService.uploadFile(base64Image.getBytes(), base64Image.getExtName()));
//        }
//        //商户标签处理
//        StringBuilder buyerTypeLabelBuilder = new StringBuilder();
//        for (String buyerTypeLabel : buyerReq.getBuyerTypeLabelList()) {
//            buyerTypeLabelBuilder.append(";").append(buyerTypeLabel);
//        }
//        //保存标签集合
//        buyer.setBuyerTypeLabel(buyerTypeLabelBuilder.length() > 1 ? buyerTypeLabelBuilder.substring(1) : "");
//        //保存图片url集合
//        buyer.setBuyerImdUrls(builder.length() > 1 ? builder.substring(1) : "");
//        //保存营业执照url集合
//        buyer.setBusinessLicenseUrls(businessLicenseBuilder.length() > 1 ? businessLicenseBuilder.substring(1) : "");
        buyer.setSaleUserId(user.getId());
        buyer.setUpdateTime(new Date());
        //商户审核状态为待审核
        buyer.setBuyerStatus(100);
        buyerService.save(buyer);
        Date date = new Date();
        buyerReq.setId(buyer.getId());
        BuyerSale buyerSale = new BuyerSale();
        buyerSale.setBuyerId(buyer.getId());
        buyerSale.setSaleId(buyerReq.getSaleUserId());
        //将这个值设置的大一点
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2300);
        buyerSale.setUntyingTime(calendar.getTime());
        buyerSale.setIsAvailable(true);
        buyerSale.setCreateTime(date);
        buyerSale.setUpdateTime(date);
        BuyerAddress buyerAddress = new BuyerAddress();
        buyerAddress.setBuyerId(buyer.getId());
        buyerAddress.setProvinceCode(buyerReq.getProvinceCode());
        buyerAddress.setCityCode(buyerReq.getCityCode());
        buyerAddress.setAreaCode(buyerReq.getAreaCode());
        buyerAddress.setDetailedAddress(buyerReq.getDetailedAddress());
        buyerAddress.setIsDefault(true);
        buyerAddress.setIsAvailable(true);
        buyerAddress.setCreateTime(new Date());
        buyerAddress.setUpdateTime(new Date());
        buyerAddress.setConsigneeName(buyerReq.getUserName());
        buyerAddress.setPhone(buyerReq.getPhone());
        buyerAddress.setPostCode("0000");
        BuyerResp buyerResp = BeanMapper.map(buyerSaleService.bindingSale(buyerSale, BeanMapper.map(buyerReq, Buyer.class), buyerAddress), BuyerResp.class);
        assert buyerResp != null;
        buyerResp.setSale(BeanMapper.map(user, UserResp.class));
        log.debug("返回结果：{}", buyerResp);
        return Result.buildSaveOk(buyerResp);
    }

    /**
     * 免责通知
     */
    @GetMapping("/exemption")
    @ApiOperation(description = "免责通知")
    public Result<String> analysisPhone() {
        return Result.buildQueryOk(Msg.EXEMPTION);
    }

    /**
     * 签到
     */
    @PostMapping("/signIn")
    @ApiOperation(description = "签到")
    public Result<SignInResp> signIn(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        LocalDate localDate = LocalDate.now();
        SignIn signIn = signInService.findByBuyerIdAndLastDay(buyer.getId());
        if (signIn != null && localDate.equals(signIn.getLastDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
            //今天签到了就直接返回
            return Result.build(Msg.FAIL, "今日已签到");
        }
        signIn = signInService.signIn(buyer.getId());
        SignInResp signInResp = BeanMapper.map(signIn, SignInResp.class);
        return Result.build(Msg.OK, "签到成功", signInResp);
    }

    /**
     * 查询签到
     */
    @GetMapping("/signIn")
    @ApiOperation(description = "查询签到")
    public Result<SignInResp> querySignIn(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        SignIn signIn = signInService.querySignIn(buyer.getId());
        SignInResp signInResp = BeanMapper.map(signIn, SignInResp.class);
        return Result.buildQueryOk(signInResp);
    }

    /**
     * 充金币
     */
    @PostMapping("/recharge")
    @ApiOperation(description = "充金币")
    public Result<Map<String, String>> recharge(@RequestBody Map<String, Long> map, HttpSession session) {
        Long id = map.get("id");
        Techarge techarge = techargeService.findById(id);
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        Order order = new Order();
        order.setOrderNo(redisService.getOrderNo());
        order.setPayableFee(techarge.getMoney().longValue());
        Map<String, String> resultMap = wechatService.recharge(order, buyer.getUniqueId());
        GoldLog goldLog = new GoldLog();
        goldLog.setBuyerId(buyer.getId());
        goldLog.setIsAvailable(false);
        goldLog.setMoney(Math.toIntExact(techarge.getMoney().longValue()));
        goldLog.setGold(techarge.getGold());
        goldLog.setCreateTime(new Date());
        goldLog.setPayNo(order.getOrderNo());
        goldLog.setType(200);
        goldLogService.save(goldLog);
        resultMap.put("id", order.getId() + "");
        resultMap.put("orderNo", order.getOrderNo());
        log.debug("微信返回结果二次签名后的返回结果：{}", resultMap);
        return Result.buildSaveOk(resultMap);
    }

    /**
     * 获取用户最新信息
     */
    @ApiOperation(description = "获取用户最新信息")
    @GetMapping("/buyer")
    public Result<BuyerResp> buyer(HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute(sessionBuyer);
        //获取用户最新信息
        return Result.build(Msg.OK, Msg.TEXT_LOGIN_OK, BeanMapper.map(buyerService.findById(buyer.getId()), BuyerResp.class));
    }

    /**
     * 登录(手机号+密码)
     */
    @ApiOperation(description = "登录(手机号+密码)")
    @PostMapping("/login/phone")
    public Result<BuyerResp> loginByPhone(@RequestBody BuyerReq buyerReq, @ApiIgnore HttpServletResponse response) {
        log.debug("请求参数：{}", buyerReq);
        if (StringUtils.isBlank(buyerReq.getPhone()) || StringUtils.isBlank(buyerReq.getPassword())) {
            return Result.buildParamFail();
        }
        Buyer buyer = buyerService.findFirstByPhone(buyerReq.getPhone());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //判断用户密码是否匹配
        if (!passwordEncoder.matches(buyerReq.getPassword(), buyer.getPassword())) {
            return Result.build(Msg.FAIL, "密码不正确");
        }
        //是否封停
        if (buyer.getIsSealUp()) {
            return Result.build(Msg.LOGIN_FAIL, Msg.TEXT_IS_SEAL_UP_FAIL);
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("id", buyer.getId() + "");
        BuyerResp buyerResp = BeanMapper.map(buyer, BuyerResp.class);
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
            buyerResp.setUser(BeanMapper.map(user, UserResp.class));
        }
        //生成token
        String token = JwtUtil.genToken(tokenMap);
        buyer.setToken(token);
        buyer.setSessionKey("phone=" + buyer.getPhone() + "pwd=" + buyer.getPassword());
        //存入redis  有效时长为1800秒（半小时）
        redisService.setExpire(tokenBuyerPrefix + buyer.getId(), buyer, tokenTimeOut);
        response.setHeader("Authorization", token);
        log.debug("微信登录Token= {}", token);
        log.debug("返回结果：{}", buyerResp);
        return Result.build(Msg.OK, Msg.TEXT_LOGIN_OK, buyerResp);
    }

    /**
     * 注册：手机号 + 短信验证码 + 昵称 + 密码 【注册完成后直接登录】
     */
    @PostMapping("/register")
    public Result<Buyer> register(@RequestBody BuyerReq buyerReq, HttpServletResponse response) {
        String redisCode = (String) redisTemplate.opsForValue().get(Constant.CODE_PREFIX + buyerReq.getPhone());
        //校验验证码
        if (buyerReq.getCode() == null || !StringUtils.equals(redisCode, buyerReq.getCode())) {
            return Result.build(Msg.FAIL, "验证码错误");
        }
//        if (StringUtils.isBlank(buyerReq.getNickName())) {
//            return Result.build(Msg.FAIL, "昵称不能为空");
//        }
//        if (StringUtils.isBlank(buyerReq.getUsername())) {
//            return Result.build(Msg.FAIL, "账号不能为空");
//        }
//        if (StringUtils.isBlank(buyerReq.getPhone())) {
//            return Result.build(Msg.FAIL, "手机号不能为空");
//        } else {
//            if (!MUtils.phoneCheck(buyerReq.getPhone())) {
//                return Result.build(Msg.FAIL, "手机号格式错误");
//            }
//        }
//        //校验手机号是否已经注册
//        User checkUser = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, buyerReq.getPhone()));
//        if (checkUser != null) {
//            return Result.build(Msg.FAIL, "该手机号已经注册");
//        }
//        //校验账号是否已经注册
//        checkUser = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, buyerReq.getUsername()));
//        if (checkUser != null) {
//            return Result.build(Msg.FAIL, "该手机号已经注册");
//        }
//        //校验昵称是否已经注册
//        checkUser = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getNickName, buyerReq.getNickName()));
//        if (checkUser != null) {
//            return Result.build(Msg.FAIL, "该昵称已经注册");
//        }
//        if (StringUtils.isNotBlank(buyerReq.getMail())) {
//            if (!Utils.mailCheck(buyerReq.getMail())) {
//                return Result.build(Msg.FAIL, "邮箱格式错误");
//            }
//            //校验邮箱是否已经注册
//            checkUser = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMail, buyerReq.getMail()));
//            if (checkUser != null) {
//                return Result.build(Msg.FAIL, "该邮箱已经注册");
//            }
//        }
//        LocalDateTime localDateTime = LocalDateTime.now();
//        buyerReq.setCreateTime(localDateTime);
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        buyerReq.setPassword(bCryptPasswordEncoder.encode(buyerReq.getPassword()));
//        buyerReq.setUpdateTime(localDateTime);
//        buyerReq.setActivation(true);
//        //注册
//        if (!userService.register(buyerReq)) {
//            return Result.build(Msg.FAIL, "注册失败");
//        }
//        //登录
//        //生成token
//        String token = JwtUtil.genToken(buyerReq.getId());
//        //在redis中进行无操作自动离线倒计时
//        response.setHeader(JwtUtil.AUTH_HEADER_KEY, token);
//        //清除注册次数
//        redisTemplate.delete(Constant.LIMIT_PREFIX + buyerReq.getPhone());
//        redisTemplate.opsForValue().set(Constant.ON_LINE_USER_PREFIX + buyerReq.getId(), token, Constant.ON_LINE_TIME_USER, TimeUnit.SECONDS);
//        response.setHeader(JwtUtil.AUTH_HEADER_KEY, token);
//        return Result.build(Msg.OK, "注册成功", buyerReq);
        return null;
    }

}