package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.UserCommentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCommentMapper {

    /**
     * 根据 userId 和 qnId 查询用户留言信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 用户留言信息
     */
    UserCommentEntity findUserCommentByUserIdAndQNId(String userId, String qnId);

    /**
     * 保存用户留言信息
     *
     * @param uc 用户留言信息
     * @return 用户留言是否保存成功
     */
    int insertUserComment(UserCommentEntity uc);
}
