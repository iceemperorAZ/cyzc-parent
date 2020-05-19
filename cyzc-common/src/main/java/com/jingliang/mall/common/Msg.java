package com.jingliang.mall.common;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 常量定义
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */
public interface Msg {
    Integer PAY_GOLD_OK = 210;

    Integer OK = 200;
    String TEXT_OK = "请求成功";
    String TEXT_LOGIN_OK = "登录成功";
    String TEXT_LOGOUT_OK = "注销成功";
    String TEXT_SAVE_OK = "保存成功";
    String TEXT_SHOW_UP_OK = "商品上架成功";
    String TEXT_SHOW_DOWN_OK = "商品下架成功";
    String TEXT_UPDATE_OK = "修改成功";
    String TEXT_CANCEL_OK = "取消成功";
    String TEXT_CONFIRM_OK = "订单完成";
    String TEXT_DELETE_OK = "删除成功";
    String TEXT_QUERY_OK = "查询成功";
    String TEXT_AUTHORIZE_OK = "授权成功";
    String TEXT_RECOVERY_AUTHORITY_OK = "权限回收成功";
    String TEXT_ORDER_OK = "下单成功";
    String TEXT_PAY_OK = "支付成功";
    String TEXT_RELEASE_OK = "发布成功";
    String TEXT_RECALL_OK = "撤回成功";

    Integer FAIL = 300;
    String TEXT_LOGOUT_FAIL = "注销失败";
    String TEXT_BUYER_FAIL = "绑定销售不存在";
    String TEXT_BUYER_REPEAT_FAIL = "该微信号已被其他销售绑定";
    String TEXT_OLD_PASSWORD_FAIL = "原密码不正确";
    String TEXT_PHONE_FAIL = "手机号格式错误";
    String TEXT_COUPON_RELEASE_FAIL = "发布失败，优惠券已过期或不可领取";
    String TEXT_COUPON_SAVE_FAIL = "保存失败，优惠券时间段选择不符合逻辑";
    String TEXT_IS_AVAILABLE_FAIL = "组不可用";

    Integer PARAM_FAIL = 310;
    String TEXT_PARAM_FAIL = "参数不全";
    String TEXT_PARAM_FORMAT_FAIL = "参数值格式错误";
    String TEXT_PARAM_VALUE_FAIL = "参数取值错误";

    Integer IMAGE_FAIL = 320;
    String TEXT_IMAGE_FAIL = "图片格式无法识别";

    Integer SAVE_FAIL = 330;
    String TEXT_SAVE_FAIL = "保存失败";
    String TEXT_SKU_NUM_FAIL = "超出实际库存数量";
    String TEXT_CART_ITEM_NUM_FAIL = "购物车中商品数量必须大于0";

    Integer DATA_FAIL = 340;
    String TEXT_DATA_FAIL = "数据不存在";
    String TEXT_BUYER_DATA_FAIL = "会员不存在";
    String TEXT_ORDER_DATA_FAIL = "订单不存在";
    String TEXT_DATA_REPEAT_FAIL = "数据重复";
    String TEXT_USER_DATA_FAIL = "用户不存在";

    Integer ORDER_FAIL = 350;
    String TEXT_ORDER_FAIL = "下单失败";
    String TEXT_ORDER_NOT_EXIST_FAIL = "修改失败,当前订单不存在";
    String TEXT_ORDER_PRODUCT_FAIL = "下单失败,商品已下架";
    String TEXT_ORDER_SKU_FAIL = "下单失败,库存不足";
    String TEXT_ORDER_DELIVER_SKU_FAIL = "发货失败,库存不足";
    String TEXT_ORDER_COUPON_FAIL = "下单失败,无效优惠券";
    String TEXT_ORDER_INSUFFICIENT_AMOUNT_FAIL = "下单失败,订单总金额最低金额不满#price#元";
    String TEXT_ORDER_AMOUNT_HIGHER_FAIL = "订单金额高于#price#元,请联系业务经理";

    Integer PAY_FAIL = 350;
    String TEXT_PAY_PAID = "支付失败,订单已支付";
    String TEXT_PAY_OVERTIME_FAIL = "支付失败,支付超时";
    String TEXT_PAY_FAIL = "支付失败";
    String TEXT_PAY_NOTHINGNESS_FAIL = "支付失败,订单不存在";

    Integer PRODUCT_FAIL = 360;
    String TEXT_PRODUCT_EXIST_FAIL = "添加失败，商品已存在";
    String TEXT_PRODUCT_SHOW_SKU_FAIL = "上架失败，存在库存不足商品";
    String TEXT_PRODUCT_SHOW_FAIL = "上架失败，存在未知商品";
    String TEXT_PRODUCT_HIDE_FAIL = "下架失败，存在未知商品";
    String TEXT_PRODUCT_DELETE_FAIL = "删除失败，存在已上架商品";
    String TEXT_PRODUCT_ORDER_FAIL = "删除失败，该商品存在未完成的订单";
    String TEXT_PRODUCT_UPDATE_FAIL = "修改失败，已上架商品不允许修改";
    String TEXT_PRODUCT_SKU_FAIL = "删除失败，商品库存大于0";

    Integer COUPON_FAIL = 370;
    String TEXT_COUPON_RECEIVE_FAIL = "领取失败，优惠券不能重复领取";
    String TEXT_COUPON_INVALID_FAIL = "领取失败，优惠券已失效";
    String TEXT_COUPON_ROB_FAIL = "领取失败，优惠券已被抢空";

    Integer ADDRESS_FAIL = 380;
    String TEXT_ADDRESS_NOT_EXIST_FAIL = "修改失败，当前修改地址不存在";

    Integer LOGIN_FAIL = 410;
    String TEXT_LOGIN_FAIL = "用户名或密码不正确";
    String TEXT_WECHAT_LOGIN_FAIL = "微信登录失败";
    String TEXT_IS_SEAL_UP_FAIL = "账号被封停";
    String TEXT_NOT_LOGIN_FAIL = "请先登录";

    Integer LOGIN_LIMIT_FAIL = 420;
    String TEXT_LIMIT_FAIL = "用户名密码多次输入错误，已限制您的登录";

    Integer TOKEN_FAIL = 430;
    String TEXT_TOKEN_INVALID_FAIL = "请重新登录";

    Integer AUTHORITY_FAIL = 440;
    String TEXT_AUTHORITY_FAIL = "权限不足";

    Integer WECHAT_FAIL = 450;
    String TEXT_WECHAT_SESSION_KEY_TIMEOUT_FAIL = "微信sessionKey过期";

    Integer PASSWORD_FAIL = 460;
    String TEXT_PASSWORD_INIT_FAIL = "当前账户密码为初始密码,请修改当前密码";
    String TEXT_MODIFY_PASSWORD_UNSAFE_FAIL = "密码安全度过低,请重新设置当前密码";

    Integer FILE_FAIL = 470;
    String TEXT_EXCEL_DOWNLOAD_FAIL = "excel下载异常";
    String TEXT_EXCEL_UPLOAD_FAIL = "excel上传异常";
    String TEXT_EXCEL_ANALYSIS_FAIL = "excel解析异常";

    Integer SYSTEM_FAIL = 500;
    String TEXT_SYSTEM_FAIL = "当前访问人数过多，请稍后重试！";

    Integer REQUEST_FAIL = 501;
    String TEXT_REQUEST_FAIL = "请求方式错误，当前请求方式为[#nowReq#],实际支持请求方式为[#req#]";

    List<String> orderExcelTitle = Lists.newArrayList("单据日期", "单据编号", "客户编号", "客户名称", "销售人员"
            , "优惠金额", "客户承担费用", "本次收款", "结算账户", "单据备注", "商品编号", "商品名称", "商品型号", "属性",
            "单位", "数量", "单价", "折扣率%", "折扣额", "金额", "税率%", "仓库", "收货地址", "收货人", "电话", "备注");

    List<String> buyerCountsToUserExcelTitle = Lists.newArrayList("时间", "新增数量", "销售员ID", "销售员", "区域经理", "区域");

    List<String> offlineOrderExcelTitle = Lists.newArrayList("客户要求送货日期和时间", "商品名称", "商品规格", "单位", "数量", "单价(元)", "总价", "商铺名称", "客户姓名", "客户电话", "客户地址", "业务员姓名", "业务员电话", "备注");
    List<String> offlineOrderExcelTitle2 = Lists.newArrayList("商铺名称", "客户姓名", "客户电话", "商品名称", "商品规格", "单位", "数量", "单价(元)", "总价(元)", "省", "市", "区/县", "客户地址", "客户要求送货日期和时间", "业务员Id", "业务员姓名", "业务员工号", "业务员电话", "备注", "创建时间", "开具发票进度", "发票类型", "单位名称", "纳税人识别码", "注册地址", "注册电话", "开户银行", "银行账号", "快递联系人", "联系电话", "快递地址");
    List<String> buyerExcelTitle = Lists.newArrayList("主键ID", "手机号", "注册时间");
    String EXEMPTION = "\n" +
            "厨亿直采服务协议\n" +
            "本《厨亿直采服务协议》(下称“协议”)是您与上海晶粮贸易有限公司(下称“厨亿直采”)的经营者之间就厨亿直釆服务等相关事宜所订立的契约。\n" +
            "厨亿直采软件包括厨亿直采小程序等(以下合称“本站”)。请您在使用厨亿直采产品或者服务前仔细阅读本用户协议，特别是免除或限制您责任的条款以及争议解决条款等，在充分理解并同意后使用相关产品或者服务。如您不同意本协议或隐私政策中任何条款的约定，您应立即停止注册、使用厨亿直釆的软件和服务。一旦您开始使用厨亿直采任何产品或者服务，即表示您已充分理解并同意本用户协议的全部内容，本用户协议即成为您与厨亿直采间具有约束力的法律文件。\n" +
            "请您注意，我们会不时地检查并更新用户协议，因此有关用户协议的内容会随之变化。我们请您定期查看用户协议页面，以确保您了解用户协议的最新版本。如果您在阅读完最新版用户协议后继续使用厨亿直采任何产品或者服务，即视为您已充分理解并同意我们对用户协议的更新修改。\n" +
            "本协议内容包括本协议正文以及所有厨亿直采公开生效的适用于所有用户的各类服务条款及规则，该等服务条款与规则为本协议不可分割的组成部分，与本协议具有同等法律效力。\n" +
            "一、用户注册登录\n" +
            "指用户注册登录厨亿直采，按要求填写相关信息并确认同意履行相关用户协议的过程。用户因进行交易、获取有偿服务等而发生的所有费用及相关税费由用户自行承担。\n" +
            "用户在注册成为厨亿直采用户时提供的信息应真实完整、有效，并保证厨亿直采移动应用平台可以通过该信息与用户本人进行联系。同时，用户应在相关资料发生变更时及时更新注册信息。在成功注册后，厨亿直采会为每位用户开通一个账户，用于已注册用户登录厨亿直采并享受该平台服务，用户应妥善保管该账户的用户名和密码，并对在其账户下发生的所有活动承担责任。已注册用户登录厨亿直釆并享受该平台服务，用户应妥善保管该账户的用户名和密码，并对在其账户下发生的所有活动承担责任。\n" +
            "厨亿直采用户必须是具有完全民事行为能力的自然人，或者是具有合法经营资格的实体组织。无民事行为能力人、限制民事行为能力人以及无经营或特定经营资格的组织不得注册为厨亿直采移动应用平台用户。对于不符合注册条件的用户，厨亿直采有权立即停止与该用户的交易、注销登录账户，并由该用户自行承担由此产生的法律风险与责任。\n" +
            "二、商品交易\n" +
            "1.商品信息\n" +
            "厨亿直釆标示的商品价格、数量、是否有货等商品信息根据实际情况随时都有可能发生变动，本站不作特别通知。由于厨亿直釆上商品信息的数量极其庞大本平台将会尽最大努力保证用户所购线上商品与对应实体门店公布的价格一致，但价目表和声明并不构成要约。厨亿直釆有权在发现线上标示的产品信息存在明显错误或已生成的订单处于缺货状态的情况下单方面予以修改或取消该订单。但由于众所周知的网络技术因素等客观原因导致的线上信息滞后或差错的，希望用户对此情形予以理解。为表述便利，商品和服务简称为“商品”或“货物”。\n" +
            "2.商品促销\n" +
            "2.1用户同意厨亿直采有权在提供本服务过程中自行或由第三方广告商向用户发送广告、推广或宣传促销信息(包括商业与非商业信息，其方式和范围无须向用户特别通知。\n" +
            "2.2厨亿直采有权为用户提供选择关闭广告信息的功能。\n" +
            "2.3在接受本服务过程中，用户将自行判断广告信息的有效性并为自己的判断行为负责。\n" +
            "2.4由于市场变化等因素的影响，本平台无法保证您接受的商场促销信息都为有效内容，实际情况以实体门店厨亿直采发布的促销信息为准。\n" +
            "3.商品订单\n" +
            "厨亿直釆保留对产品订购数量的限制权。在提交订单的同时，用户也同时承认了其拥有购买这些产品的权利能力和行为能力，并且对订单中提供的所有信息的真实性负责。由于市场变化等因素的影响，厨亿直釆无法保证用户提交的订单商品都会有货；如用户拟购买的商品发生缺货，则厨亿直采有权取消订单，此种情形用户下单行为并不视为双方买卖合同关系成立。4.商品提货\n" +
            "商品提货仅适用于用户所订购的厨亿直采商品，厨亿直采将在您指定的门店进行备货。所有在厨亿直采界面上标示的订单状态为参考状态，供用户参照使用。参考状态的计算是根据库存状况、正常的处理过程的基础上估计得出的。如用户需变更提货时间，应在厨亿直采通过客服电话及时通知；如果用户需他人代领商品(不建议代领)请让代领人出示“其他支付凭\n" +
            "证”,由于“其他支付凭证”是领商品的必要凭证，请妥善保管“其他支付凭证”,否则，用户自行承担货物被假冒代领的法律风险与损失。\n" +
            "5.商品售后\n" +
            "用户在小程序交易后发生的售后问题，适用此规则。\n" +
            "厨亿直采将通过系统通知、页面信息提示、短信或电话等方式向用户发送与售后处理相关的提示或通知构成售后处理依据及结论的有效组成部分。\n" +
            "5.1售后受理时效\n" +
            "(1)用户下单后，厨亿直采如发现库存不足时，应及时告知用户并及时向用户退款。\n" +
            "(2)不支持无理由退换货的商品发生质量问题或错漏发时应在收到商品之时起及时提出退换货申请。\n" +
            "(3)实行国家“三包政策”的商品如出现国家法律规定的功能性障碍或质量问题，支持消费者有权利在三包期间，商品损坏的时候要求商家免费修理或者换新的服务。\n" +
            "(4)超出本规则规定的售后时限发起的退换货申请无法受理。\n" +
            "5.2售后受理范围\n" +
            "(1)当用户购买的商品发生质量问题、商品错发/漏发商品破损、过期、描述不符等问题时，支持退换货申请。\n" +
            "(2)参加限时抢购、换购、特价促销(清仓)的商品出现质量问颎时因商品特性仅支持退货退款，暂不支持换货。\n" +
            "(3)因客户保管储藏不当、人为操作不当导致商品无法食用/使用，不支持退换货。\n" +
            "5.2处理规则\n" +
            "5.2.1商品质量问题、假冒商品认定\n" +
            "(1)若商品发生质量问题，系肉眼可识别的(如发霉、腐烂：用户应在有效售后时限内反馈并提供相应凭证(图片或视频),用户无法提供凭证的，不支持退换货。(2)用户反馈商品质量问题系肉眼不可见或系假冒商品的：\n" +
            "A、用户应提供有效的质检凭证或其他有效凭证。用户无法提供的，质量问题或假冒商品认定无效；\n" +
            "B、用户已提供相关凭证，厨亿直采将通过合理解释或提供厂家的经销凭证、报关单据(进口商品)、产品合格证、执行标准等相关凭证以证明商品来源或出合规。\n" +
            "C、若确认岀售的商品为质量问题，提供退货退款服务；若确认出售的商品系假冒商品，用户基于生活消费所需购买的，提供退货退款服务(生活消费所需的认定：厨亿直釆将依照普通人的认知水准及日常经验判断，或基于概率学和大数据技术，从交易主体、交易信息、交易行为等多个维度进行排查、综合判断用户是否属生活消费所需购买。)\n" +
            "(3)用户应保管申请退换货的商品，如有必要，厨亿直釆将联系用户上门取回商品后进行退货退款处理。\n" +
            "5.2.2描述不符问题处理规范\n" +
            "(1)用户申请售后的商品存在描述不当系肉眼可识别的，用户应提供凭证予以证明，用户无法提供凭证的，不支持退换货。用户已提供凭证，厨亿直釆针对用户给出的凭证确认描述不符属实的，不提供退货退款服务。\n" +
            "(2)商品的描述不当系无需使用，肉眼即可显著识别的，若商品完好，支持退货退款；若用户已经使用且影响商品完好的，不支持退货退款。\n" +
            "(3)生鲜类商品(新鲜蔬果、肉禽鱼类)存在重量短缺情况时，按短缺部分作相应比例部分退款处理。\n" +
            "5.2.3签收问题处理规范\n" +
            "(1)用户填写的订单收件信息应真实、详细、准确、有效。因提供的收件信息错误导致延迟或者未收到商品的用户需承担该责任限度内的不利后果。\n" +
            "(2)订单发货后，收件人应亲自签收商品。收件人委托他人签收商品或已按收件人的指示将商品置于指定地点的，视为收件人本人签收。\n" +
            "(3)用户无正当理由拒绝签收不支持无理由退货且性质不适宜拒签的商品，商品返回门店后，若商品性质已变化，影响二次销售，用户应承担由此产生的损失\n" +
            "5.2.4其他处理细则\n" +
            "(1)当用户下单成功之后会返利一定的金币，若用户购买的商品需办理退款时，需将商返利的金币一并扣除。当此账户金币数不满足退币的数量时，会在实际退款时需扣除相应金额。\n" +
            "2)办理整单退货退款时，已开具的发票将进行自动红冲；办理部分退货退款时，系统将根据退货后的有效商品金额自动重新开具新的发票，原发票自动红冲。\n" +
            "(3)办理整单退货退款时，已使用的积分将按比例退还，优惠券支持退回，第三方支付优惠不支持退回。因质量问题发生的退货退款，优惠券退回日期若超过券的限用日期，支持补发。\n" +
            "5.3附则\n" +
            "(1)厨亿直釆将根据相关法律法规、市场情况、业务规则对本规则进行不定期修改、完善，并将通过厨亿直采小程序在适当位置予以展示。\n" +
            "(2)本规则生效或变更后尚未处理完结的交易，适用生效或变更后的规则。\n" +
            "6.积分部分\n" +
            "用户在厨亿直采注册时即生效为厨亿直采普通会员参照《厨亿直釆会员权益》积分规则享受积分优惠。\n" +
            "三、合作伙伴\n" +
            "厨亿直釆只选择信誉良好的餐饮公司或本身即是上海晶粮贸易有限公司特约商户的公司为合作伙伴(以下简称合作伙伴),当用户通过厨亿直采获得合作伙伴的优惠服务时，请事先详阅优惠细则，因用户与合作伙伴产生的任何交易、服务发生纠纷时，应由用户与该合作伙伴自行协商解决，厨亿直釆不承担任何责任。厨亿直釆第三方场景使用，以下或简称“第三方场景使用功能”,是指在您同意本协议的前提下，您可以在第三方服务商的APP、手机厂商系统的软硬件(以下简称“第三方场景”)内使用厨亿直釆消费购物功能及服务。在开通第三方场景使用功能之前，您应当注意以下事项并承诺：您有意愿在第三方场景中使用厨亿直采购物车服务；同意第三方场景提供方将相关信息提供、同步给厨亿直釆或厨亿直釆将相关信息提供、同步绐第三方场景提供方；您认可第三方场景的软硬件安全管控机制，将按照其与您的协议约定履行用户安全注意义务\n" +
            "1.在开通第三方场景使用功能之后，您理解并认可如下服务：\n" +
            "(1)可在第三方场景中查看到厨亿直采购物服务以及相关信息，包括厨亿直采账号(邮箱/手机)、头像、昵称、订单信息等。\n" +
            "(2)可在第三方场景中使用购物服务，包括信息查询位置服务、第三支付服务等，以及由此引起与第三方场景之间的信息同步。\n" +
            "(3)可在厨亿直采客户端注销厨亿直采帐号，注销后您将无法在第三方场景中查看、使用厨亿直釆购物功能、查询订单信息或开具发票。\n" +
            "2.在使用第三方场景功能时，您理解并认可以下事项\n" +
            "(1)第三方服务商是第三方场景的独立运营方并承担责任。您应当妥善使用、保管第三方场景的软硬件如发生第三方场景软硬件的软件故障、硬件丢失、系统安全漏洞等情形，将可能导致您产生经济损失。出现前述情形，请您及时联系第三方服务商沟通解决。\n" +
            "(2)第三方场景软硬件的技术、系统、网络等方面的稳定性，可能会造成您使用购物服务时，出现页面迟延、信息同步不及时或无法使用的情况，请您耐心等待或在厨亿直采客户端内使用购物服务。第三方场景使用功能实现以厨亿直采与第三方服务商的合作为基础，若厨亿直釆与第三方服务商终止合作，第三方场景使用功能将自动终止。厨亿直釆可以视第三方场景使用的用户体验情况，对产品功能进行升级、迭代更新或终止、下线。\n" +
            "四、用户权利与义务\n" +
            "用户有权拥有其在厨亿直采的用户名及密码，并有权使用自己的用户名及密码随时登录厨亿直采，同时用户可以使用用户信息通过厨亿直釆客服中心进行购买行为。用户不得以任何形式转让或授权他人使用自己的厨亿直采用户名，否则由此产生的一切后果由用户自行承担。\n" +
            "用户有权根据本协议的规定以及厨亿直釆上发布的相关规则在厨亿直釆上查询商品信息、发表使用体验、参与商品讨论、邀请关注好友、合法上传商品图片发表社区言论等参加厨亿直采的有关活动以及有权享受厨亿直采提供的其它的有关信息服务。\n" +
            "用户应当保证在使用厨亿直采购买商品过程中遵守诚实信用原则，不在购买过程中采取不正当行为，不扰乱平台上交易的正常秩序。\n" +
            "用户不得在厨亿直采发表包含以下内容的言论，否则厨亿直采有权随时删除相关言论。\n" +
            "(1)\t煽动、抗拒、破坏宪法和法律、行政法规实施的；\n" +
            "(2)\t(2)煽动颠覆国家政权，推翻社会主义制度的；\n" +
            "(3)煽动、分裂国家，破坏国家统一的；\n" +
            "(4)煽动民族仇恨、民族歧视，破坏民族团结的；\n" +
            "(5)任何包含对种族、性别、宗教、地域内容等歧视的；\n" +
            "(6)捏造或者歪曲事实，散布谣言，扰乱社会秩序的；\n" +
            "(7)宣扬封建迷信、淫秽、色情、赌博、暴力、凶杀、恐怖、教唆犯罪的；\n" +
            "(8)公然侮辱他人或者捏造事实诽谤他人的，或者进行其他恶意攻击的；\n" +
            "(9)损害国家机关信誉的\n" +
            "(10)其他违反宪法和法律行政法规的。\n" +
            "用户在发表使用体验、讨论图片等，除遵守本条款外，还应遵守厨亿直采的相关规定。未经厨亿直采同意，用户不得在网站发布任何形式的广告。否则，厨亿直采移动应用管理平台有权随时予以删除。\n" +
            "五、厨亿直采的权利和义务\n" +
            "厨亿直采有义务在现有技术上维护整个交易平台的正常运行，并努力提升和改进技术，使用户在平台上交易活动得以顺利进行。\n" +
            "对用户在注册使用厨亿直采交易平台中所遇到的与交易或注册有关的问题及反映的情况，厨亿直采将及时作出回复。\n" +
            "对于用户在厨亿直采上的不当行为或其它任何厨亿直采认为应当终止服务的情况，厨亿直采有权随时作出删除相关信息、终止提供服务等处理，而无须征得用户的同意。\n" +
            "用户在此授予厨亿直采独家的、全球通用的、永久的、免費的许可使用权利(并有权对该权利进行再授权)使厨亿直采全部或部分地使用、复制、修订、改写、发布、翻译、分发、执行和展示用户公示于厨亿直采的各类信息或制作其派生作品，或以现在已知或日后开发的任何形式、媒体或技术，将上述信息纳入其它作品内。\n" +
            "六、隐私保护\n" +
            "厨亿直采注重保护用户的个人信息及个人隐私。一旦完成厨亿直采的注册动作，您的注册信息即受到本用户协议保护。厨亿直釆不会向任何无关第三方提供、出售出租、分享或交易您的个人信息，除非事先得到您的许可。但是，厨亿直釆可能会向合作伙伴等第三方共享你的个人信息，以保障为您提供的服务顺利完成，并且改善和向用户提供更好的服务，但厨亿直釆仅限于合法、正当、必要、特定、明确的目的共享您的个人信息。厨亿直的合作伙伴为商品和技术服务供应商，厨亿直釆可能会将与您的个人信息有关的用户数据共享给支持厨亿直釆功能的的第三方进行分析和优化；这些支持包括为厨亿直釆提供商品推荐、优化厨亿直采铺货策略等。同时，您应当遵守法律法规并谨慎合理地使用和妥善保管厨亿直釆账号及密码并对本人专属之账号和密码项下所有行为和事件负责，当用户发现厨亿直釆账号被未经其授权的第三方使用或存在其他账号安全问题时应立即通知上海晶粮贸易有限公司，要求暂停该账号的服务，并向公安机关报案。上海晶粮贸易有限公司有权在合理时间内对用户的该类请求釆取行动，但对采取行动前用户已经遭受的损失不承担任何责任。\n" +
            "隐私保护的适用范围\n" +
            "1.在您注册厨亿直釆账户时，您根据厨亿直采要求提供的个人注册信息。\n" +
            "2.在您使用厨亿直采服务时，厨亿直采自动接收并记录您的手机软硬件信息，包括但不限于您的手机软件版本号、机型信息、系统版本号、访问日期和时间等数据；预约自提和预约配送需要用户选择希望配送或自提的时间，用户需要提供收货人，收货地址，收货详情等，以便为用户提供服务。\n" +
            "3.基于履单需求，厨亿直采获取到您的相关信息，包括但不限于预期配送时间、预期自提时间、收货人信息、收货地址信息、收货详情等数据。\n" +
            "4.厨亿直采通过合法途径从商业伙伴处取得的用户个人数据。\n" +
            "5.您了解并同意，以下信息不适用本隐私权政策。\n" +
            "5.1您在使用厨亿直釆提供的搜索服务时输入的关键字信息。\n" +
            "5.2厨亿直釆收集到您在本应用进行交易的有关数据，包括但不限于成交信息及评价详情。\n" +
            "5.3信用评价、违反法律规定或违反厨亿直釆规则行为及厨亿直釆已对您釆取的措施。\n" +
            "七、厨亿直釆用户协议的变更、终止、责任限制\n" +
            "1.协议的变更\n" +
            "用户认可，厨亿直采有权自行变更本条款、其它服务条款和条件或用户的会员资格条款。对这些条款的任何修改将被包含在厨亿直采更新的条款中。如果任何变更被认定为无效、废止或因任何原因不可执行，则该变更是可分割的，且不影响其它变更或条件的有效性或可执行性。在厨亿直采变更相关条款后，用户仍继续使用原有账户，视为对上述变更的接受。\n" +
            "2.协议的终止\n" +
            "用户认可，厨亿直釆可以不经通知而自行决定终止全部或部分服务，包括终止用户的会员资格。即使厨亿直釆没有要求或强制您严格遵守这些条款，也并不构成对属于厨亿直釆的任何权利的放弃。如果用户在厨亿直采的客户账户被关闭，那么该用户也将丧失会员资格，对此，用户无权对厨亿直釆主张任何权利或赔偿、补偿。\n" +
            "3.协议的责任限制\n" +
            "除了厨亿直釆的使用条件中规定的其它限制和除外情况之外，在中国法律法规所允许的限度內，对于因会员制而引起的或与之有关的任何直接的、间接的、特殊的、附带的、后果性的或恁罚性的损害，或任何其它性质的损害，厨亿直采、上海晶粮贸易有限公司及其关联公司、分支机构的董事、管理人员、雇员、代理或其它代表在任何情况下都不承担责任。\n" +
            "八、服务的中断和终止\n" +
            "用户认可，厨亿直采有权随时修改或中断服务而不需通知用户，且不需对用户或任何第三方承担任何责任。用户对本协议的修改有异议，或对厨亿直采的服务不满，可以行使如下权利\n" +
            "(1)停止使用厨亿直采的网络服务；\n" +
            "(2)通过客服等渠道要求厨亿直采停止对其服务。\n" +
            "九、适用的法律与管辖权\n" +
            "用户与厨亿直采之间的协议将适用中华人民共和国的法律，所有的争端将诉诸于厨亿直采服务器或控制台终端所在地的人民法院。\n" +
            "十、其他\n" +
            "如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。\n";
}
