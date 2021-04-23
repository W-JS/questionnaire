package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题信息数据的实体类
 */
public class QuestionEntity implements Serializable {

    private String questionId;          // 问题编号
    private String questionTitle;       // 问题标题
    private String questionDescription; // 问题描述
    private Integer questionStatus;     // 问题状态：0-非必填; 1-必填;
    private String preQuestionId;       // 前置问题
    private String preOptionId;         // 前置选项
    private String questionnaireId;     // 问卷编号
    private String questiontypeId;      // 问题类型：singleChoice-单项选择题; multipleChoice-多项选择题; judgment-判断题; fillBlank-填空题; score-评分题;
    private Date questionCreateTime;    // 问题创建时间

    public QuestionEntity() {
    }

    // 无前置问题和前置选项
    public QuestionEntity(String questionId, String questionTitle, String questionDescription, Integer questionStatus, String questionnaireId, String questiontypeId, Date questionCreateTime) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionDescription = questionDescription;
        this.questionStatus = questionStatus;
        this.questionnaireId = questionnaireId;
        this.questiontypeId = questiontypeId;
        this.questionCreateTime = questionCreateTime;
    }

    // 完整
    public QuestionEntity(String questionId, String questionTitle, String questionDescription, Integer questionStatus, String preQuestionId, String preOptionId, String questionnaireId, String questiontypeId, Date questionCreateTime) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.questionDescription = questionDescription;
        this.questionStatus = questionStatus;
        this.preQuestionId = preQuestionId;
        this.preOptionId = preOptionId;
        this.questionnaireId = questionnaireId;
        this.questiontypeId = questiontypeId;
        this.questionCreateTime = questionCreateTime;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public Integer getQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(Integer questionStatus) {
        this.questionStatus = questionStatus;
    }

    public String getPreQuestionId() {
        return preQuestionId;
    }

    public void setPreQuestionId(String preQuestionId) {
        this.preQuestionId = preQuestionId;
    }

    public String getPreOptionId() {
        return preOptionId;
    }

    public void setPreOptionId(String preOptionId) {
        this.preOptionId = preOptionId;
    }

    public String getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getQuestiontypeId() {
        return questiontypeId;
    }

    public void setQuestiontypeId(String questiontypeId) {
        this.questiontypeId = questiontypeId;
    }

    public Date getQuestionCreateTime() {
        return questionCreateTime;
    }

    public void setQuestionCreateTime(Date questionCreateTime) {
        this.questionCreateTime = questionCreateTime;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "questionId='" + questionId + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", questionDescription='" + questionDescription + '\'' +
                ", questionStatus=" + questionStatus +
                ", preQuestionId='" + preQuestionId + '\'' +
                ", preOptionId='" + preOptionId + '\'' +
                ", questionnaireId='" + questionnaireId + '\'' +
                ", questiontypeId='" + questiontypeId + '\'' +
                ", questionCreateTime=" + questionCreateTime +
                '}';
    }
}
