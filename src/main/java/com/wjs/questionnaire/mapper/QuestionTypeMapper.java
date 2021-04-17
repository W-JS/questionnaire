package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.QuestionTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionTypeMapper {

    /**
     * 获取所有题型信息列表
     *
     * @return 题型信息列表
     */
    List<QuestionTypeEntity> findAllQuestionTypeList();

    /**
     * 保存题型信息
     *
     * @param qt 题型信息
     * @return 题型是否保存成功
     */
    int insertQuestionType(QuestionTypeEntity qt);
}