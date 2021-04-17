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
    public List<QuestionEntity> getQuestionByQnId(String qnId) {
        return questionMapper.findQuestionByQnId(qnId);
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
}
