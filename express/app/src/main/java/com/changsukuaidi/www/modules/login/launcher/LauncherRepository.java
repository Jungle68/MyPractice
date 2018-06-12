package com.changsukuaidi.www.modules.login.launcher;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.PunchClient;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class LauncherRepository {
    private PunchClient mPunchClient;

    @Inject
    LauncherRepository(ClientManager clientManager) {
        mPunchClient = clientManager.providePunchClient();
    }

    Observable<BaseJson<CarBean>> getCarInfo() {
        return mPunchClient.getCarInfo()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
