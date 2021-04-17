package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取所有用户信息列表
     *
     * @return 用户信息列表
     */
    @Override
    public List<UserEntity> getAllUserList() {
        return userMapper.findAllUserList();
    }

    /**
     * 根据 userId 查找 User
     *
     * @param userId 用户ID
     * @return User
     */
    @Override
    public UserEntity getUserByUserId(String userId) {
        return userMapper.findUserByUserId(userId);
    }

    /**
     * 根据 userName 查找 User
     *
     * @param userName 用户名
     * @return User
     */
    @Override
    public UserEntity getUserByUserName(String userName) {
        return userMapper.findUserByUserName(userName);
    }

    /**
     * 根据 userPhone 查找 User
     *
     * @param userPhone 手机号
     * @return User
     */
    @Override
    public UserEntity getUserByUserPhone(String userPhone) {
        return userMapper.findUserByUserPhone(userPhone);
    }

    /**
     * 根据 userEmail 查找 User
     *
     * @param userEmail 电子邮箱
     * @return User
     */
    @Override
    public UserEntity getUserByUserEmail(String userEmail) {
        return userMapper.findUserByUserEmail(userEmail);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 是否注册成功
     */
    @Override
    public int addUser(UserEntity user) {
        return userMapper.insertUser(user);
    }

    /**
     * 激活用户，修改用户状态为 1
     *
     * @param userStatus 用户状态
     * @param userId     用户ID
     * @return 用户状态是否修改成功
     */
    @Override
    public int activateUser(Integer userStatus, String userId) {
        return userMapper.updateUserStatus(userStatus, userId);
    }

    /**
     * 修改用户最后一次登录时间
     *
     * @param userLastLoginTime 用户最后一次登录时间
     * @param userId            用户ID
     * @return 用户最后一次登录时间是否修改成功
     */
    @Override
    public int modifyLastLoginTime(Date userLastLoginTime, String userId) {
        return userMapper.updateUserLastLoginTime(userLastLoginTime, userId);
    }
}
