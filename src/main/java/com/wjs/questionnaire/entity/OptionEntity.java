package com.wjs.questionnaire.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题信息数据的实体类
 */
public class OptionEntity implements Serializable {

    private String optionId;        // 选项编号
    private String optionContent;   // 选项内容
    private String questionId;      // 问题编号
    private Date optionCreateTime;  // 选项创建时间

    public OptionEntity() {
    }

    public OptionEntity(String optionId, String optionContent, String questionId, Date optionCreateTime) {
        this.optionId = optionId;
        this.optionContent = optionContent;
        this.questionId = questionId;
        this.optionCreateTime = optionCreateTime;
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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Date getOptionCreateTime() {
        return optionCreateTime;
    }

    public void setOptionCreateTime(Date optionCreateTime) {
        this.optionCreateTime = optionCreateTime;
    }

    @Override
    public String toString() {
        return "OptionEntity{" +
                "optionId='" + optionId + '\'' +
                ", optionContent='" + optionContent + '\'' +
                ", questionId='" + questionId + '\'' +
                ", optionCreateTime=" + optionCreateTime +
                '}';
    }
}
