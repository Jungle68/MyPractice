package com.changsukuaidi.www.bean;

/**
 * @Describe 简单json格式
 * @Author zhouhao
 * @Date 2018/2/24
 * @Contact 605626708@qq.com
 */

public class BaseJson<T> {
    private int status;
    private String message;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
