package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.AnswerEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.AnswerMapper;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IDataAnalysisService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataAnalysisServiceImpl implements IDataAnalysisService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private OptionMapper optionMapper;

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

    /**
     * 获取 用户对该问题的回答情况（该问题的每个选项分别有多少人选择）
     *
     * @param qnId 问卷编号
     * @param qId  问题编号
     * @return 单选选择题每个选项的数量
     */
    @Override
    public JSONResult getOptionsNumberByQNIdAndQId(String qnId, String qId) {
        List<AnswerEntity> answerList = answerMapper.findOptionsNumberByQNIdAndQId(qnId, qId);

        JSONResult jsonResult;
        if (answerList != null) {
            Map<String, Object> map1 = new HashMap<>();
            for (AnswerEntity answer : answerList) {
                String optionId = answer.getOptionId();
                if (map1.get(optionId) == null) {
                    map1.put(optionId, 1);
                } else {
                    map1.put(optionId, Integer.parseInt(map1.get(optionId).toString()) + 1);
                }
            }

            Map<String, Object> map2 = new TreeMap<>();
            for (String optionId : map1.keySet()) {
                map2.put(optionMapper.findOptionByOId(optionId).getOptionContent(),Integer.parseInt(map1.get(optionId).toString()));
            }

            jsonResult = JSONResult.build(map2);
        } else {
            jsonResult = JSONResult.build("暂时还未有人回答该问题！！！");
        }
        return jsonResult;
    }
}
