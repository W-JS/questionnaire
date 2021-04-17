package com.wjs.questionnaire.util;

/**
 * 响应JSON结果的类
 * @param <T> 响应的数据的类型
 */
@Deprecated
public class ResponseJSONResult<T> {

    // 响应业务状态
    private Integer state;

    // 响应消息
    private String message;

    // 响应中的数据
    private T data;

    public ResponseJSONResult() {
        super();
    }

    public ResponseJSONResult(Integer state) {
        super();
        this.state = state;
    }

    public ResponseJSONResult(Integer state, T data) {
        super();
        this.state = state;
        this.data = data;
    }

    public ResponseJSONResult(Integer state, String message, T data) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
