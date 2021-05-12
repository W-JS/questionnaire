package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

public class UserCommentEntity implements Serializable {

    private String usercommentId;       // 用户留言编号
    private String userId;              // 用户编号
    private String usercommentContent;  // 用户留言内容
    private Date usercommentCreateTime; // 用户留言创建时间

    public UserCommentEntity() {
    }

    public UserCommentEntity(String usercommentId, String userId, String usercommentContent, Date usercommentCreateTime) {
        this.usercommentId = usercommentId;
        this.userId = userId;
        this.usercommentContent = usercommentContent;
        this.usercommentCreateTime = usercommentCreateTime;
    }

    public String getUsercommentId() {
        return usercommentId;
    }

    public void setUsercommentId(String usercommentId) {
        this.usercommentId = usercommentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsercommentContent() {
        return usercommentContent;
    }

    public void setUsercommentContent(String usercommentContent) {
        this.usercommentContent = usercommentContent;
    }

    public Date getUsercommentCreateTime() {
        return usercommentCreateTime;
    }

    public void setUsercommentCreateTime(Date usercommentCreateTime) {
        this.usercommentCreateTime = usercommentCreateTime;
    }

    @Override
    public String toString() {
        return "UserCommentEntity{" +
                "usercommentId='" + usercommentId + '\'' +
                ", userId='" + userId + '\'' +
                ", usercommentContent='" + usercommentContent + '\'' +
                ", usercommentCreateTime=" + usercommentCreateTime +
                '}';
    }
}
