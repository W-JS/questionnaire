package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.entity.UserEntity;
import com.wjs.questionnaire.mapper.QuestionnaireMapper;
import com.wjs.questionnaire.mapper.UserMapper;
import com.wjs.questionnaire.service.IQuestionnaireService;
import com.wjs.questionnaire.util.JSONResult;
import com.wjs.questionnaire.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wjs.questionnaire.util.DateUtil.StringToDate;
import static com.wjs.questionnaire.util.QuestionnaireConstant.ONLINEUSERID;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OnlineQNID;

@Service
public class QuestionnaireServiceImpl implements IQuestionnaireService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    @Override
    public List<Map<String, Object>> getAllQuestionnaireList() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findAllQuestionnaireList();
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
     * 获取所有问卷信息列表的行数
     *
     * @return 问卷信息列表的行数
     */
    @Override
    public int getQuestionnaireRows() {
        return questionnaireMapper.findQuestionnaireRows();
    }

    /**
     * 获取所有问卷信息列表
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问卷信息列表
     */
    @Override
    public List<Map<String, Object>> findQuestionnairePage(int offset, int limit) {
        List<Map<String, Object>> list = new ArrayList<>();

        List<QuestionnaireEntity> questionnaireList = questionnaireMapper.findQuestionnairePage(offset, limit);

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
     * 保存问卷信息
     *
     * @param qnTitle       问卷标题
     * @param qnFuTitle     问卷副标题
     * @param qnDescription 问卷描述
     * @param qnStatus      状态：-1-已删除; 0-编辑中; 1-已开启; 2-已结束;
     * @param qnCreateTime  问卷创建时间
     * @param userId        创建人的用户编号
     * @return 问卷信息是否保存成功
     */
    @Override
    public JSONResult getQuestionnaireSubmit(String qnTitle, String qnFuTitle, String qnDescription, int qnStatus, String qnCreateTime, String userId) {
        String qnId = UUIDGenerator.get16UUID();
        QuestionnaireEntity questionnaire = new QuestionnaireEntity(qnId, qnTitle, qnFuTitle, qnDescription, qnStatus, StringToDate(qnCreateTime), userId);
        int flag = questionnaireMapper.insertQuestionnaire(questionnaire);
        JSONResult jsonResult;
        if (flag == 1) {
            System.out.println(questionnaire);
            jsonResult = JSONResult.build();
            redisTemplate.opsForValue().set(OnlineQNID, qnId);// 成功保存问卷信息，将 qnId 存进Redis
        } else {
            jsonResult = JSONResult.build("问卷信息保存失败！！！");
        }
        return jsonResult;
    }

    /**
     * 根据 qnId 得到问卷信息
     *
     * @return 问卷信息
     */
    @Override
    public JSONResult getQuestionnaireByQnId() {
        String qnId = (String) redisTemplate.opsForValue().get(OnlineQNID);
        QuestionnaireEntity questionnaire = questionnaireMapper.findQuestionnaireByQnId(qnId);
        JSONResult jsonResult;
        if (questionnaire != null) {
            jsonResult = JSONResult.build(questionnaire);
        } else {
            jsonResult = JSONResult.build("当前问卷信息不存在！！！");
        }
        return jsonResult;
    }

    /**
     * @return 存了 user 信息的 map
     */
    @Override
    public Map<String, Object> GetOnlineUser() {
        String OnlineUserID = (String) redisTemplate.opsForValue().get(ONLINEUSERID);
        UserEntity user = userMapper.findUserByUserId(OnlineUserID);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return map;
    }
}
