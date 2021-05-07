package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.OptionEntity;
import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.mapper.OptionMapper;
import com.wjs.questionnaire.mapper.QuestionMapper;
import com.wjs.questionnaire.mapper.QuestionnaireMapper;
import com.wjs.questionnaire.service.IIndexService;
import com.wjs.questionnaire.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IIndexService {

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private OptionMapper optionMapper;

    /**
     * 根据 qnId 查询当前问卷（问卷信息 + 问题信息 + 选项信息）
     *
     * @param qnId 问卷编号
     * @return JSON格式数据：根据 qnId 查询当前问卷
     */
    @Override
    public JSONResult getQuestionnaireByQnId(String qnId) {

        Map<String, Object> map = new HashMap<>();

        // 1、获取问卷信息
        QuestionnaireEntity questionnaire = questionnaireMapper.findQuestionnaireByQnId(qnId);
        map.put("questionnaire", questionnaire);

        // 2、获取问题信息（根据题型分类）
        List<QuestionEntity> questionList = questionMapper.findAllQuestionByQnId(qnId);
        if (questionList != null) {
            // 2.1、获取单项选择题
            List<Map<String, Object>> singleChoiceQuestionList = new ArrayList<>();
            for (QuestionEntity question : questionList) {
                if ("singleChoice".equals(question.getQuestiontypeId())) {
                    Map<String, Object> singleChoiceMap = new HashMap<>();
                    singleChoiceMap.put("question", question);

                    List<OptionEntity> optionList = optionMapper.findOptionByQId(question.getQuestionId());
                    singleChoiceMap.put("optionList", optionList);
                    singleChoiceQuestionList.add(singleChoiceMap);
                }
            }
            map.put("singleChoiceQuestionList", singleChoiceQuestionList);

            // 2.2、获取多项选择题
            List<QuestionEntity> multipleChoiceQuestionList = new ArrayList<>();
            for (QuestionEntity question : questionList) {
                if ("multipleChoice".equals(question.getQuestiontypeId())) {
                    multipleChoiceQuestionList.add(question);
                }
            }
            map.put("multipleChoiceQuestionList", multipleChoiceQuestionList);

            // 2.3、获取判断题
            List<QuestionEntity> judgmentQuestionList = new ArrayList<>();
            for (QuestionEntity question : questionList) {
                if ("judgment".equals(question.getQuestiontypeId())) {
                    judgmentQuestionList.add(question);
                }
            }
            map.put("judgmentQuestionList", judgmentQuestionList);

            // 2.4、获取填空题
            List<QuestionEntity> fillBlankQuestionList = new ArrayList<>();
            for (QuestionEntity question : questionList) {
                if ("fillBlank".equals(question.getQuestiontypeId())) {
                    fillBlankQuestionList.add(question);
                }
            }
            map.put("fillBlankQuestionList", fillBlankQuestionList);

            // 2.5、获取评分题
            List<QuestionEntity> scoreQuestionList = new ArrayList<>();
            for (QuestionEntity question : questionList) {
                if ("score".equals(question.getQuestiontypeId())) {
                    scoreQuestionList.add(question);
                }
            }
            map.put("scoreQuestionList", scoreQuestionList);
        }

        for (QuestionEntity question : questionList) {
            System.out.println(question);
        }
        // 3、获取选项信息

        JSONResult jsonResult = JSONResult.build(map);
        return jsonResult;
    }

    /**
     * 根据 qnId 查询问卷信息
     *
     * @param qnId 问卷编号
     * @return 问卷信息
     */
    @Override
    public QuestionnaireEntity getQuestionnaire(String qnId) {
        return questionnaireMapper.findQuestionnaireByQnId(qnId);
    }

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    @Override
    public List<Map<String, Object>> findQuestionByQnIdAndQtId1(String qnId, String qtId) {
        List<Map<String, Object>> data1 = new ArrayList<>();
        List<QuestionEntity> questionList = questionMapper.findQuestionByQnIdAndQtId(qnId, qtId);
        if (questionList != null) {
            for (QuestionEntity question : questionList) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("question", question);

                List<Map<String, Object>> data2 = new ArrayList<>();
                List<OptionEntity> optionList = optionMapper.findOptionByQId(question.getQuestionId());
                if (optionList != null) {
                    for (OptionEntity option : optionList) {
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("option", option);
                        data2.add(map2);
                    }
                    map1.put("option", data2);
                }

                data1.add(map1);
            }
        }
        return data1;
    }

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    @Override
    public List<QuestionEntity> findQuestionByQnIdAndQtId2(String qnId, String qtId) {
        return questionMapper.findQuestionByQnIdAndQtId(qnId, qtId);
    }
}
