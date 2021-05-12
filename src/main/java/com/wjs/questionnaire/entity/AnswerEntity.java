package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 选项信息数据的实体类
 */
public class AnswerEntity implements Serializable {

    private String answerId;        // 回答编号
    private String userId;          // 用户编号
    private String questionId;      // 问题编号
    private String questiontypeId;  // 题型编号
    private String optionId;        // 选项编号
    private String optionContent;   // 选项内容
    private Date answerCreateTime;  // 回答创建时间

    public AnswerEntity() {
    }

    public AnswerEntity(String answerId, String userId, String questionId, String questiontypeId, String optionId, Date answerCreateTime) {
        this.answerId = answerId;
        this.userId = userId;
        this.questionId = questionId;
        this.questiontypeId = questiontypeId;
        this.optionId = optionId;
        this.answerCreateTime = answerCreateTime;
    }

    public AnswerEntity(String answerId, String userId, String questionId, String questiontypeId, String optionId, String optionContent, Date answerCreateTime) {
        this.answerId = answerId;
        this.userId = userId;
        this.questionId = questionId;
        this.questiontypeId = questiontypeId;
        this.optionId = optionId;
        this.optionContent = optionContent;
        this.answerCreateTime = answerCreateTime;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestiontypeId() {
        return questiontypeId;
    }

    public void setQuestiontypeId(String questiontypeId) {
        this.questiontypeId = questiontypeId;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }

    public Date getAnswerCreateTime() {
        return answerCreateTime;
    }

    public void setAnswerCreateTime(Date answerCreateTime) {
        this.answerCreateTime = answerCreateTime;
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
                "answerId='" + answerId + '\'' +
                ", userId='" + userId + '\'' +
                ", questionId='" + questionId + '\'' +
                ", questiontypeId='" + questiontypeId + '\'' +
                ", optionId='" + optionId + '\'' +
                ", optionContent='" + optionContent + '\'' +
                ", answerCreateTime=" + answerCreateTime +
                '}';
    }
}
