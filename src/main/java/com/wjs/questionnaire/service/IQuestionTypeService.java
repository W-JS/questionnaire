package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.QuestionTypeEntity;

import java.util.List;

/**
 * 处理题型信息数据的业务层接口
 */
public interface IQuestionTypeService {

    /**
     * 获取所有题型信息列表
     *
     * @return 题型信息列表
     */
    List<QuestionTypeEntity> getAllQuestionTypeList();

    /**
     * 保存题型信息
     *
     * @param qt 题型信息
     * @return 题型是否保存成功
     */
    int addQuestionType(QuestionTypeEntity qt);
}
