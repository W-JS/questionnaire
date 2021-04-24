package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.QuestionEntity;
import com.wjs.questionnaire.entity.QuestionnaireEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionnaireMapper {

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    List<QuestionnaireEntity> findAllQuestionnaireList();

    /**
     * 获取所有问卷信息列表的行数
     *
     * @return 问卷信息列表的行数
     */
    int findQuestionnaireRows();

    /**
     * 获取所有问卷信息列表
     *
     * @param offset 从第几条数据查询
     * @param limit  需要查询的记录条数
     * @return 问卷信息列表
     */
    List<QuestionnaireEntity> findQuestionnairePage(int offset, int limit);

    /**
     * 根据 qnId 得到问卷信息
     * @param qnId 问卷编号
     * @return 问卷信息
     */
    QuestionnaireEntity findQuestionnaireByQnId(String qnId);

    /**
     * 保存问卷信息
     *
     * @param qn 问卷信息
     * @return 问卷是否保存成功
     */
    int insertQuestionnaire(QuestionnaireEntity qn);
}
