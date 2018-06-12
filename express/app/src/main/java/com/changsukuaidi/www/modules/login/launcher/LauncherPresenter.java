package com.changsukuaidi.www.modules.login.launcher;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

class LauncherPresenter extends BasePresenter<LauncherRepository, LauncherFragment> {

    @Inject
    LauncherPresenter(LauncherFragment fragment, LauncherRepository repository, BaseApplication application) {
        super(fragment, repository, application);
    }

    /**
     * 获取车辆信息和打卡信息
     */
    void getCarInfo() {
        mRepository.getCarInfo()
                .subscribe(new BaseObserver<CarBean>() {
                    @Override
                    public void onSuccess(String message, CarBean o) {
                        ACache.get(mApplication).put(Content.CAR_INFO, o);
                        mRootView.finishGetCarInfo(o);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        if (status == BaseObserver.DEFAULT_ERROR) {
                            mRootView.showNetError();
                        } else {
                            mRootView.finishGetCarInfo(null);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }
}
