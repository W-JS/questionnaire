package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;

/**
 * 处理题型信息数据的业务层接口
 */
public interface IQuestionTypeService {

    /**
     * @return JSON格式数据：所有问题类型
     */
    JSONResult getQuestionType();
}
