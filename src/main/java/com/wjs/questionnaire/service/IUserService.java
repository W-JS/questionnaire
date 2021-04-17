package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.UserEntity;

import java.util.Date;
import java.util.List;

/**
 * 处理用户信息数据的业务层接口
 */
public interface IUserService {

    /**
     * 获取所有用户信息列表
     *
     * @return 用户信息列表
     */
    List<UserEntity> getAllUserList();

    /**
     * 根据 userId 查找 User
     *
     * @param userId 用户ID
     * @return User
     */
    UserEntity getUserByUserId(String userId);

    /**
     * 根据 userName 查找 User
     *
     * @param userName 用户名
     * @return User
     */
    UserEntity getUserByUserName(String userName);

    /**
     * 根据 userPhone 查找 User
     *
     * @param userPhone 手机号
     * @return User
     */
    UserEntity getUserByUserPhone(String userPhone);

    /**
     * 根据 userEmail 查找 User
     *
     * @param userEmail 电子邮箱
     * @return User
     */
    UserEntity getUserByUserEmail(String userEmail);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 是否注册成功
     */
    int addUser(UserEntity user);

    /**
     * 激活用户，修改用户状态为 1
     *
     * @param userStatus 用户状态
     * @param userId     用户ID
     * @return 用户状态是否修改成功
     */
    int activateUser(Integer userStatus, String userId);

    /**
     * 修改用户最后一次登录时间
     *
     * @param userLastLoginTime 用户最后一次登录时间
     * @param userId            用户ID
     * @return 用户最后一次登录时间是否修改成功
     */
    int modifyLastLoginTime(Date userLastLoginTime, String userId);


}
