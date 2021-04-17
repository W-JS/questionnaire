package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题信息数据的实体类
 */
public class QuestionTypeEntity implements Serializable {

    private String questiontypeId;          // 题型编号
    private String questiontypeName;        // 题型名称
    private Date questiontypeCreateTime;    // 题型创建时间

    public QuestionTypeEntity() {
    }

    public QuestionTypeEntity(String questiontypeId, String questiontypeName, Date questiontypeCreateTime) {
        this.questiontypeId = questiontypeId;
        this.questiontypeName = questiontypeName;
        this.questiontypeCreateTime = questiontypeCreateTime;
    }

    public String getQuestiontypeId() {
        return questiontypeId;
    }

    public void setQuestiontypeId(String questiontypeId) {
        this.questiontypeId = questiontypeId;
    }

    public String getQuestiontypeName() {
        return questiontypeName;
    }

    public void setQuestiontypeName(String questiontypeName) {
        this.questiontypeName = questiontypeName;
    }

    public Date getQuestiontypeCreateTime() {
        return questiontypeCreateTime;
    }

    public void setQuestiontypeCreateTime(Date questiontypeCreateTime) {
        this.questiontypeCreateTime = questiontypeCreateTime;
    }

    @Override
    public String toString() {
        return "QuestionTypeEntity{" +
                "questiontypeId='" + questiontypeId + '\'' +
                ", questiontypeName='" + questiontypeName + '\'' +
                ", questiontypeCreateTime=" + questiontypeCreateTime +
                '}';
    }
}
