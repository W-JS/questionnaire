package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IUserService;
import com.wjs.questionnaire.util.*;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
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

    /**
     * 设置分页参数
     *
     * @param page 分页对象参数
     * @return 分页结果
     */
    @Override
    public PageUtil setUserListPage(PageUtil page) {
        page.setRows(userMapper.findAllUserRows());
        page.setPath("/user/AllUser");
        return page;
    }

    /**
     * 获取所有普通用户信息列表
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 普通用户信息列表
     */
    @Override
    public List<Map<String, Object>> getUserList(int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<UserEntity> userList = userMapper.findAllUserPage(offset, limit);
        if (userList != null) {
            for (UserEntity user : userList) {
                Map<String, Object> map = new HashMap<>();
                map.put("user", user);
                list.add(map);
            }
        }
        return list;
    }

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
        ;
//        int flag = userMapper.updateUserLastLoginTime(userLastLoginTime, userId);
        int flag = userMapper.updateUserByUserId(new UserEntity(userId, userLastLoginTime));

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
     * 判断密码是否正确
     *
     * @param userId   用户编号
     * @param password 密码
     * @return 密码是否正确
     */
    @Override
    public JSONResult getPasswordExists(String userId, String password) {
        UserEntity user = userMapper.findUserByUserId(userId);
        boolean flag = isNotEmpty(user);
        JSONResult jsonResult;
        if (flag) {
            if (user.getUserPassword().equals(md5AndSha(password))) {
                jsonResult = JSONResult.build();
            } else {
                jsonResult = JSONResult.build("密码不正确，请重新输入！！！");
            }
        } else {
            jsonResult = JSONResult.build("该用户不存在！！！");
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
        UserEntity user = new UserEntity(userId, usernameReg, phoneReg, emailReg, md5AndSha(passwordReg), sexReg, StringToDate(birthdayReg), statusReg, typeReg, StringToDate(createTimeReg));
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
    @Override
    public JSONResult getUpdateSubmit1(String userId, String userName, String userPhone, String userEmail, String sex, String birthday, String userUpdateTime) {
        JSONResult jsonResult;
        int flag = userMapper.updateUserByUserId(new UserEntity(userId, userName, userPhone, userEmail, sex, StringToDate(birthday), StringToDate(userUpdateTime)));
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("个人信息修改失败！！！");
        }
        return jsonResult;
    }

    /**
     * 修改用户密码
     *
     * @param userId   用户编号
     * @param password 密码
     * @return 用户密码是否修改成功
     */
    @Override
    public JSONResult getUpdateSubmit2(String userId, String password) {
        JSONResult jsonResult;
        int flag = userMapper.updateUserByUserId(new UserEntity(userId, md5AndSha(password)));
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("密码修改失败！！！");
        }
        return jsonResult;
    }

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
    @Override
    public JSONResult getUpdateSubmit3(String userId, String userName, String userPhone, String userEmail, String userSex, String userBirthday, String userStatus, String userCreateTime, String userUpdateTime, String userDeleteTime, String userLastLoginTime) {
        JSONResult jsonResult;
        int flag = userMapper.updateUserByUserId(new UserEntity(userId, userName, userPhone, userEmail, userSex, StringToDate(userBirthday), Integer.parseInt(userStatus), StringToDate(userCreateTime), StringToDate(userUpdateTime), StringToDate(userDeleteTime), StringToDate(userLastLoginTime)));
        if (flag == 1) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("用户信息修改失败！！！");
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
        return mailClient.sendMail(email, "调查问卷网站-激活邮件", content);
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
//            int count = userMapper.updateUserStatus(USERSTATUS_ACTIVATED, user.getUserId());
            int count = userMapper.updateUserByUserId(new UserEntity(user.getUserId(), USERSTATUS_ACTIVATED));
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
     * 生成验证码，发送登录验证码邮件，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    @Override
    public JSONResult LoginCode(String userId, int codeLength) {
        UserEntity user = userMapper.findUserByUserId(userId);
        JSONResult jsonResult;
        if (user != null) {
            if (user.getUserStatus() == 1) {
                String code = StringUtil.getRandomString(codeLength);
                if (sendLoginCodeHtml(user.getUserEmail(), user.getUserName(), code)) {
                    redisTemplate.opsForValue().set(userId, code, 2, TimeUnit.MINUTES);
                    jsonResult = JSONResult.build(OBJECT_EXISTENCE, null, code);
                } else {
                    jsonResult = JSONResult.build("该邮箱不存在！！！");
                }
            } else {
                jsonResult = JSONResult.build("账号未激活，请先激活账号再登录！！！");
            }
        } else {
            jsonResult = JSONResult.build("用户不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 生成验证码，发送修改密码码邮件，存入Redis
     *
     * @param codeLength 验证码长度
     * @return 验证码
     */
    @Override
    public JSONResult UpdatePasswordCode(String userId, int codeLength) {
        UserEntity user = userMapper.findUserByUserId(userId);
        JSONResult jsonResult;
        if (user != null) {
            if (user.getUserStatus() == 1) {
                String code = StringUtil.getRandomString(codeLength);
                if (sendUpdatePasswordCodeHtml(user.getUserEmail(), user.getUserName(), code)) {
                    redisTemplate.opsForValue().set(userId, code, 2, TimeUnit.MINUTES);
                    jsonResult = JSONResult.build(OBJECT_EXISTENCE, null, code);
                } else {
                    jsonResult = JSONResult.build("该邮箱不存在！！！");
                }
            } else {
                jsonResult = JSONResult.build("账号未激活，请先激活账号再登录！！！");
            }
        } else {
            jsonResult = JSONResult.build("用户不存在！！！");
        }
        return jsonResult;
    }

    /**
     * 登录前，发送登录验证码邮件
     *
     * @param email    邮件接收者
     * @param username 用户名
     * @param code     验证码
     */
    public boolean sendLoginCodeHtml(String email, String username, String code) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("code", code);
        String content = templateEngine.process("/mail/code", context);
        return mailClient.sendMail(email, "调查问卷网站-登录验证码邮件", content);
    }

    /**
     * 登录前，发送修改密码验证码邮件
     *
     * @param email    邮件接收者
     * @param username 用户名
     * @param code     验证码
     */
    public boolean sendUpdatePasswordCodeHtml(String email, String username, String code) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("code", code);
        String content = templateEngine.process("/mail/forget", context);
        return mailClient.sendMail(email, "调查问卷网站-修改密码验证码邮件", content);
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
     * 根据 userId 删除用户信息
     *
     * @param userId 用户编号
     * @return 用户信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit1(String userId) {
        JSONResult jsonResult;
        if (userMapper.deleteUserByUserId(userId) != 0) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("用户信息删除失败！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 userId 删除多个用户信息
     *
     * @param user JSON格式的字符串，包含多个用户编号
     * @return 用户信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit2(String user) {
        JSONArray json = JSONArray.fromObject(user);
        JSONResult jsonResult1;
        boolean flag = true;
        if (json.size() > 0) {
            for (int i = 0; i < json.size(); i++) {
                String userId = (String) json.get(i);
                String userName = userMapper.findUserByUserId(userId).getUserName();
                jsonResult1 = getDeleteSubmit1(userId);
                if (jsonResult1.getState() != 1) {
                    flag = false;
                    System.out.println("删除用户 失败：" + userName);
                    break;
                } else {
                    System.out.println("删除用户 成功：" + userName);
                }

            }
        }
        JSONResult jsonResult2;
        if (flag) {
            jsonResult2 = JSONResult.build();
        } else {
            jsonResult2 = JSONResult.build("问卷信息删除失败！！！");
        }
        return jsonResult2;
    }

    /**
     * 根据 userId 得到用户信息
     *
     * @return 用户信息
     */
    @Override
    public JSONResult getUserByUserId(String userId) {
        UserEntity user = userMapper.findUserByUserId(userId);
        JSONResult jsonResult;
        if (user != null) {
            jsonResult = JSONResult.build(user);
        } else {
            jsonResult = JSONResult.build("用户信息不存在！！！");
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
