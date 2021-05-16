package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.UserCommentEntity;
import com.wjs.questionnaire.util.JSONResult;

import java.util.List;
import java.util.Map;

/**
 * 处理用户留言信息数据的业务层接口
 */
public interface IUserCommentService {

    /**
     * 根据 ucId 查询留言信息
     *
     * @param ucId 用户编号
     * @return 用户留言信息
     */
    UserCommentEntity getUserCommentByUCId(String ucId);

    /**
     * 获取当前管理员用户已处理的用户留言信息列表
     *
     * @param userId 用户编号
     * @return 已处理的用户留言信息列表
     */
    List<Map<String, Object>> getTreatedUserCommentLists(String userId);

    /**
     * 获取所有的未处理的用户留言信息的行数
     *
     * @return 未处理的用户留言信息的行数
     */
    int getAllUntreatedUserCommentRows();

    /**
     * 获取所有的未处理的用户留言信息列表
     *
     * @return 未处理的用户留言信息列表
     */
    List<Map<String, Object>> getAllUntreatedUserCommentLists();

    /**
     * 保存回复内容
     *
     * @param ucId        用户留言编号
     * @param adminUserId 管理员用户编号
     * @param answer      回复的内容
     * @param answerTime  回复时间
     * @return 回复内容是否保存成功
     */
    JSONResult getUserCommentSubmit(String ucId, String adminUserId, String answer, String answerTime);

}
