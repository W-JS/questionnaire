package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.QuestionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {

    /**
     * 根据 qnId 查询当前问卷的所有问题（不分页）
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
     * 根据 qnId 查询当前问卷的所有问题（分页）
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> findQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 获取所有问卷的所有问题信息列表（不分页）
     *
     * @return 问题信息列表
     */
    List<QuestionEntity> findAllQuestionList();

    /**
     * 获取所有问卷的所有问题信息列表的行数
     *
     * @return 所有问卷的所有问题的行数
     */
    int findAllQuestionRows();

    /**
     * 获取所有问卷的所有问题信息列表（分页）
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 所有问卷的所有问题
     */
    List<QuestionEntity> findAllQuestionPage(int offset, int limit);

    /**
     * 获取模糊查询 问题标题 的问题信息列表的行数
     *
     * @param qTitle 问题标题
     * @return 模糊查询 问题标题 的问题信息列表的行数
     */
    int findLikeQuestionRowsByQTitle(String qTitle);

    /**
     * 获取模糊查询 问题标题 的问题信息列表（分页）
     *
     * @param qTitle 问题标题
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 模糊查询 问题标题 的问题信息列表
     */
    List<QuestionEntity> findLikeQuestionPageByQTitle(String qTitle, int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int findNoPrependedQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷未被前置的问题（分页）
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> findNoPrependedQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    int findPrependedQuestionRowsByQnId(String qnId);

    /**
     * 根据 qnId 查询当前问卷被前置的问题（分页）
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    List<QuestionEntity> findPrependedQuestionPageByQnId(String qnId, int offset, int limit);

    /**
     * 根据 qId 查询指定的问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity findQuestionByQId(String qId);

    /**
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity findPrependedQuestionByQId(String qId);

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity findRearQuestionByQId(String qId);

    /**
     * 查询当前问题的当前选项的后置问题
     *
     * @param qId 当前问题编号
     * @param oId 当前选项编号
     * @return问题信息
     */
    QuestionEntity findRearQuestionByQIdAndOId(String qId, String oId);

    /**
     * 获取当前问卷的不同题型的问题信息
     *
     * @param qnId 当前问卷编号
     * @param qtId 题型
     * @return 问题信息列表
     */
    List<QuestionEntity> findQuestionByQnIdAndQtId(String qnId, String qtId);

    /**
     * 获取当前记录的上一条记录
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity findLastQuestionByQId(String qId);

    /**
     * 获取当前记录的下一条记录
     *
     * @param qId 当前问题编号
     * @return 问题信息
     */
    QuestionEntity findNextQuestionByQId(String qId);

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

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    int insertQuestionS(QuestionEntity q);

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    int updateQuestion(QuestionEntity q);

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    int updateQuestions(QuestionEntity q);

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    int updateQuestionByQId(QuestionEntity q);

    /**
     * 删除问题信息
     *
     * @param qId 当前问题编号
     * @return 问题是否删除成功
     */
    int deleteQuestionByQId(String qId);
}