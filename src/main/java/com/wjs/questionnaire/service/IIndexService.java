package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.util.JSONResult;

import java.util.List;
import java.util.Map;

/**
 * 处理首页信息数据的业务层接口
 */
public interface IIndexService {

    /**
     * 根据 qnId 查询问卷（问卷信息 + 问题信息 + 选项信息）
     *
     * @param qnId 问卷编号
     * @return JSON格式数据：根据 qnId 查询当前问卷
     */
    JSONResult getQuestionnaireByQnId(String qnId);

    /**
     * 根据 qnId 查询问卷信息
     *
     * @param qnId 问卷编号
     * @return 问卷信息
     */
    QuestionnaireEntity getQuestionnaire(String qnId);

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    List<Map<String, Object>> findQuestionByQnIdAndQtId1(String qnId, String qtId);

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    List<Map<String, Object>> findQuestionByQnIdAndQtId2(String qnId, String qtId);

    /**
     * 查询当前问题的当前选项的后置问题
     *
     * @param qId 当前问题编号
     * @param oId 当前选项编号
     * @return JSON格式数据
     */
    JSONResult findRearQuestionByQIdAndOId(String qId, String oId);

//    JSONResult test(String qId);
}
