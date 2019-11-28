package com.jingliang.mall.common;

/**
 * 常量定义
 *
 * @author Zhenfeng Li
 * @version 1.0.0
 * @date 2019-09-19 09:02:33
 */
public interface MallConstant {
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

    Integer FAIL = 300;
    String TEXT_LOGOUT_FAIL = "注销失败";
    String TEXT_BUYER_FAIL = "绑定销售不存在";
    String TEXT_BUYER_REPEAT_FAIL = "该微信号已被其他销售绑定";
    String TEXT_OLD_PASSWORD_FAIL = "原密码不正确";
    String TEXT_PHONE_FAIL = "手机号格式错误";

    Integer PARAM_FAIL = 310;
    String TEXT_PARAM_FAIL = "参数不全";

    Integer IMAGE_FAIL = 320;
    String TEXT_IMAGE_FAIL = "图片格式无法识别";

    Integer SAVE_FAIL = 330;
    String TEXT_SAVE_FAIL = "保存失败";
    String TEXT_SKU_NUM_FAIL = "超出实际库存数量";
    String TEXT_CART_ITEM_NUM_FAIL = "购物车中商品数量必须大于0";

    Integer DATA_FAIL = 340;
    String TEXT_DATA_FAIL = "数据不存在";
    String TEXT_BUYER_DATA_FAIL = "会员不存在";
    String TEXT_USER_DATA_FAIL = "用户不存在";


    Integer ORDER_FAIL = 350;
    String TEXT_ORDER_NOT_EXIST_FAIL = "修改失败,当前订单不存在";
    String TEXT_ORDER_PRODUCT_FAIL = "下单失败,商品已下架";
    String TEXT_ORDER_SKU_FAIL = "下单失败,库存不足";
    String TEXT_ORDER_COUPON_FAIL = "下单失败,优惠券不可用";

    Integer PAY_FAIL = 350;
    String TEXT_PAY_PAID = "支付失败,订单已支付";
    String TEXT_PAY_OVERTIME_FAIL = "支付失败,支付超时";
    String TEXT_PAY_FAIL = "支付失败";
    String TEXT_PAY_NOTHINGNESS_FAIL = "支付失败,订单不存在";


    Integer PRODUCT_FAIL = 360;
    String TEXT_PRODUCT_EXIST_FAIL = "商品添加失败，商品已存在";
    String TEXT_PRODUCT_SHOW_SKU_FAIL = "商品上架失败，存在库存不足商品";
    String TEXT_PRODUCT_SHOW_FAIL = "商品上架失败，存在未知商品";
    String TEXT_PRODUCT_HIDE_FAIL = "商品下架失败，存在未知商品";
    String TEXT_PRODUCT_DELETE_FAIL = "商品删除失败，存在已上架商品";
    String TEXT_PRODUCT_UPDATE_FAIL = "修改失败，已上架商品不允许修改";

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

    Integer TOKEN_FAIL = 430;
    String TEXT_TOKEN_INVALID_FAIL = "token失效";

    Integer AUTHORITY_FAIL = 440;
    String TEXT_AUTHORITY_FAIL = "权限不足";

    Integer WECHAT_FAIL = 450;
    String TEXT_WECHAT_SESSION_KEY_TIMEOUT_FAIL = "微信sessionKey过期";

    Integer SYSTEM_FAIL = 500;
    String TEXT_SYSTEM_FAIL = "系统内部异常";

    Integer REQUEST_FAIL = 501;
    String TEXT_REQUEST_FAIL = "请求方式错误，当前请求方式为[#nowReq#],实际支持请求方式为[#req#]";
}
