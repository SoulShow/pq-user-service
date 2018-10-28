package com.pq.user.utils;

/**
 * 用户常量类
 *
 * @author liutao
 */
public class ConstantsUser {
    /**
     * 1-正常  2-锁定
     */
    public static final int USER_STATUS_NORMAL = 1;
    public static final int USER_STATUS_LOCKED = 2;
    
    /**
     *  用户登录后使用SESSION_USER_ID_KEY记录用户id
     **/
    public static final String SESSION_USER_ID_KEY = "user_id";

    /**
     * 日志类型：1、重置密码， 2-修改密码, 3-修改用户信息 4-设置密码
     **/
    public static final Integer USER_MODIFY_TYPE_REST_PASSWORD = 1;
    public static final Integer USER_MODIFY_TYPE_PASSWORD = 2;
    public static final Integer USER_MODIFY_TYPE_USERINFO = 3;
    public static final Integer USER_MODIFY_TYPE_SET_PASSWORD = 4;


    /**
     * 会员积分状态: 0-正常；1-过期；2-冻结
     */
    public static final int USER_INTEGRAL_STATUS_NORMAL = 0;
    public static final int USER_INTEGRAL_STATUS_TIMEOUT = 1;
    public static final int USER_INTEGRAL_STATUS_FREEZE = 2;


    /**
     * 会员积分类型: 1：购物送积分，2：取消积分回滚_用户取消订单，3：订单取消积分回滚_系统取消订单，
     *             4：人工增加积分，5：退款扣减积分，6：购物抵扣积分，7：积分过期，8：人工减少积分
     */
    public static final int USER_INTEGRAL_TYPE_SHOPPING = 1;
    public static final int USER_INTEGRAL_TYPE_CANCEL_USER = 2;
    public static final int USER_INTEGRAL_TYPE_CANCEL_SYSTEM = 3;
    public static final int USER_INTEGRAL_TYPE_ADD_HAND = 4;
    public static final int USER_INTEGRAL_TYPE_REFUND = 5;
    public static final int USER_INTEGRAL_TYPE_COUNTERACT = 6;
    public static final int USER_INTEGRAL_TYPE_TIMEOUT = 7;
    public static final int USER_INTEGRAL_TYPE_SUBTRACT_HAND = 8;


    /**
     * 会员积分操作: 0-加积分；1-减积分；
     */
    public static final int USER_INTEGRAL_ADD = 0;
    public static final int USER_INTEGRAL_SUBTRACT = 1;


    /**
     * 会员积分操作方式: 0-自动；1-手动
     */
    public static final int USER_INTEGRAL_OPERATE_TYPE_AUTO = 0;
    public static final int USER_INTEGRAL_OPERATE_TYPE_HAND = 1;

    /**
     * 积分配置规则类型: 1-赠送规则；2-抵扣规则
     */
    public static final int USER_INTEGRAL_CONFIG_GIVE = 1;
    public static final int USER_INTEGRAL_CONFIG_COUNTERACT = 2;

}
