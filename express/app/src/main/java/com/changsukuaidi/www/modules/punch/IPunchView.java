package com.changsukuaidi.www.modules.punch;

import com.changsukuaidi.www.base.i.IBaseView;
import com.changsukuaidi.www.bean.CarBean;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public interface IPunchView extends IBaseView {
    void punchSuccess();

    void updateTime(String time);

    void loadUserInfo(int status);

    void finishLoadCarInfo(int status);
}
