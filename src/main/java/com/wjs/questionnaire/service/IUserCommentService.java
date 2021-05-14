package com.wjs.questionnaire.service;

import com.wjs.questionnaire.entity.UserCommentEntity;

import java.util.List;

/**
 * 处理用户留言信息数据的业务层接口
 */
public interface IUserCommentService {

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
    List<UserCommentEntity> getAllUntreatedUserCommentLists();

}
