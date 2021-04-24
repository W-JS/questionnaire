package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.QuestionTypeEntity;
import com.wjs.questionnaire.util.JSONResult;

import java.util.List;

/**
 * 处理题型信息数据的业务层接口
 */
public interface IQuestionTypeService {

    /**
     * @return JSON格式数据：所有问题类型
     */
    JSONResult getQuestionType();
}
