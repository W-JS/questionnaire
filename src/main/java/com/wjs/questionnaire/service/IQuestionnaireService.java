package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.QuestionnaireEntity;

import java.util.List;

/**
 * 处理问卷信息数据的业务层接口
 */
public interface IQuestionnaireService {

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    List<QuestionnaireEntity> getAllQuestionnaireList();

    /**
     * 根据 qnId 得到问卷信息
     * @param qnId 问卷编号
     * @return 问卷信息
     */
    QuestionnaireEntity getQuestionnaireByQnId(String qnId);

    /**
     * 保存问卷信息
     *
     * @param qn 问卷信息
     * @return 问卷是否保存成功
     */
    int addQuestionnaire(QuestionnaireEntity qn);
}
