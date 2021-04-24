package com.wjs.questionnaire.controller;

import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.wjs.questionnaire.util.EncryptUtil.md5AndSha;

/**
 * 处理用户相关请求的控制器类
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 访问URL：http://localhost:8080/questionnaire/user/login
     *
     * @return 进入登录页面
     */
    @GetMapping(value = "/login")
    public String jumpLoginPage() {
        return "/site/login";
    }

    /**
     * 访问URL：http://localhost:8080/questionnaire/user/showUser
     *
     * @return 进入显示所有用户信息页面
     */
    @RequestMapping(value = "/showUser")
    public String jumpShowUserPage() {
        return "/prompt/showUser";
    }

    /**
     * 激活账号
     *
     * @param userId 用户ID
     * @return 激活结果信息
     */
    @GetMapping(value = "/activate/{userId}")
    public String activate(@PathVariable String userId) {
        String flag = userService.activate(userId);
        if ("userActivated".equals(flag)) {
            return "/prompt/userActivated";
        } else if ("userActivateSucceed".equals(flag)) {
            return "/prompt/userActivateSucceed";
        } else if ("userActivateFailure".equals(flag)) {
            return "/prompt/userActivateFailure";
        } else {
            return "/prompt/userNotExist";
        }
    }

    /**
     * 访问URL：http://localhost:8080/questionnaire/user/allUser
     *
     * @return JSON格式数据：所有用户信息
     */
    @GetMapping("allUser")
    @ResponseBody
    public JSONResult getAllUserList() {
        return userService.getAllUserList();
    }

    /**
     * 判断用户名/手机号码/电子邮箱是否存在
     *
     * @param loginMethod user_name/user_phone/user_email
     * @param userLog     用户名/手机号码/电子邮箱
     * @return 是否存在用户名/手机号码/电子邮箱
     */
    @GetMapping(value = "/userLogExists")
    @ResponseBody
    public JSONResult getUserLogExists(String loginMethod, String userLog) {
        return userService.getUserLogExists(loginMethod, userLog);
    }

    /**
     * 判断登录密码是否正确
     *
     * @param userId      用户ID
     * @param passwordLog 登录密码
     * @return 登录密码是否正确
     */
    @GetMapping(value = "/passwordLogExists")
    @ResponseBody
    public JSONResult getPasswordLogExists(String userId, String passwordLog) {
        return userService.getPasswordLogExists(userId, passwordLog);
    }

    /**
     * 用户登录
     *
     * @param userLastLoginTimeLog 用户最后一次登录时间
     * @param userId               用户ID
     * @return 用户是否登录成功
     */
    @PostMapping(value = "/loginSubmit")
    @ResponseBody
    public JSONResult getLoginSubmit(String userLastLoginTimeLog, String userId) {
        return userService.getLoginSubmit(userLastLoginTimeLog, userId);
    }


    /**
     * 生成通用唯一标识符
     *
     * @return 用户ID（userId）
     */
    @GetMapping(value = "/generatorUserID")
    @ResponseBody
    public String GeneratorUserID() {
        return userService.GeneratorUserID();
    }

    /**
     * 判断用户名是否存在
     *
     * @param usernameReg 用户名
     * @return 是否存在用户名
     */
    @GetMapping(value = "/usernameRegExists")
    @ResponseBody
    public JSONResult getUsernameRegExists(String usernameReg) {
        return userService.getUsernameRegExists(usernameReg);
    }

    /**
     * 判断手机号是否存在
     *
     * @param phoneReg 手机号
     * @return 是否存在手机号
     */
    @GetMapping(value = "/phoneRegExists")
    @ResponseBody
    public JSONResult getPhoneRegExists(String phoneReg) {
        return userService.getPhoneRegExists(phoneReg);
    }

    /**
     * 判断电子邮件是否存在
     *
     * @param emailReg 电子邮件
     * @return 是否存在电子邮件
     */
    @GetMapping(value = "/emailRegExists")
    @ResponseBody
    public JSONResult getEmailRegExists(String emailReg) {
        return userService.getEmailRegExists(emailReg);
    }

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
    @PostMapping(value = "/registerSubmit")
    @ResponseBody
    public JSONResult getRegisterSubmit(String userId, String usernameReg, String phoneReg, String emailReg, String passwordReg, String sexReg,
                                        String birthdayReg, int statusReg, int typeReg, String createTimeReg) {
        return userService.getRegisterSubmit(userId, usernameReg, phoneReg, emailReg, md5AndSha(passwordReg), sexReg, birthdayReg, statusReg, typeReg, createTimeReg);
    }

    /**
     * 生成验证码，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    @PostMapping(value = "/generateAuthCode")
    @ResponseBody
    public String getCodeReg(String userId, int codeLength) {
        return userService.getCodeReg(userId, codeLength);
    }

    /**
     * 从Redis获取验证码，判断验证码是否正确
     *
     * @param code 验证码
     * @return 验证码是否正确
     */
    @GetMapping(value = "/codeExists")
    @ResponseBody
    public JSONResult getCodeExists(String userId, String code) {
        return userService.getCodeExists(userId, code);
    }
}
