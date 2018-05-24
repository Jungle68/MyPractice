package com.changsukuaidi.www.bean;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/19
 */

public class BaseBean {
    protected String message; // 状态信息
    protected int code; // 状态码

    public BaseBean() {
    }

    public BaseBean(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
