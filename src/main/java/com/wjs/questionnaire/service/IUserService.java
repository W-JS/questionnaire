package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * 处理用户信息数据的业务层接口
 */
public interface IUserService {


    /**
     * 设置分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    PageUtil setUserListPage(PageUtil page);

    /**
     * 获取所有普通用户信息列表
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 普通用户信息列表
     */
    List<Map<String, Object>> getUserList(int offset, int limit);

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
     * 判断密码是否正确
     *
     * @param userId   用户编号
     * @param password 密码
     * @return 密码是否正确
     */
    JSONResult getPasswordExists(String userId, String password);

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
     * 修改用户信息
     *
     * @param userId         用户编号
     * @param userName       用户名
     * @param userPhone      手机号
     * @param userEmail      电子邮箱
     * @param sex            性别
     * @param birthday       生日
     * @param userUpdateTime 个人信息修改时间
     * @return 用户信息是否修改成功
     */
    JSONResult getUpdateSubmit1(String userId, String userName, String userPhone, String userEmail, String sex, String birthday, String userUpdateTime);

    /**
     * 修改用户密码
     *
     * @param userId   用户编号
     * @param password 密码
     * @return 用户密码是否修改成功
     */
    JSONResult getUpdateSubmit2(String userId, String password);

    /**
     * 修改用户信息
     *
     * @param userId            用户编号
     * @param userName          用户名
     * @param userPhone         手机号
     * @param userEmail         电子邮箱
     * @param userSex           性别
     * @param userBirthday      生日
     * @param userStatus        状态
     * @param userCreateTime    注册时间
     * @param userUpdateTime    更新时间
     * @param userDeleteTime    注销时间
     * @param userLastLoginTime 最后一次登录时间
     * @return 用户信息是否修改成功
     */
    JSONResult getUpdateSubmit3(String userId, String userName, String userPhone, String userEmail, String userSex, String userBirthday, String userStatus, String userCreateTime, String userUpdateTime, String userDeleteTime, String userLastLoginTime);

    /**
     * 激活账号
     *
     * @param userId 用户ID
     * @return 激活结果信息
     */
    String activate(String userId);


    /**
     * 生成验证码，发送登录验证码邮件，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    JSONResult LoginCode(String userId, int codeLength);

    /**
     * 生成验证码，发送修改密码码邮件，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    JSONResult UpdatePasswordCode(String userId, int codeLength);

    /**
     * 从Redis获取验证码，判断验证码是否正确
     *
     * @param code 验证码
     * @return 验证码是否正确
     */
    JSONResult getCodeExists(String userId, String code);

    /**
     * 根据 userId 删除用户信息
     *
     * @param userId 用户编号
     * @return 用户信息是否删除成功
     */
    JSONResult getDeleteSubmit1(String userId);

    /**
     * 根据 userId 删除多个用户信息
     *
     * @param user JSON格式的字符串，包含多个用户编号
     * @return 用户信息是否删除成功
     */
    JSONResult getDeleteSubmit2(String user);

    /**
     * 根据 userId 得到用户信息
     *
     * @return 用户信息
     */
    JSONResult getUserByUserId(String userId);


    /**
     * 获取在线用户信息
     *
     * @return 在线用户信息
     */
    JSONResult getOnlineUser();

}
