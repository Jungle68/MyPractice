package com.changsukuaidi.www.modules.settings.modify_pwd;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.SettingsClient;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class ModifyPwdRepository {
    private SettingsClient mSettingsClient;

    @Inject
    ModifyPwdRepository(ClientManager clientManager) {
        mSettingsClient = clientManager.provideSettingsClient();
    }

    /**
     * 验证原密码
     *
     * @param password 原密码
     */
    Observable<BaseJson<String>> checkOldPwd(String password) {
        return mSettingsClient.checkOldPwd(password)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改密码
     *
     * @param password 新密码
     */
    Observable<BaseJson<String>> modifyPwd(String password) {
        return mSettingsClient.modifyPwd(password)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
