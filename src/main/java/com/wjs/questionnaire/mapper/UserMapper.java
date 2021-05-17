package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 用户数据的持久层接口
 */
@Mapper
public interface UserMapper {

    /**
     * 获取所有普通用户信息列表的行数
     *
     * @return 普通用户信息列表的行数
     */
    int findAllUserRows();

    /**
     * 获取所有普通用户信息列表（分页）
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 普通用户信息列表
     */
    List<UserEntity> findAllUserPage(int offset, int limit);

    /**
     * 获取所有用户信息列表
     *
     * @return 用户信息列表
     */
    List<UserEntity> findAllUserList();

    /**
     * 根据 userId 查找 User
     *
     * @return User
     */
    UserEntity findUserByUserId(String userId);

    /**
     * 根据 userName 查找 User
     *
     * @return User
     */
    UserEntity findUserByUserName(String userName);

    /**
     * 根据 userPhone 查找 User
     *
     * @return User
     */
    UserEntity findUserByUserPhone(String userPhone);

    /**
     * 根据 userEmail 查找 User
     *
     * @return User
     */
    UserEntity findUserByUserEmail(String userEmail);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 是否注册成功
     */
    int insertUser(UserEntity user);

    /**
     * 修改用户状态
     *
     * @param userStatus 用户状态
     * @param userId     用户ID
     * @return 用户状态是否修改成功
     */
    int updateUserStatus(Integer userStatus, String userId);

    /**
     * 修改用户最后一次登录时间
     *
     * @param userLastLoginTime 用户最后一次登录时间
     * @param userId            用户ID
     * @return 用户最后一次登录时间是否修改成功
     */
    int updateUserLastLoginTime(Date userLastLoginTime, String userId);

    /**
     * 根据 userId 修改用户信息
     *
     * @param u 用户信息
     * @return 用户信息是否修改成功
     */
    int updateUserByUserId(UserEntity u);

    /**
     * 根据 userId 删除用户信息
     *
     * @param userId 用户编号
     * @return 用户信息是否删除成功
     */
    int deleteUserByUserId(String userId);
}
