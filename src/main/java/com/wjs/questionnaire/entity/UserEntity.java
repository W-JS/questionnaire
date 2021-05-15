package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息数据的实体类
 */
//@Data                             // @Data 在lombok工具包含了get,set,toString等方法
public class UserEntity implements Serializable {

    private String userId;          // 用户编号
    private String userName;        // 用户名
    private String userPhone;       // 手机号
    private String userEmail;       // 电子邮箱
    private String userPassword;    // 密码
    private String userSex;         // 性别
    private Date userBirthday;      // 生日
    private Integer userStatus;     // 状态：-1-已注销; 0-未激活; 1-已激活;
    private Integer userType;       // 类型：0-普通用户; 1-超级管理员;
    private Date userCreateTime;    // 用户注册时间
    private Date userUpdateTime;    // 用户信息更新时间
    private Date userDeleteTime;    // 用户注销时间
    private Date userLastLoginTime; // 用户最后一次登录时间

    public UserEntity() {
    }

    public UserEntity(String userId, Date userLastLoginTime) {
        this.userId = userId;
        this.userLastLoginTime = userLastLoginTime;
    }

    public UserEntity(String userId, Integer userStatus) {
        this.userId = userId;
        this.userStatus = userStatus;
    }

    public UserEntity(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public UserEntity(String userId, String userName, String userPhone, String userEmail, String userSex, Date userBirthday, Date userUpdateTime) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userSex = userSex;
        this.userBirthday = userBirthday;
        this.userUpdateTime = userUpdateTime;
    }

    public UserEntity(String userId, String userName, String userPhone, String userEmail, String userPassword, String userSex, Date userBirthday, Integer userStatus, Integer userType, Date userCreateTime) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userSex = userSex;
        this.userBirthday = userBirthday;
        this.userStatus = userStatus;
        this.userType = userType;
        this.userCreateTime = userCreateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public Date getUserUpdateTime() {
        return userUpdateTime;
    }

    public void setUserUpdateTime(Date userUpdateTime) {
        this.userUpdateTime = userUpdateTime;
    }

    public Date getUserDeleteTime() {
        return userDeleteTime;
    }

    public void setUserDeleteTime(Date userDeleteTime) {
        this.userDeleteTime = userDeleteTime;
    }

    public Date getUserLastLoginTime() {
        return userLastLoginTime;
    }

    public void setUserLastLoginTime(Date userLastLoginTime) {
        this.userLastLoginTime = userLastLoginTime;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userBirthday=" + userBirthday +
                ", userStatus=" + userStatus +
                ", userType=" + userType +
                ", userCreateTime=" + userCreateTime +
                ", userUpdateTime=" + userUpdateTime +
                ", userDeleteTime=" + userDeleteTime +
                ", userLastLoginTime=" + userLastLoginTime +
                '}';
    }
}
