package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.AnswerEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.AnswerMapper;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IDataAnalysisService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataAnalysisServiceImpl implements IDataAnalysisService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnswerMapper answerMapper;

    /**
     * 获取 总人数+填写该问卷的人数+未填写该问卷的人数
     *
     * @param qnId 问卷编号
     * @return 人数
     */
    @Override
    public JSONResult getWriteQuestionnaireNumberByQNId(String qnId) {
        List<UserEntity> userList = userMapper.findAllUserList();
        List<AnswerEntity> answerList = answerMapper.findWriteQuestionnaireNumberByQNId(qnId);

        JSONResult jsonResult;
        if (userList != null && answerList != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("all", userList.size());
            map.put("filled", answerList.size());
            map.put("unFilled", userList.size() - answerList.size());
            jsonResult = JSONResult.build(map);
        } else {
            jsonResult = JSONResult.build("暂时还未有人填写该问卷！！！");
        }
        return jsonResult;
    }
}
