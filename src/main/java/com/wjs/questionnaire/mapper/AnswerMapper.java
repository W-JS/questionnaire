package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.AnswerEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnswerMapper {

    /**
     * 保存回答信息
     *
     * @param a 回答信息
     * @return 回答是否保存成功
     */
    int insertAnswer(AnswerEntity a);
}
