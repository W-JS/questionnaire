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

    /**
     * 获取 用户对该问题的回答情况（该问题的每个选项分别有多少人选择）
     *
     * @param qnId 问卷编号
     * @param qId  问题编号
     * @return 单选选择题每个选项的数量
     */
    JSONResult getOptionsNumberByQNIdAndQId(String qnId, String qId);
}
