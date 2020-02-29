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

    /**
     * 免责通知
     */
    @ApiOperation(value = "免责通知")
    @GetMapping("/exemption")
    public MallResult<String> analysisPhone() {
       return MallResult.buildQueryOk("厨亿直采用户服务协议\r\n" +
               " 【审慎阅读】您在申请注册流程中点击同意本协议之前，应当认真阅读本协议。请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款、法律适用和争议解决条款。免除或者限制责任的条款将以粗体下划线标识，您应重点阅读。如您对协议有任何疑问，可按照本协议中的联系方式向我们咨询，我们会为您做进一步解释和说明。 \r\n" +
               "【民事行为能力】您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力。 如您未满 18 周岁或者不具备相应的民事行为能力，请您在法定监护人的陪同下阅读本服务 协议，并在进行注册、下单、支付等任何行为或使用厨亿直采平台其他任何服务 前，应事先征得您法定监护人的同意。 \r\n" +
               "【签约动作】当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后， 即表示您已充分阅读、理解并接受本协议的全部内容，并与厨亿直采平台达成一致，成为厨亿直采平台的“用户”。在阅读本协议的过程中，如果您不同意本协 议或其中任何条款约定，您应立即停止注册程序。\r\n" +
               "一、概述 \r\n" +
               "第一条 本服务协议双方为上海晶粮实业有限公司旗下网站“厨亿直采”（下称“厨亿直采”）与厨亿直采平台用户（下称“用户”或“您”），本服务协议具有合同效力。 \r\n" +
               "第二条 厨亿直采平台经营者指经营厨亿直采平台的法律主体，您可随时查看平台底部公示的证照信息以确定向您履约的厨亿直采主体。本协议下，厨亿直采平台经营者可能根据平台的业务调整而发生变更，变更后的厨亿直采平台经营者与您共同履行本协议并向您提供服务，厨亿直采平台经营者的变更不会影响您本协议项下的权益。发生争议时，您可根据您具体使用的服务及对您权 益产生影响的具体行为对象确定与您履约的主体及争议相对方。 \r\n" +
               "第三条 由于互联网高速发展，您与厨亿直采签署的本服务协议列明的条款并不能完整罗列并覆盖您与厨亿直采所有权利与义务，现有的约定也不能保证完全符合未来发展的需求。因此，厨亿直采平台的隐私权政策及其他各类规则为本 协议的补充协议，与本协议不可分割且具有同等法律效力。如您使用厨亿直采平台服务，视为您同意上述补充协议。 \r\n" +
               "第四条 根据国家法律法规变化、运营需要或为提升服务质量的目的，厨亿直采将在必要的时候对上述各项协议、条款与规则进行修改更新，并通过在网页或移动客户端上发出公告、通知等合理、醒目的方式向您进行提前通知。您应当及时查阅并了解相关更 新修改内容，如您不同意相关更新修改内容，可停止使用相关更新修改内容所涉及的服务， 此情形下，变更事项对您不产生效力；如您在上述更新修改内容实施后继续使用所涉及的服务，将视为您已同意各项更新修改内容。 \r\n" +
               "二、定义 \r\n" +
               "厨亿直采的用户： \r\n" +
               "1、用户为具备完全民事行为能力的自然人。无民事 行为能力人、限制民事行为能力人不当注册为厨亿直采用户或超过其民事权利或 行为能力范围从事交易的，其与厨亿直采之间的服务协议自始无效，厨亿直采一经发现，有权立即注销该账号，并追究其使用厨亿直采“服务”的一切 法律责任。 \r\n" +
               "2、用户注册是指用户登录厨亿直采平台，并按要求填写相关信息并确 认同意履行相关服务协议的过程。用户因进行交易，获取订购商品的有偿服务或接触厨亿直采平台服务器而发生的所有应纳税赋，以及一切硬件、软件、服务及其他方面的费用均由用户负责支付。厨亿直采平台仅作为网上订购的交易平台。 \r\n" +
               "3、厨亿直采平台： 由厨亿直采自主开发，提供给用户进行 网站上交易的平台软件，用户可以使用此系统进行订单管理、商户可以使用此系统进行店铺 和外卖信息的发布和订单管理。 \r\n" +
               "三、用户的权利和义务\r\n" +
               "为了给广大用户营造一个健康、有序的网络服务交易平台，厨亿直采倡导诚信交易，并为此提供一系列的解决方案。 厨亿直采为用户提供交易平台，厨亿直采只能部分控制交易所涉及的产品的质量、安全或合法性、商贸信息的真实性或准确性，以及交易方履行其在贸易协议下的各项义务的能力。鉴于厨亿直采平台具备存在海量信息及信息网络环境下信息与实物相分离的特点，厨亿直采平台无法逐一审查商品及/或服务的信息，无法逐一 审查交易所涉及的商品及/或服务的质量、安全以及合法性、真实性、准确性，对此您应谨 慎判断。 \r\n" +
               "四、用户的权利： \r\n" +
               "(一) 用户有权在向厨亿直采提交相关注册资料后创立厨亿直采账号，拥有自己在厨亿直采平台的用户名及密码，并有权利使用自己的用户名及密码登录厨亿直采平台。厨亿直采星选使用厨亿直采平台账号为您提供服务，您需要先创立厨亿直采平台账号。除因历史原因、业务整合等厨亿直采所认可的特殊情况外，厨亿直采原则上只允许每位用户使用一个厨亿直采平台账号。用户不得以任何形式擅自转让或授权他人使用自己的厨亿直采账号； \r\n" +
               "(二) 用户有权利使用厨亿直采平台和厨亿直采商户在线取得直接联系，以获得最新的商品信息和相关增值服务；用户点击“去结算”按钮后系统会自动生成 包含商品名称、购买数量、应付总额、收货地址（如有）等信息的订单，用户应仔细核对。 用户点击订单底部的“去支付”按钮，视为用户已阅读并同意外卖页面的所有内容并认可订 单内容，订单内容页面信息视为用户与商户之间的交易合同。 \r\n" +
               "(三) 商户确认用户订单后，选择配送的订单将由商户完成配送服务。用户同意并知晓，部分商品由商户向您提供送货服务，在此情况下，送货服务相关标准按该商户在厨亿直采平台公示的标准执行（包括但不限于费用标准、送货 时限等）；同时，部分商品由第三方配送服务商向您提供配送服务，为您提供配送服务的一方将向您收取一定的配送服务费用，具体金额以您下单时系统显示的金额为准，您知悉并授权第三方配送服务商代您向商户取货并将商品送到您所指定的收货地址。用户支付的配送费用由实际的配送服务提供商收取。用户有权以实际支付的金额为限要求商品发票，商品金额发票由商户提供，配送费金额发票由实际提供配送服务的商户或配送服务商提供。\r\n" +
               "(四) 用户有权利对厨亿直采商户的服务做出评价和投诉，并提出建议和意见； " +
               "(五) 用户在厨亿直采平台交易过程中如与商户因交易产生纠纷，可以请求厨亿直采从中予以协调。对因商户在整个物流配送过程中导致的货物损坏、损毁、灭失等情况，将由对应责任方承担赔偿责任，厨亿直采将根据平台规则，积极配合用户对该部分损失进行追偿。用户如发现其他用户有违法或违反本服务协议的行为，可以向厨亿直采进行反映要求处理。如用户因交易与其他用户产生诉讼的，用户有权通过司法部门要求厨亿直采提供相关资料。 \r\n" +
               "五、用户的义务： \r\n" +
               "(一) 用户有义务在注册及使用厨亿直采平台时提供自己的真实资料，并保证 诸如电子邮件地址、联系电话、联系地址等内容的有效性及安全性，保证厨亿直采及其他用户可以通过上述联系方式与您进行联系。同时，用户也有义务在相关资料实际变更时及时更新有关资料。用户保证不以他人资料在厨亿直采平台进行注册。若用户使用虚假电话、姓名、地址或冒用他人信息使用厨亿直采平台服务，厨亿直采将做出相应处罚或屏蔽地址的处理； \r\n" +
               "(二) 用户承诺自己在使用厨亿直采平台时不会出现以下违规行为，如因出现 以下违规行为导致任何法律后果的发生，厨亿直采用户将以自己的名义独立承担 所有法律责任： \r\n" +
               "1. 违反国家法律、法规要求以及各种社会公共利益或公共道德的行为；\r\n" +
               "2. 违反厨亿直采平台规定的行为（包括但不限于制造虚假订单、利用系统漏洞 获取利润、违规套现等）； \r\n" +
               "3. 其他损害厨亿直采平台、商户和其他用户利益的行为。\r\n" +
               "(三) 用户同意，不得对厨亿直采平台上任何资料作商业性利用，包括但不限 于在未经厨亿直采事先书面同意的情况下，以复制、传播等方式使用在厨亿直采平台上展示的任何资料； \r\n" +
               "(四) 用户同意接收来自厨亿直采平台或者厨亿直采平台合作伙伴 发出的邮件、信息。如果您不愿意接受此类信息，您有权通过厨亿直采平台提供的相应退订方式进行退订。 \r\n" +
               "六、厨亿直采的权利\r\n" +
               "(一) 用户在厨亿直采平台交易过程中如与其他用户因交易产生纠纷，请求厨亿直采从中予以调处，经厨亿直采审核后，厨亿直采有权通 过电话或电子邮件向纠纷双方了解情况，并将所了解的情况通过电话或电子邮件互相通知对方； \r\n" +
               "(二) 厨亿直采有权对用户的注册资料及交易行为进行查阅，发现注册资料或交易行为中存在违反国家法律法规或厨亿直采平台规则时，厨亿直采有权根据相关规定进行处理；\r\n" +
               "(三) 许可使用权。对于用户提供、发布及在使用厨亿直采平台服务中形成的除个人信息外的文字、图片、视频、音频等非个人信息，在法律规定的保护期限内您免费授予厨亿直采的许可使用权利及再授权给其他第三方使用并可以自身名义对第三方侵权行为取证及提起诉讼的权利。您同意厨亿直采及其关联公司存储、使用、复制、修订、编辑、发布、展示、翻译、分发您的非个人信息或制作其派生作品，并以已知或日后开发的形式、媒体或技术将上述信息纳入其它作品内。 \r\n" +
               "七、厨亿直采的义务：\r\n" +
               "(一) 厨亿直采有义务在现有技术上维护整个交易平台的正常运行，并努力提 升和改进技术，使用户网上订购交易活动得以顺利进行。 \r\n" +
               "(二) 对用户在注册使用厨亿直采平台中所遇到的与交易或注册有关的问题及 反映的情况，厨亿直采应及时做出回复。 \r\n" +
               "(三) 用户因在厨亿直采平台进行的订购交易与其他用户产生诉讼的，用户通 过司法部门或行政部门依照法定程序要求厨亿直采提供相关资料，厨亿直采应积极配合并提供有关资料。 \r\n" +
               "八、隐私权政策 \r\n" +
               "厨亿直采非常重视用户个人信息的保护，根据有关法律法规要求， 网络产品、服务具有收集用户信息功能的，其提供者应当向用户明示并取得同意。厨亿直采特此通过单独明示的《隐私权政策》明确向您告知收集、使用用户个人信息的目的、方式和范围，查询、更正信息的渠道以及拒绝提供信息的后果。厨亿直采希 望通过隐私权政策向您清楚地介绍厨亿直采对您个人信息的处理方式，因此厨亿直采建议您完整地阅读《隐私权政策》，以帮助您更好地保护您的隐私权。 \r\n" +
               "九、用户违约及处理 \r\n" +
               "第一条 发生如下情形之一的，视为您违约： \r\n" +
               "(一) 使用我方平台服务时违反有关法律法规规定的； \r\n" +
               "(二) 违反本协议或本协议补充协议约定的。 为适应互联网行业发展和满足海量用户对高效优质服务的需求，您理解并同意，厨亿直采可在厨亿直采平台规则中约定违约认定的程序和标准。如：厨亿直采可依据您的用户数据与海量用户数据的关系来认定您是否构成违约；您有权利对您的数据异常现象进行充分举证和合理解释，否则将被认定为违约。 \r\n" +
               "第二条 违约处理措施 \r\n" +
               "(一) 您在厨亿直采平台上发布的内容和信息构成违约的，厨亿直采平台有权根据相应规则立即对相应内容和信息进行删除、屏蔽等处理或对您的账号进行暂停使用、查封、注销等处理。我方平台在法律有明确规定的情况下承担相应的责任。 \r\n" +
               "(二) 您在厨亿直采平台上实施的行为，或虽未在厨亿直采平台上实施但对厨亿直采平台及其用户产生影响的行为构成违约的，厨亿直采可依据相应规则对您的账号执行限制参加活动、中止向您提供部分或全部服务等处理措施。如您的行为构成根本违约的，厨亿直采可查封您的账号，终止向您提供服务。 \r\n" +
               "(三) 如果您在厨亿直采平台上的行为违反相关的法律法规，厨亿直采可依法向相关主管机关报告并提交您的使用记录和其他信息。 \r\n" +
               "第三条 如您的行为使厨亿直采及或其关联公司遭受损失（包括自身的直接 经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失），您应赔偿厨亿直采及/或其关联公司的上述全部损失。如您的行为使厨亿直采或其关联公司遭受第三人主张权利，厨亿直采及或其关联公司可在对第三人承担金钱给付等义务后就全部损失向您追偿。 \r\n" +
               "十、协议的终止 \r\n" +
               "第一条 用户有权通过以下任一方式终止本协议： \r\n" +
               "（一）在满足厨亿直采平台公示的账号注销条件时您通过厨亿直采平台注销您的账号的； \r\n" +
               "（二）变更事项生效前您停止使用并明示不愿接受变更事项的； \r\n" +
               "（三）您明示不愿继续使用厨亿直采平台服务，且符合厨亿直采平台终 止条件的。 \r\n" +
               "第二条、出现以下情况时，厨亿直采可以通知您终止本协议： \r\n" +
               "（一）您违反本协议约定，厨亿直采依据违约条款终止本协议的； \r\n" +
               "（二）您盗用他人账号、发布违禁信息、骗取他人财物、扰乱市场秩序、采取不正当手段谋 利等行为，厨亿直采依据厨亿直采平台规则对您的账号予以查封的； \r\n" +
               "（三）除上述情形外，因您多次违反厨亿直采平台规则相关规定且情节严重，厨亿直采依据厨亿直采平台规则对您的账号予以查封的； \r\n" +
               "（四）您的账号被厨亿直采依据本协议进行注销等清理的； \r\n" +
               "（五）您在厨亿直采平台有侵犯他人合法权益或其他严重违法违约行为的； （六）其它根据相关法律法规厨亿直采应当终止服务的情况。 \r\n" +
               "第十九条 本协议终止后，除法律有明确规定外，原则上，厨亿直采无义务向您或您指定的第三方披露您账号中的任何信息。 \r\n" +
               "第二十条 本协议终止后，厨亿直采仍享有下列权利： \r\n" +
               "（一）厨亿直采可根据适用法律的要求删除您的个人信息，或使其匿名化处理。 也可依照法律规定的期限和方式继续保存您留存于厨亿直采平台的其他内容和信 息。 \r\n" +
               "（二）对于您过往的违约行为，厨亿直采仍可依据本协议向您追究违约责任。 十一、责任范围 \r\n" +
               "第一条 厨亿直采依照法律规定履行基础保障义务，但对于下述原因导 致的协议履行障碍、履行瑕疵、履行延后或履行内容变更等情形，厨亿直采并不 承担相应的违约责任： \r\n" +
               "（一）因自然灾害、罢工、暴乱、战争、政府行为、司法行政命令等不可抗力因素； \r\n" +
               "（二）因电力供应故障、通讯网络故障等公共服务因素或第三人因素； \r\n" +
               "（三）在厨亿直采已尽善意管理的情况下，因常规或紧急的设备与系统维护、设备与系统故障、网络信息与数据安全等因素。 \r\n" +
               " 第二条 附则本协议之订立、生效、解释、修订、补充、终止、执行与争议解决均适用中华人民共和国法律；如法律无相关规定的，参照商业惯例及/或行业惯例。 \r\n" +
               "第三条 您因使用厨亿直采平台服务所产生及与厨亿直采平 台服务有关的争议，由厨亿直采与您协商解决。协商不成时，任何一方均可向被 告所在地人民法院提起诉讼。本协议任一条款被视为废止、无效或不可执行，该条应视为可 分的且并不影响本协议其余条款的有效性及可执行性。 \r\n" +
               "第四条 如您对本服务协议有任何问题或建议，可通过厨亿直采客服与 我们取得联系。\r\n");
    }
}