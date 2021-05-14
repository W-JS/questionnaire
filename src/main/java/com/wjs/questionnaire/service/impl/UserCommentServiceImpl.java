package com.wjs.questionnaire.service.impl;

import com.wjs.questionnaire.entity.UserCommentEntity;
import com.wjs.questionnaire.mapper.UserCommentMapper;
import com.wjs.questionnaire.service.IUserCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentServiceImpl implements IUserCommentService {

    @Autowired
    private UserCommentMapper userCommentMapper;

    /**
     * 获取所有的未处理的用户留言信息的行数
     *
     * @return 未处理的用户留言信息的行数
     */
    @Override
    public int getAllUntreatedUserCommentRows() {
        return userCommentMapper.findAllUntreatedUserCommentRows();
    }

    /**
     * 获取所有的未处理的用户留言信息列表
     *
     * @return 未处理的用户留言信息列表
     */
    @Override
    public List<UserCommentEntity> getAllUntreatedUserCommentLists() {
        return userCommentMapper.findAllUntreatedUserCommentLists();
    }
}
