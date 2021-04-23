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
    List<QuestionEntity> getAllQuestionByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷的所有问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int getQuestionRowsByQnId(String qnId);

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
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int getNoPrependedQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> getNoPrependedQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int getPrependedQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷被前置的问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> getPrependedQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qId 查询指定的问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity getQuestionByQId(String qId);

    /**
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity getPrependedQuestionByQId(String qId);

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity getRearQuestionByQId(String qId);

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

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    int addQuestionS(QuestionEntity q);

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    int modifyQuestion(QuestionEntity q);

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    int modifyQuestions(QuestionEntity q);

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    int modifyQuestionByQId(QuestionEntity q);

    /**
     * 删除问题信息
     *
     * @param qId 当前问题编号
     * @return 问题是否删除成功
     */
    int deleteQuestionByQId(String qId);
}
