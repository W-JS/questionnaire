package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.UserCommentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCommentMapper {

    /**
     * 保存用户留言信息
     *
     * @param uc 用户留言信息
     * @return 用户留言是否保存成功
     */
    int insertUserComment(UserCommentEntity uc);
}
