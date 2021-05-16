package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.entity.UserCommentEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.QuestionnaireMapper;
import com.wjs.questionnaire.mapper.UserCommentMapper;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IUserCommentService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserCommentServiceImpl implements IUserCommentService {

    @Autowired
    private UserCommentMapper userCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 根据 ucId 查询留言信息
     *
     * @param ucId 用户编号
     * @return 用户留言信息
     */
    @Override
    public UserCommentEntity getUserCommentByUCId(String ucId) {
        return userCommentMapper.findUserCommentByUCId(ucId);
    }

    /**
     * 获取当前管理员用户已处理的用户留言信息列表
     *
     * @param userId 用户编号
     * @return 已处理的用户留言信息列表
     */
    @Override
    public List<Map<String, Object>> getTreatedUserCommentLists(String userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<UserCommentEntity> userCommentList = userCommentMapper.findTreatedUserCommentLists(userId);
        if (userCommentList != null) {
            for (UserCommentEntity userComment : userCommentList) {
                Map<String, Object> map = new HashMap<>();
                map.put("userComment", userComment);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取所有的未处理的用户留言信息的行数
     *
     * @return 未处理的用户留言信息的行数
     */
    @Override
    public int getAllUntreatedUserCommentRows() {
        return userCommentMapper.findAllUntreatedUserCommentRows();
    }

    /**
     * 获取所有的未处理的用户留言信息列表
     *
     * @return 未处理的用户留言信息列表
     */
    @Override
    public List<Map<String, Object>> getAllUntreatedUserCommentLists() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<UserCommentEntity> userCommentList = userCommentMapper.findAllUntreatedUserCommentLists();
        if (userCommentList != null) {
            for (UserCommentEntity userComment : userCommentList) {
                Map<String, Object> map = new HashMap<>();
                map.put("userComment", userComment);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 保存回复内容
     *
     * @param ucId        用户留言编号
     * @param adminUserId 管理员用户编号
     * @param answer      回复的内容
     * @param answerTime  回复时间
     * @return 回复内容是否保存成功
     */
    @Override
    public JSONResult getUserCommentSubmit(String ucId, String adminUserId, String answer, String answerTime) {
        UserCommentEntity userComment = userCommentMapper.findUserCommentByUCId(ucId);
        UserEntity user = userMapper.findUserByUserId(userComment.getUserId());
        QuestionnaireEntity questionnaire = questionnaireMapper.findQuestionnaireByQnId(userComment.getQuestionnaireId());

        JSONResult jsonResult;
        if (sendAnswerHtml(user.getUserEmail(), user.getUserName(), questionnaire.getQuestionnaireTitle(), answer)) {
            UserCommentEntity uc = new UserCommentEntity(ucId, adminUserId, answer, 1);
            if (userCommentMapper.updateUserCommentByUCId(uc) == 1) {
                System.out.println(uc);
                jsonResult = JSONResult.build();
            } else {
                jsonResult = JSONResult.build("用户留言信息更新失败！！！");
            }
        } else {
            jsonResult = JSONResult.build("邮件发送失败！！！");
        }

        return jsonResult;
    }

    /**
     * 发送回复邮件
     *
     * @param email              邮件接收者
     * @param username           用户名
     * @param questionnaireTitle 问卷标题
     * @param answer             回复内容
     * @return 邮件是否发送成功
     */
    public boolean sendAnswerHtml(String email, String username, String questionnaireTitle, String answer) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("questionnaireTitle", questionnaireTitle);
        context.setVariable("answer", answer);
        String content = templateEngine.process("/mail/answer", context);
        return mailClient.sendMail(email, "对问卷 " + questionnaireTitle + " 的回复", content);
    }
}
