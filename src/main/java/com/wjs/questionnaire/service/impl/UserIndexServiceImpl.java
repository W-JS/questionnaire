package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.*;
import com.wjs.questionnaire.mapper.*;
import com.wjs.questionnaire.service.IUserIndexService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.UUIDGenerator;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.wjs.questionnaire.util.QuestionnaireConstant.ONLINEUSERID;

@Service
public class UserIndexServiceImpl implements IUserIndexService {

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

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取当前登录用户已填写的问卷
     *
     * @param userId 用户编号
     * @return 已填写的问卷列表
     */
    @Override
    public List<Map<String, Object>> getCompleteQuestionnaireByUserId(String userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findCompleteQuestionnaireByUserId(userId);
        if (questionnaireList != null) {
            for (QuestionnaireEntity questionnaire : questionnaireList) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionnaire", questionnaire);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取当前登录用户未填写的问卷
     *
     * @param userId 用户编号
     * @return 未填写的问卷列表
     */
    @Override
    public List<Map<String, Object>> getNotCompleteQuestionnaireByUserId(String userId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findNotCompleteQuestionnaireByUserId(userId);
        if (questionnaireList != null) {
            for (QuestionnaireEntity questionnaire : questionnaireList) {
                Map<String, Object> map = new HashMap<>();
                map.put("questionnaire", questionnaire);
                list.add(map);
            }
        }
        return list;
    }

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
     * 判断该问卷是否填写和获取问卷信息
     *
     * @param qnId 问卷编号
     * @return 回答状态和问卷信息
     */
    @Override
    public Map<String, Object> getAnswerAndQuestionnaire(String qnId) {
        String userId = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        Map<String, Object> map = new HashMap<>();
        AnswerEntity answer = answerMapper.findAnswerByUserIdAndQNId(userId, qnId);
        if (answer == null) {
            map.put("answer", "true");// 该用户未填写该问卷，可填写
        } else {
            map.put("answer", "false");// 该用户已填写该问卷，不可填写
        }
        map.put("questionnaire", questionnaireMapper.findQuestionnaireByQnId(qnId));
        return map;
    }

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    @Override
    public List<Map<String, Object>> getQuestionByQnIdAndQtId1(String qnId, String qtId) {
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
    public List<Map<String, Object>> getQuestionByQnIdAndQtId2(String qnId, String qtId) {
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
     * 获取当前登录用户填写的该问卷的回答信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 回答信息列表
     */
    @Override
    public JSONResult getAllAnswerByUserIdAndQNId(String userId, String qnId) {
        List<Map<String, Object>> data = new ArrayList<>();
        List<AnswerEntity> answerList = answerMapper.findAllAnswerByUserIdAndQNId(userId, qnId);
        if (answerList != null) {
            Map<String, Object> singleChoiceMap = new HashMap<>();
            Map<String, Object> multipleChoiceMap = new HashMap<>();
            Map<String, Object> judgmentMap = new HashMap<>();
            Map<String, Object> fillBlankMap = new HashMap<>();
            Map<String, Object> scoreMap = new HashMap<>();
            Map<String, Object> ucMap = new HashMap<>();

            List<Map<String, Object>> singleChoiceList = new ArrayList<>();
            List<Map<String, Object>> multipleChoiceList = new ArrayList<>();
            List<Map<String, Object>> judgmentList = new ArrayList<>();
            List<Map<String, Object>> fillBlankList = new ArrayList<>();
            List<Map<String, Object>> scoreList = new ArrayList<>();

            for (AnswerEntity answer : answerList) {
                if ("singleChoice".equals(answer.getQuestiontypeId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("answer", answer);
                    singleChoiceList.add(map);
                } else if ("multipleChoice".equals(answer.getQuestiontypeId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("answer", answer);
                    multipleChoiceList.add(map);
                } else if ("judgment".equals(answer.getQuestiontypeId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("answer", answer);
                    judgmentList.add(map);
                } else if ("fillBlank".equals(answer.getQuestiontypeId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("answer", answer);
                    fillBlankList.add(map);
                } else if ("score".equals(answer.getQuestiontypeId())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("answer", answer);
                    scoreList.add(map);
                }
            }

            singleChoiceMap.put("qt", "singleChoice");
            singleChoiceMap.put("singleChoice", singleChoiceList);
            multipleChoiceMap.put("qt", "multipleChoice");
            multipleChoiceMap.put("multipleChoice", multipleChoiceList);
            judgmentMap.put("qt", "judgment");
            judgmentMap.put("judgment", judgmentList);
            fillBlankMap.put("qt", "fillBlank");
            fillBlankMap.put("fillBlank", fillBlankList);
            scoreMap.put("qt", "score");
            scoreMap.put("score", scoreList);
            ucMap.put("qt", "userComment");
            ucMap.put("userComment", userCommentMapper.findUserCommentByUserIdAndQNId(userId, qnId));

            data.add(singleChoiceMap);
            data.add(multipleChoiceMap);
            data.add(judgmentMap);
            data.add(fillBlankMap);
            data.add(scoreMap);
            data.add(ucMap);
        }
        JSONResult jsonResult;
        if (data.size() != 0) {
            jsonResult = JSONResult.build(data);
        } else {
            jsonResult = JSONResult.build("您暂未填写的该问卷！！！");
        }
        return jsonResult;
    }

    /**
     * 查询当前问题的当前选项的后置问题
     *
     * @param qId 当前问题编号
     * @param oId 当前选项编号
     * @return JSON格式数据
     */
    @Override
    public JSONResult getRearQuestionByQIdAndOId(String qId, String oId) {
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
     * @param qnId   问卷编号
     * @param JSONsc 单项选择题
     * @param JSONmc 多项选择题
     * @param JSONjm 判断题
     * @param JSONfb 填空题
     * @param JSONs  评分题
     * @return 用户填写的问卷信息是否保存成功
     */
    @Override
    public JSONResult saveSubmit(String userId, String qnId, String JSONsc, String JSONmc, String JSONjm, String JSONfb, String JSONs, String userComments) {
        Date date = new Date();

        // 用户留言
        UserCommentEntity userComment = new UserCommentEntity(UUIDGenerator.get16UUID(), userId, qnId, userComments, 0, date);
        userCommentMapper.insertUserComment(userComment);

        // 单项选择题
        JSONArray sc = JSONArray.fromObject(JSONsc);
        if (sc.size() > 0) {
            for (int i = 0; i < sc.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, qnId, (String) sc.getJSONObject(i).get("qId"), (String) sc.getJSONObject(i).get("qt"), (String) sc.getJSONObject(i).get("oId"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 多项选择题
        JSONArray mc = JSONArray.fromObject(JSONmc);
        if (mc.size() > 0) {
            for (int i = 0; i < mc.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, qnId, (String) mc.getJSONObject(i).get("qId"), (String) mc.getJSONObject(i).get("qt"), (String) mc.getJSONObject(i).get("oId"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 判断题
        JSONArray jm = JSONArray.fromObject(JSONjm);
        if (jm.size() > 0) {
            for (int i = 0; i < jm.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, qnId, (String) jm.getJSONObject(i).get("qId"), (String) jm.getJSONObject(i).get("qt"), (String) jm.getJSONObject(i).get("oId"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 填空题
        JSONArray fb = JSONArray.fromObject(JSONfb);
        if (fb.size() > 0) {
            for (int i = 0; i < fb.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, qnId, (String) fb.getJSONObject(i).get("qId"), (String) fb.getJSONObject(i).get("qt"), (String) fb.getJSONObject(i).get("oId"), (String) fb.getJSONObject(i).get("value"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        // 评分题
        JSONArray s = JSONArray.fromObject(JSONs);
        if (s.size() > 0) {
            for (int i = 0; i < s.size(); i++) {
                date.setTime(date.getTime() + 1000);
                AnswerEntity answer = new AnswerEntity(UUIDGenerator.get16UUID(), userId, qnId, (String) s.getJSONObject(i).get("qId"), (String) s.getJSONObject(i).get("qt"), (String) s.getJSONObject(i).get("oId"), (String) s.getJSONObject(i).get("value"), date);
                answerMapper.insertAnswer(answer);
            }
        }

        return JSONResult.build();


    }

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
