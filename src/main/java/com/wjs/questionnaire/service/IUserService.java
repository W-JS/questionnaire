package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;

/**
 * 处理用户信息数据的业务层接口
 */
public interface IUserService {

    /**
     * @return JSON格式数据：所有用户信息
     */
    JSONResult getAllUserList();

    /**
     * 判断用户名/手机号码/电子邮箱是否存在
     *
     * @param loginMethod user_name/user_phone/user_email
     * @param userLog     用户名/手机号码/电子邮箱
     * @return 是否存在用户名/手机号码/电子邮箱
     */
    JSONResult getUserLogExists(String loginMethod, String userLog);

    /**
     * 判断登录密码是否正确
     *
     * @param userId      用户ID
     * @param passwordLog 登录密码
     * @return 登录密码是否正确
     */
    JSONResult getPasswordLogExists(String userId, String passwordLog);

    /**
     * 用户登录
     *
     * @param userLastLoginTimeLog 用户最后一次登录时间
     * @param userId               用户ID
     * @return 用户是否登录成功
     */
    JSONResult getLoginSubmit(String userLastLoginTimeLog, String userId);

    /**
     * 生成通用唯一标识符
     *
     * @return 用户ID（userId）
     */
    String GeneratorUserID();

    /**
     * 判断用户名是否存在
     *
     * @param usernameReg 用户名
     * @return 是否存在用户名
     */
    JSONResult getUsernameRegExists(String usernameReg);

    /**
     * 判断手机号是否存在
     *
     * @param phoneReg 手机号
     * @return 是否存在手机号
     */
    JSONResult getPhoneRegExists(String phoneReg);

    /**
     * 判断电子邮件是否存在
     *
     * @param emailReg 电子邮件
     * @return 是否存在电子邮件
     */
    JSONResult getEmailRegExists(String emailReg);

    /**
     * 用户注册
     *
     * @param userId        用户编号
     * @param usernameReg   用户名
     * @param phoneReg      手机号
     * @param emailReg      电子邮箱
     * @param passwordReg   密码
     * @param sexReg        性别
     * @param birthdayReg   生日
     * @param statusReg     状态
     * @param typeReg       类型
     * @param createTimeReg 用户注册时间
     * @return 用户是否注册成功
     */
    JSONResult getRegisterSubmit(String userId, String usernameReg, String phoneReg, String emailReg, String passwordReg, String sexReg, String birthdayReg, int statusReg, int typeReg, String createTimeReg);

    /**
     * 激活账号
     *
     * @param userId 用户ID
     * @return 激活结果信息
     */
    String activate(String userId);


    /**
     * 生成验证码，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    String getCodeReg(String userId, int codeLength);

    /**
     * 从Redis获取验证码，判断验证码是否正确
     *
     * @param code 验证码
     * @return 验证码是否正确
     */
    JSONResult getCodeExists(String userId, String code);

}
