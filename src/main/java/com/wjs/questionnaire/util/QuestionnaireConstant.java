package com.wjs.questionnaire.util;

public final class QuestionnaireConstant {

    /**
     * 该对象存在
     */
    public static final int OBJECT_EXISTENCE = 1;

    /**
     * 该对象不存在
     */
    public static final int OBJECT_INEXISTENCE = 0;

    /**
     * 表示响应成功，用户的操作是正确的
     */
    public static final int SUCCESS = 200;

    /**
     * 表示响应失败，用户的操作是错误的
     */
    public static final int ERROR = 404;

    /**
     * 操作成功
     */
    public static final int OPERATE_SUCCESS = 1;

    /**
     * 操作失败
     */
    public static final int OPERATE_UNSUCCESS = -1;

    /**
     * 用户状态：已注销
     */
    public static final int USERSTATUS_CANCELED = -1;

    /**
     * 用户状态：未激活
     */
    public static final int USERSTATUS_INACTIVE = 0;

    /**
     * 用户状态：已激活
     */
    public static final int USERSTATUS_ACTIVATED = 1;

    /**
     * 用户类型：普通用户
     */
    public static final int USERTYPE_NORMALUSER = 0;

    /**
     * 用户类型：超级管理员
     */
    public static final int USERTYPE_ADMINISTRATORSUSER = 1;

    /**
     * 在线用户状态：在线用户ID
     */
    public static final String ONLINEUSERID = "OnlineUserID";

    /**
     * 在线问卷状态：在线问卷ID
     */
    public static final String OnlineQNID = "OnlineQNID";

    /**
     * 在线问题状态：在线问题ID
     */
    public static final String OnlineQID = "OnlineQID";

    /**
     * 在线选项状态：在线选项ID
     */
    public static final String OnlineOID = "OnlineOID";

}
