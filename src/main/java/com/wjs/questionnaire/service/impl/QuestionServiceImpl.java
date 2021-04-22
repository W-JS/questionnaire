package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.mapper.QuestionMapper;
import com.wjs.questionnaire.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 获取所有问题信息列表
     *
     * @return 问题信息列表
     */
    @Override
    public List<QuestionEntity> getAllQuestionList() {
        return questionMapper.findAllQuestionList();
    }

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表
     */
    @Override
    public List<QuestionEntity> getAllQuestionByQnId(String qnId) {
        return questionMapper.findAllQuestionByQnId(qnId);
    }

    /**
     * 根据 qnId 查询当前问卷的所有问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    @Override
    public int getQuestionRowsByQnId(String qnId) {
        return questionMapper.findQuestionRowsByQnId(qnId);
    }

    /**
     * 根据 qnId 查询当前问卷的所有问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    @Override
    public List<QuestionEntity> getQuestionPageByQnId(String qnId, int offset, int limit) {
        return questionMapper.findQuestionPageByQnId(qnId, offset, limit);
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题的行数
     *
     * @param qnId 当前问卷编号
     * @return 问题信息列表的行数
     */
    @Override
    public int getNoPrependedQuestionRowsByQnId(String qnId) {
        return questionMapper.findNoPrependedQuestionRowsByQnId(qnId);
    }

    /**
     * 根据 qnId 查询当前问卷未被前置的问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    @Override
    public List<QuestionEntity> getNoPrependedQuestionPageByQnId(String qnId, int offset, int limit) {
        return questionMapper.findNoPrependedQuestionPageByQnId(qnId, offset, limit);
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题的行数
     *
     * @param qnId   当前问卷编号
     * @return 问题信息列表的行数
     */
    @Override
    public int getPrependedQuestionRowsByQnId(String qnId) {
        return questionMapper.findPrependedQuestionRowsByQnId(qnId);
    }

    /**
     * 根据 qnId 查询当前问卷被前置的问题
     *
     * @param qnId   当前问卷编号
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问题信息列表
     */
    @Override
    public List<QuestionEntity> getPrependedQuestionPageByQnId(String qnId, int offset, int limit) {
        return questionMapper.findPrependedQuestionPageByQnId(qnId, offset, limit);
    }

    /**
     * 根据 qnId 和 qId 查询当前问卷指定的问题
     *
     * @param qnId 当前问卷编号
     * @param qId  当前问题编号
     * @return 问题信息
     */
    @Override
    public QuestionEntity getQuestionByQnIdAndQId(String qnId, String qId) {
        return questionMapper.findQuestionByQnIdAndQId(qnId, qId);
    }

    /**
     * 如果当前问题是被前置问题，则找到当前问题的后置问题
     *
     * @param qnId 当前问卷编号
     * @param qId  当前问题编号
     * @return 问题信息
     */
    @Override
    public QuestionEntity getRearQuestionByPrependedByQnIdAndQId(String qnId, String qId) {
        return questionMapper.findRearQuestionByPrependedByQnIdAndQId(qnId, qId);
    }

    /**
     * 如果当前问题有前置问题，则找到当前问题的前置问题
     *
     * @param qnId 当前问卷编号
     * @param qId  当前问题编号
     * @return 问题信息
     */
    @Override
    public QuestionEntity getPrependedQuestionByRearByQnIdAndQId(String qnId, String qId) {
        return questionMapper.findPrependedQuestionByRearByQnIdAndQId(qnId, qId);
    }

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    @Override
    public int addQuestion(QuestionEntity q) {
        return questionMapper.insertQuestion(q);
    }

    /**
     * 保存问题信息
     *
     * @param q 问题信息
     * @return 问题是否保存成功
     */
    @Override
    public int addQuestions(QuestionEntity q) {
        return questionMapper.insertQuestions(q);
    }

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    @Override
    public int modifyQuestion(QuestionEntity q) {
        return questionMapper.updateQuestion(q);
    }

    /**
     * 更新问题信息
     *
     * @param q 问题信息
     * @return 问题是否更新成功
     */
    @Override
    public int modifyQuestions(QuestionEntity q) {
        return questionMapper.updateQuestions(q);
    }
}
