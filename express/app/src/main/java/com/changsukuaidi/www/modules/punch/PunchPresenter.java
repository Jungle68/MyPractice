package com.changsukuaidi.www.modules.punch;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

class PunchPresenter extends BasePresenter<PunchRepository, IPunchView> {
    private Disposable mDisposable;

    @Inject
    PunchPresenter(IPunchView view, PunchRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    /**
     * 下班打卡
     */
    void offWorkPunch(String area, double distance, String points, double oils) {
        mRepository.offWorkPunch(area, distance + "", points, oils)
                .subscribe(new BaseObserver<Object>() { // 下班打卡
                    @Override
                    public void onSuccess(String message, Object o) {
                        mRootView.punchSuccess();
                        ToastUtils.showToast(message);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast(message);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }

    /**
     * 下班打卡
     */
    void startPunch(String area) {
        mRepository.startPunch(area)
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    public void onSuccess(String message, Object o) {
                        mRootView.punchSuccess();
                        ToastUtils.showToast(message);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast(message);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }

    /**
     * 更新时间
     */
    void updateTime() {
        mDisposable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(aLong -> new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis())))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(string -> mRootView.updateTime(string));
        addSubscribe(mDisposable);
    }

    /**
     * 停止更新时间
     */
    void stopUpdateTime() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.isDisposed();
        }
    }

    void loadUserInfo() {
        mRepository.getUserInfo()
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    public void onSuccess(String message, UserBean userBean) {
                        ACache.get(mApplication).put(Content.USER, userBean);
                        mRootView.loadUserInfo(1);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
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
                        mRootView.finishLoadCarInfo(1);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        mRootView.finishLoadCarInfo(0);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }
}
