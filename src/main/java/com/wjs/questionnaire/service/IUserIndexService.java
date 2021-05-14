package com.wjs.questionnaire.service;

import com.wjs.questionnaire.util.JSONResult;

/**
 * 处理用户首页信息数据的业务层接口
 */
public interface IUserIndexService {

    /**
     * 根据 userId 和 qnId 删除已填写的问卷信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 已填写的问卷信息是否删除成功
     */
    JSONResult getDeleteSubmit(String userId, String qnId);
}
