package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.*;
import com.wjs.questionnaire.mapper.*;
import com.wjs.questionnaire.service.IIndexService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.UUIDGenerator;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IndexServiceImpl implements IIndexService {

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private OptionMapper optionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private UserCommentMapper userCommentMapper;

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
                    boolean flag = false;
                    for (OptionEntity option : optionList) {
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("option", option);
                        data2.add(map2);

                        QuestionEntity q = questionMapper.findRearQuestionByQIdAndOId(question.getQuestionId(), option.getOptionId());
                        if (q != null) {
                            flag = true;
                            map1.put("q", "true");
                            map2.put("q", "true");
                        } else {
                            map2.put("q", "false");
                        }
                    }
                    if (!flag) {
                        map1.put("q", "false");
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
    public List<Map<String, Object>> findQuestionByQnIdAndQtId2(String qnId, String qtId) {
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
     * 查询当前问题的当前选项的后置问题
     *
     * @param qId 当前问题编号
     * @param oId 当前选项编号
     * @return JSON格式数据
     */
    @Override
    public JSONResult findRearQuestionByQIdAndOId(String qId, String oId) {
        QuestionEntity question = questionMapper.findRearQuestionByQIdAndOId(qId, oId);
        JSONResult jsonResult;
        if (question != null) {
            List<Map<String, Object>> data1 = new ArrayList<>();
            Map<String, Object> map1 = new HashMap<>();
            map1.put("question", question);

            List<OptionEntity> optionList = optionMapper.findOptionByQId(question.getQuestionId());
            if (optionList != null) {
                List<Map<String, Object>> data2 = new ArrayList<>();
                for (OptionEntity option : optionList) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("option", option);
                    data2.add(map2);
                }
                map1.put("optionList", data2);
            }

            data1.add(map1);
            jsonResult = JSONResult.build(data1);
        } else {
            jsonResult = JSONResult.build("当前问题的当前选项无后置问题");
        }
        return jsonResult;
    }

    /**
     * 保存用户填写的问卷信息
     *
     * @param userId 用户编号
     * @param JSONsc 单项选择题
     * @param JSONmc 多项选择题
     * @param JSONjm 判断题
     * @param JSONfb 填空题
     * @param JSONs  评分题
     * @return 用户填写的问卷信息是否保存成功
     */
    @Override
    public JSONResult saveSubmit(String userId, String JSONsc, String JSONmc, String JSONjm, String JSONfb, String JSONs, String userComments) {
        Date date = new Date();

        // 用户留言
        UserCommentEntity userComment = new UserCommentEntity(UUIDGenerator.get16UUID(), userId, userComments, date);
        userCommentMapper.insertUserComment(userComment);

        // 单项选择题
        JSONArray sc = JSONArray.fromObject(JSONsc);
        if (sc.size() > 0) {
            for (int i = 0; i < sc.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, (String) sc.getJSONObject(i).get("qId"), (String) sc.getJSONObject(i).get("qt"), (String) sc.getJSONObject(i).get("oId"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 多项选择题
        JSONArray mc = JSONArray.fromObject(JSONmc);
        if (mc.size() > 0) {
            for (int i = 0; i < mc.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, (String) mc.getJSONObject(i).get("qId"), (String) mc.getJSONObject(i).get("qt"), (String) mc.getJSONObject(i).get("oId"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 判断题
        JSONArray jm = JSONArray.fromObject(JSONjm);
        if (jm.size() > 0) {
            for (int i = 0; i < jm.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, (String) jm.getJSONObject(i).get("qId"), (String) jm.getJSONObject(i).get("qt"), (String) jm.getJSONObject(i).get("oId"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 填空题
        JSONArray fb = JSONArray.fromObject(JSONfb);
        if (fb.size() > 0) {
            for (int i = 0; i < fb.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, (String) fb.getJSONObject(i).get("qId"), (String) fb.getJSONObject(i).get("qt"), (String) fb.getJSONObject(i).get("oId"), (String) fb.getJSONObject(i).get("value"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 评分题
        JSONArray s = JSONArray.fromObject(JSONs);
        if (s.size() > 0) {
            for (int i = 0; i < s.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, (String) s.getJSONObject(i).get("qId"), (String) s.getJSONObject(i).get("qt"), (String) s.getJSONObject(i).get("oId"), (String) s.getJSONObject(i).get("value"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        return JSONResult.build();


    }
//    @Override
//    public JSONResult test(String qId) {
////        return JSONResult.build(questionMapper.findLastQuestionByQId(qId));
//        return JSONResult.build(questionMapper.findNextQuestionByQId(qId));
//    }
}
