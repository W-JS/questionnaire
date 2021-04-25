package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 问卷信息数据的实体类
 */
public class QuestionnaireEntity implements Serializable {

    private String questionnaireId;             // 问卷编号
    private String questionnaireTitle;          // 问卷标题
    private String questionnaireFuTitle;        // 问卷副标题
    private String questionnaireDescription;    // 问卷描述
    private Integer questionnaireFullCount;     // 填写人数
    private Integer questionnaireStatus;        // 状态：-1-已删除; 0-编辑中; 1-已开启; 2-已结束;
    private Date questionnaireCreateTime;       // 问卷创建时间
    private Date questionnaireStartTime;        // 问卷开启时间
    private Date questionnaireEndTime;          // 问卷结束时间
    private Date questionnaireDeleteTime;       // 问卷删除时间
    private String userId;                      // 创建人的用户编号

    public QuestionnaireEntity() {
    }

    public QuestionnaireEntity(String questionnaireId, String questionnaireTitle, String questionnaireFuTitle, String questionnaireDescription, Date questionnaireCreateTime) {
        this.questionnaireId = questionnaireId;
        this.questionnaireTitle = questionnaireTitle;
        this.questionnaireFuTitle = questionnaireFuTitle;
        this.questionnaireDescription = questionnaireDescription;
        this.questionnaireCreateTime = questionnaireCreateTime;
    }

    public QuestionnaireEntity(String questionnaireId, String questionnaireTitle, String questionnaireFuTitle, String questionnaireDescription, Integer questionnaireStatus, Date questionnaireCreateTime, String userId) {
        this.questionnaireId = questionnaireId;
        this.questionnaireTitle = questionnaireTitle;
        this.questionnaireFuTitle = questionnaireFuTitle;
        this.questionnaireDescription = questionnaireDescription;
        this.questionnaireStatus = questionnaireStatus;
        this.questionnaireCreateTime = questionnaireCreateTime;
        this.userId = userId;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getQuestionnaireTitle() {
        return questionnaireTitle;
    }

    public void setQuestionnaireTitle(String questionnaireTitle) {
        this.questionnaireTitle = questionnaireTitle;
    }

    public String getQuestionnaireFuTitle() {
        return questionnaireFuTitle;
    }

    public void setQuestionnaireFuTitle(String questionnaireFuTitle) {
        this.questionnaireFuTitle = questionnaireFuTitle;
    }

    public String getQuestionnaireDescription() {
        return questionnaireDescription;
    }

    public void setQuestionnaireDescription(String questionnaireDescription) {
        this.questionnaireDescription = questionnaireDescription;
    }

    public Integer getQuestionnaireFullCount() {
        return questionnaireFullCount;
    }

    public void setQuestionnaireFullCount(Integer questionnaireFullCount) {
        this.questionnaireFullCount = questionnaireFullCount;
    }

    public Integer getQuestionnaireStatus() {
        return questionnaireStatus;
    }

    public void setQuestionnaireStatus(Integer questionnaireStatus) {
        this.questionnaireStatus = questionnaireStatus;
    }

    public Date getQuestionnaireCreateTime() {
        return questionnaireCreateTime;
    }

    public void setQuestionnaireCreateTime(Date questionnaireCreateTime) {
        this.questionnaireCreateTime = questionnaireCreateTime;
    }

    public Date getQuestionnaireStartTime() {
        return questionnaireStartTime;
    }

    public void setQuestionnaireStartTime(Date questionnaireStartTime) {
        this.questionnaireStartTime = questionnaireStartTime;
    }

    public Date getQuestionnaireEndTime() {
        return questionnaireEndTime;
    }

    public void setQuestionnaireEndTime(Date questionnaireEndTime) {
        this.questionnaireEndTime = questionnaireEndTime;
    }

    public Date getQuestionnaireDeleteTime() {
        return questionnaireDeleteTime;
    }

    public void setQuestionnaireDeleteTime(Date questionnaireDeleteTime) {
        this.questionnaireDeleteTime = questionnaireDeleteTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "QuestionnaireEntity{" +
                "questionnaireId='" + questionnaireId + '\'' +
                ", questionnaireTitle='" + questionnaireTitle + '\'' +
                ", questionnaireFuTitle='" + questionnaireFuTitle + '\'' +
                ", questionnaireDescription='" + questionnaireDescription + '\'' +
                ", questionnaireFullCount=" + questionnaireFullCount +
                ", questionnaireStatus=" + questionnaireStatus +
                ", questionnaireCreateTime=" + questionnaireCreateTime +
                ", questionnaireStartTime=" + questionnaireStartTime +
                ", questionnaireEndTime=" + questionnaireEndTime +
                ", questionnaireDeleteTime=" + questionnaireDeleteTime +
                ", userId='" + userId + '\'' +
                '}';
    }
}
