package com.wjs.questionnaire.util;

import static com.wjs.questionnaire.util.QuestionnaireConstant.OBJECT_EXISTENCE;
import static com.wjs.questionnaire.util.QuestionnaireConstant.OBJECT_INEXISTENCE;

public class JSONResult {

    // 响应业务状态
    private Integer state;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;

    public JSONResult() {
        this.state = null;
        this.message = null;
        this.data = null;
    }

    public JSONResult(Object data) {
        this.state = OBJECT_EXISTENCE;
        this.message = "SUCCESS";
        this.data = data;
    }

    public JSONResult(Integer state, Object data) {
        this.state = state;
        this.data = data;
    }

    public JSONResult(Integer state, String message, Object data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return 成功状态码和成功消息
     */
    public static JSONResult build() {
        return new JSONResult(null);
    }

    /**
     * @param message 消息
     * @return JSON格式的错误状态码和消息
     */
    public static JSONResult build(String message) {
        return new JSONResult(OBJECT_INEXISTENCE, message, null);
    }

    /**
     * @param data 需要转换为 JSON 格式的数据
     * @return JSON格式的数据
     */
    public static JSONResult build(Object data) {
        return new JSONResult(data);
    }

    /**
     * @param errno   错误状态码
     * @param message 错误消息
     * @return JSON格式的错误状态码和错误消息
     */
    public static JSONResult build(Integer errno, String message) {
        return new JSONResult(errno, message, null);
    }

    /**
     * @param state   状态码
     * @param message 消息
     * @param data    数据
     * @return JSON格式的状态码、消息和数据
     */
    public static JSONResult build(Integer state, String message, Object data) {
        return new JSONResult(state, message, data);
    }

}
