package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.MailClient;
import com.wjs.questionnaire.util.StringUtil;
import com.wjs.questionnaire.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.EmptyUtil.isNotEmpty;
import static com.wjs.questionnaire.util.EncryptUtil.md5AndSha;
import static com.wjs.questionnaire.util.QuestionnaireConstant.*;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public JSONResult getAllUserList() {
        List<UserEntity> data = userMapper.findAllUserList();
        JSONResult jsonResult;
        if (data != null) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("暂时还未创建用户！！！");
        }
        return jsonResult;
    }

    /**
     * 判断用户名/手机号码/电子邮箱是否存在
     *
     * @param loginMethod user_name/user_phone/user_email
     * @param userLog     用户名/手机号码/电子邮箱
     * @return 是否存在用户名/手机号码/电子邮箱
     */
    @Override
    public JSONResult getUserLogExists(String loginMethod, String userLog) {
        UserEntity user;
        if ("username".equals(loginMethod)) {
            user = userMapper.findUserByUserName(userLog);
        } else if ("phone".equals(loginMethod)) {
            user = userMapper.findUserByUserPhone(userLog);
        } else if ("email".equals(loginMethod)) {
            user = userMapper.findUserByUserEmail(userLog);
        } else {
            user = null;
        }

        boolean flag = isNotEmpty(user);
        JSONResult jsonResult;
        if (flag) {
            jsonResult = JSONResult.build(OBJECT_EXISTENCE, user.getUserId(), null);
        } else {
            jsonResult = JSONResult.build("用户名/手机号码/电子邮箱不存在！！！");
        }
        return jsonResult;

    }

    /**
     * 判断登录密码是否正确
     *
     * @param userId      用户ID
     * @param passwordLog 登录密码
     * @return 登录密码是否正确
     */
    @Override
    public JSONResult getPasswordLogExists(String userId, String passwordLog) {

        UserEntity user = userMapper.findUserByUserId(userId);
        boolean flag = isNotEmpty(user);
        JSONResult jsonResult;
        if (flag) {
            if (user.getUserPassword().equals(md5AndSha(passwordLog))) {
                jsonResult = JSONResult.build();
            } else {
                jsonResult = JSONResult.build("密码错误，请重新输入！！！");
            }
        } else {
            jsonResult = JSONResult.build("当前注册用户不存在！！！");
        }
        return jsonResult;

    }

    /**
     * 用户登录
     *
     * @param userLastLoginTimeLog 用户最后一次登录时间
     * @param userId               用户ID
     * @return 用户是否登录成功
     */
    @Override
    public JSONResult getLoginSubmit(String userLastLoginTimeLog, String userId) {

        Date userLastLoginTime = StringToDate(userLastLoginTimeLog);

        UserEntity user = userMapper.findUserByUserId(userId);
        int flag = userMapper.updateUserLastLoginTime(userLastLoginTime, userId);

        JSONResult jsonResult;
        if (user.getUserStatus() == USERSTATUS_ACTIVATED) {
            if (flag == OPERATE_SUCCESS) {
                jsonResult = JSONResult.build();
                redisTemplate.opsForValue().set(ONLINEUSERID, userId);// 成功登录，将 userId 存进Redis
            } else {
                jsonResult = JSONResult.build("登录失败！！！");
            }
        } else {
            jsonResult = JSONResult.build("用户未激活！！！");
        }
        return jsonResult;
    }


    /**
     * 生成通用唯一标识符
     *
     * @return 用户ID（userId）
     */
    @Override
    public String GeneratorUserID() {
        String userId = UUIDGenerator.get16UUID();
        return userId;
    }

    /**
     * 判断用户名是否存在
     *
     * @param usernameReg 用户名
     * @return 是否存在用户名
     */
    @Override
    public JSONResult getUsernameRegExists(String usernameReg) {
        UserEntity user = userMapper.findUserByUserName(usernameReg);
        boolean flag = isNotEmpty(user);
        JSONResult jsonResult;
        if (flag) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("用户名不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 判断手机号是否存在
     *
     * @param phoneReg 手机号
     * @return 是否存在手机号
     */
    @Override
    public JSONResult getPhoneRegExists(String phoneReg) {
        UserEntity user = userMapper.findUserByUserPhone(phoneReg);
        boolean flag = isNotEmpty(user);
        JSONResult jsonResult;
        if (flag) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("手机号不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 判断电子邮件是否存在
     *
     * @param emailReg 电子邮件
     * @return 是否存在电子邮件
     */
    @Override
    public JSONResult getEmailRegExists(String emailReg) {
        UserEntity user = userMapper.findUserByUserEmail(emailReg);
        boolean flag = isNotEmpty(user);
        JSONResult jsonResult;
        if (flag) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("电子邮件不存在！！！");
        }
        return jsonResult;
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
    @Override
    public JSONResult getRegisterSubmit(String userId, String usernameReg, String phoneReg, String emailReg, String passwordReg, String sexReg, String birthdayReg, int statusReg, int typeReg, String createTimeReg) {
        Date userBirthday = StringToDate(birthdayReg);
        Date userCreateTime = StringToDate(createTimeReg);
        UserEntity user = new UserEntity(userId, usernameReg, phoneReg, emailReg, md5AndSha(passwordReg), sexReg, userBirthday, statusReg, typeReg, userCreateTime);
        JSONResult jsonResult;
        String url = "http://localhost:8080/questionnaire/user/activate/" + user.getUserId();
        if (sendActivateHtml(user.getUserEmail(), user.getUserName(), url)) {
            if (userMapper.insertUser(user) == 1) {
                System.out.println(user);
                jsonResult = JSONResult.build();
            } else {
                jsonResult = JSONResult.build("注册失败！！！");
            }
        } else {
            jsonResult = JSONResult.build("该邮箱不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 注册后，发送激活邮件
     *
     * @param email    邮件接收者
     * @param username 用户名
     * @param url      激活地址URL
     */
    public boolean sendActivateHtml(String email, String username, String url) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        return mailClient.sendMail(email, "调查问卷激活邮件", content);
    }

    /**
     * 激活账号
     *
     * @param userId 用户ID
     * @return 激活结果信息
     */
    @Override
    public String activate(String userId) {
        UserEntity user = userMapper.findUserByUserId(userId);
        boolean flag = isNotEmpty(user);
        if (flag) {
            if (user.getUserStatus() == USERSTATUS_ACTIVATED) {
                return "userActivated";
            }
            int count = userMapper.updateUserStatus(USERSTATUS_ACTIVATED, user.getUserId());
            if (count == 1) {
                return "userActivateSucceed";
            } else {
                return "userActivateFailure";
            }
        } else {
            return "userNotExist";
        }
    }


    /**
     * 生成验证码，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    @Override
    public String getCodeReg(String userId, int codeLength) {
        String redisKey = userId;
        String code = StringUtil.getRandomString(codeLength);
        redisTemplate.opsForValue().set(redisKey, code, 2, TimeUnit.MINUTES);
        return code;
    }

    /**
     * 从Redis获取验证码，判断验证码是否正确
     *
     * @param code 验证码
     * @return 验证码是否正确
     */
    @Override
    public JSONResult getCodeExists(String userId, String code) {
        String getCode = (String) redisTemplate.opsForValue().get(userId);
        boolean flag = code.equals(getCode);
        JSONResult jsonResult;
        if (flag) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("验证码错误，请重新输入！！！");
        }
        return jsonResult;
    }


    /**
     * 获取在线用户信息
     *
     * @return 在线用户信息
     */
    @Override
    public JSONResult getOnlineUser() {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        UserEntity user = userMapper.findUserByUserId(OnlineUserID);
        return JSONResult.build(user);
    }
}
