package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.QuestionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {

    /**
     * 获取所有问题信息列表
     *
     * @return 问题信息列表
     */
    List<QuestionEntity> findAllQuestionList();

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表
     */
    List<QuestionEntity> findAllQuestionByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷的所有问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int findQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> findQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int findNotFrontQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> findNotFrontQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int findFrontQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷被前置的问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> findFrontQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qnId 和 qId 查询当前问卷指定的问题
     *
     * @param qnId 当前问卷编号
     * @param qId  当前问题编号
     * @return 问题信息
     */
    QuestionEntity findQuestionByQnIdAndQId(String qnId, String qId);

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    int insertQuestion(QuestionEntity q);

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    int insertQuestions(QuestionEntity q);
}