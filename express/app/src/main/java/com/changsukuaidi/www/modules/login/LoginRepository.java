package com.changsukuaidi.www.modules.login;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.TokenBean;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.LoginClient;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class LoginRepository{
    private LoginClient mLoginClient;

    @Inject
    LoginRepository(ClientManager clientManager) {
        mLoginClient = clientManager.provideLoginClient();
    }

    public Observable<BaseJson<TokenBean>> login(String account, String pwd, String uuid) {
        return mLoginClient.login(1, uuid, account, pwd);
    }

    public Observable<BaseJson<String>> resetPassword(String username, String password) {
        return mLoginClient.resetPassword(username, password)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseJson<String>> getVerifyCode(String type, String phone) {
        return mLoginClient.getVerifyCode(type, phone)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseJson<String>> checkVerifyCode(String type, String phone, String code) {
        return mLoginClient.checkVerifyCode(type, phone, code)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
    Observable<BaseJson<CarBean>> getCarInfo() {
        return mLoginClient.getCarInfo()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
