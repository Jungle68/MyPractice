package com.changsukuaidi.www.modules.home;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.remote.ClientManager;
import com.changsukuaidi.www.remote.HomeClient;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

class HomeRepository {

    private HomeClient mHomeClient;

    @Inject
    HomeRepository(ClientManager clientManager) {
        mHomeClient = clientManager.provideHomeClient();
    }

    Observable<BaseJson<UserBean>> getUserInfo() {
        return mHomeClient.getUserInfo()
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
