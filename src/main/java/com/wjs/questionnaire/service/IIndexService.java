package com.wjs.questionnaire.service;

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
     * 判断该问卷是否填写和获取问卷信息
     *
     * @param qnId 问卷编号
     * @return 回答状态和问卷信息
     */
    Map<String, Object> getAnswerAndQuestionnaire(String qnId);

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    List<Map<String, Object>> getQuestionByQnIdAndQtId1(String qnId, String qtId);

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    List<Map<String, Object>> getQuestionByQnIdAndQtId2(String qnId, String qtId);

    /**
     * 获取当前登录用户填写的该问卷的回答信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 回答信息列表
     */
    JSONResult getAllAnswerByUserIdAndQNId(String userId, String qnId);

    /**
     * 查询当前问题的当前选项的后置问题
     *
     * @param qId 当前问题编号
     * @param oId 当前选项编号
     * @return JSON格式数据
     */
    JSONResult getRearQuestionByQIdAndOId(String qId, String oId);

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
    JSONResult saveSubmit(String userId, String qnId, String JSONsc, String JSONmc, String JSONjm, String JSONfb, String JSONs, String userComments);


//    JSONResult test(String qId);
}
