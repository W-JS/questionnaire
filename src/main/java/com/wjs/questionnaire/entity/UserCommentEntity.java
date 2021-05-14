package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

public class UserCommentEntity implements Serializable {

    private String usercommentId;       // 用户留言编号
    private String userId;              // 用户编号
    private String questionnaireId;     // 问卷编号
    private String usercommentContent;  // 用户留言内容
    private Integer usercommentStatus;   // 用户留言状态：0-未处理; 1-已处理
    private Date usercommentCreateTime; // 用户留言创建时间

    public UserCommentEntity() {
    }

    public UserCommentEntity(String usercommentId, String userId, String questionnaireId, String usercommentContent, Integer usercommentStatus, Date usercommentCreateTime) {
        this.usercommentId = usercommentId;
        this.userId = userId;
        this.questionnaireId = questionnaireId;
        this.usercommentContent = usercommentContent;
        this.usercommentStatus = usercommentStatus;
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

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getUsercommentContent() {
        return usercommentContent;
    }

    public void setUsercommentContent(String usercommentContent) {
        this.usercommentContent = usercommentContent;
    }

    public Integer getUsercommentStatus() {
        return usercommentStatus;
    }

    public void setUsercommentStatus(Integer usercommentStatus) {
        this.usercommentStatus = usercommentStatus;
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
                ", questionnaireId='" + questionnaireId + '\'' +
                ", usercommentContent='" + usercommentContent + '\'' +
                ", usercommentStatus=" + usercommentStatus +
                ", usercommentCreateTime=" + usercommentCreateTime +
                '}';
    }
}
