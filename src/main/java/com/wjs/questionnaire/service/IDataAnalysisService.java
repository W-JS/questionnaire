package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;

/**
 * 处理问题信息数据的业务层接口
 */
public interface IDataAnalysisService {

    /**
     * 获取 总人数+填写该问卷的人数+未填写该问卷的人数
     *
     * @param qnId 问卷编号
     * @return 人数
     */
    JSONResult getWriteQuestionnaireNumberByQNId(String qnId);
}
