package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.QuestionnaireEntity;
import com.wjs.questionnaire.mapper.QuestionnaireMapper;
import com.wjs.questionnaire.service.IQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionnaireServiceImpl implements IQuestionnaireService {

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    @Override
    public List<QuestionnaireEntity> getAllQuestionnaireList() {
        return questionnaireMapper.findAllQuestionnaireList();
    }

    /**
     * 根据 qnId 得到问卷信息
     * @param qnId 问卷编号
     * @return 问卷信息
     */
    @Override
    public QuestionnaireEntity getQuestionnaireByQnId(String qnId) {
        return questionnaireMapper.findQuestionnaireByQnId(qnId);
    }

    /**
     * 保存问卷信息
     *
     * @param qn 问卷信息
     * @return 问卷是否保存成功
     */
    @Override
    public int addQuestionnaire(QuestionnaireEntity qn) {
        return questionnaireMapper.insertQuestionnaire(qn);
    }
}
