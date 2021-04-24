package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;

import java.util.List;
import java.util.Map;

/**
 * 处理问卷信息数据的业务层接口
 */
public interface IQuestionnaireService {

    /**
     * 获取所有问卷信息列表
     *
     * @return 问卷信息列表
     */
    List<Map<String, Object>> getAllQuestionnaireList();

    /**
     * 保存问卷信息
     *
     * @param qnTitle       问卷标题
     * @param qnFuTitle     问卷副标题
     * @param qnDescription 问卷描述
     * @param qnStatus      状态：-1-已删除; 0-编辑中; 1-已开启; 2-已结束;
     * @param qnCreateTime  问卷创建时间
     * @param userId        创建人的用户编号
     * @return 问卷信息是否保存成功
     */
    JSONResult getQuestionnaireSubmit(String qnTitle, String qnFuTitle, String qnDescription, int qnStatus, String qnCreateTime, String userId);


    /**
     * 根据 qnId 得到问卷信息
     *
     * @return 问卷信息
     */
    JSONResult getQuestionnaireByQnId();

    /**
     * @return 存了 user 信息的 map
     */
    Map<String, Object> GetOnlineUser();
}
