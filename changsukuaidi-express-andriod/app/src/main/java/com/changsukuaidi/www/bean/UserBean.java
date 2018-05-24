package com.changsukuaidi.www.bean;

import java.io.Serializable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/27
 * @Contact 605626708@qq.com
 */

public class UserBean implements Serializable{
    /**
     * uid : 184
     * nickname : 13568476886
     * avatar : http://csd.jipushop.com/Public/Home/defaulted-mobile/images/avatar-default.png
     * mobile_bind : 0
     * wechat_bind : 0
     * mobile : 13438028251
     */

    private Long uid;
    private String nickname;
    private String avatar;
    private String mobile;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
