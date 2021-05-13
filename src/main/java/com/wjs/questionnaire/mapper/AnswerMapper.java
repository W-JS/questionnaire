package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.AnswerEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerMapper {

    /**
     * 获取当前登录用户是否已经填写该问卷（有数据：已填写，无数据：未填写）
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 回答信息
     */
    AnswerEntity findAnswerByUserIdAndQNId(String userId, String qnId);

    /**
     * 获取当前登录用户填写的该问卷的回答信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 回答信息列表
     */
    List<AnswerEntity> findAllAnswerByUserIdAndQNId(String userId, String qnId);

    /**
     * 保存回答信息
     *
     * @param a 回答信息
     * @return 回答是否保存成功
     */
    int insertAnswer(AnswerEntity a);
}
