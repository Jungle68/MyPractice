package com.changsukuaidi.www.modules.punch;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.PunchClient;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

class PunchRepository {
    private PunchClient mPunchClient;

    @Inject
    PunchRepository(ClientManager clientManager) {
        mPunchClient = clientManager.providePunchClient();
    }

    Observable<BaseJson<UserBean>> getUserInfo() {
        return mPunchClient.getUserInfo()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<BaseJson<CarBean>> getCarInfo() {
        return mPunchClient.getCarInfo()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<BaseJson<Object>> startPunch(String area) {
        return mPunchClient.startPunch("1", area)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Observable<BaseJson<Object>> offWorkPunch(String area, String distance, String point, double oils) {
        return mPunchClient.offWorkPunch("2", area, distance, point, oils)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
