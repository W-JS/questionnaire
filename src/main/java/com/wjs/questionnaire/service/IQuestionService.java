package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.QuestionEntity;

import java.util.List;

/**
 * 处理问题信息数据的业务层接口
 */
public interface IQuestionService {

    /**
     * 获取所有问题信息列表
     *
     * @return 问题信息列表
     */
    List<QuestionEntity> getAllQuestionList();


    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表
     */
    List<QuestionEntity> getQuestionByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> getQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷的所有问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int getQuestionRowsByQnId(String qnId);

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    int addQuestion(QuestionEntity q);

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    int addQuestions(QuestionEntity q);
}
