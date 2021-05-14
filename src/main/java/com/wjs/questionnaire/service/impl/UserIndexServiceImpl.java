package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.mapper.AnswerMapper;
import com.wjs.questionnaire.mapper.UserCommentMapper;
import com.wjs.questionnaire.service.IUserIndexService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserIndexServiceImpl implements IUserIndexService {

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserCommentMapper userCommentMapper;

    /**
     * 根据 userId 和 qnId 删除已填写的问卷信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 已填写的问卷信息是否删除成功
     */
    @Override
    public JSONResult getDeleteSubmit(String userId, String qnId) {
        JSONResult jsonResult;
        int flag1 = answerMapper.deleteAnswerByUserIdAndQNId(userId, qnId);
        int flag2 = userCommentMapper.deleteUserCommentByUserIdAndQNId(userId, qnId);
        if (flag1 > 0 && flag2 > 0) {
            jsonResult = JSONResult.build();
        } else {
            jsonResult = JSONResult.build("已填写的该问卷信息删除失败！！！");
        }
        return jsonResult;
    }
}
