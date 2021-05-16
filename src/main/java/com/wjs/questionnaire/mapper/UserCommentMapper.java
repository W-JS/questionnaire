package com.wjs.questionnaire.mapper;

import com.wjs.questionnaire.entity.UserCommentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserCommentMapper {

    /**
     * 根据 ucId 查询留言信息
     *
     * @param ucId 用户编号
     * @return 用户留言信息
     */
    UserCommentEntity findUserCommentByUCId(String ucId);

    /**
     * 根据 userId 和 qnId 查询用户留言信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 用户留言信息
     */
    UserCommentEntity findUserCommentByUserIdAndQNId(String userId, String qnId);

    /**
     * 获取当前管理员用户已处理的用户留言信息列表
     *
     * @param userId 用户编号
     * @return 已处理的用户留言信息列表
     */
    List<UserCommentEntity> findTreatedUserCommentLists(String userId);

    /**
     * 获取所有的未处理的用户留言信息的行数
     *
     * @return 未处理的用户留言信息的行数
     */
    int findAllUntreatedUserCommentRows();

    /**
     * 获取所有的未处理的用户留言信息列表
     *
     * @return 未处理的用户留言信息列表
     */
    List<UserCommentEntity> findAllUntreatedUserCommentLists();

    /**
     * 保存用户留言信息
     *
     * @param uc 用户留言信息
     * @return 用户留言是否保存成功
     */
    int insertUserComment(UserCommentEntity uc);

    /**
     * 根据 userId 和 qnId 删除用户留言信息
     *
     * @param userId 用户编号
     * @param qnId   问卷编号
     * @return 用户留言信息是否删除成功
     */
    int deleteUserCommentByUserIdAndQNId(String userId, String qnId);

    /**
     * 根据 ucId 修改用户留言信息
     *
     * @param uc 用户留言信息
     * @return 用户信息是否修改成功
     */
    int updateUserCommentByUCId(UserCommentEntity uc);
}
