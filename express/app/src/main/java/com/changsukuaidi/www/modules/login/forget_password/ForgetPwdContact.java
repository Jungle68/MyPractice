package com.changsukuaidi.www.modules.login.forget_password;

import android.view.View;

import com.changsukuaidi.www.base.i.IBasePresenter;
import com.changsukuaidi.www.base.i.IBaseView;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class ForgetPwdContact {
    interface ForgetPwdView extends IBaseView {
        void modifyPwdSuccess();

        void refreshGetVerify(int number);

        void verifyCodePass();
    }

    interface ForgetPwdPresenter extends IBasePresenter {
        void checkVerifyCode(String type, String phone, String verifyCode);

        void getVerifyCode(String type, String phone, View timeView);

        void modifyPwd(String phone, String pwd, String confirmPwd, String verifyCode);
    }
}
