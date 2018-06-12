package com.changsukuaidi.www.bean;

import java.io.Serializable;

public class TokenBean implements Serializable {

    /**
     * uid : 1
     * device_code : 7jQWgKFBEg2_dyrlV6tMF63B-xnT3sCpReKLghFCxyk
     */
    private int uid;
    private String device_code;

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}