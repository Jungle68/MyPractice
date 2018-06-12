package com.changsukuaidi.www.base;

import com.changsukuaidi.www.base.i.IBasePresenter;
import com.changsukuaidi.www.base.i.IBaseView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/12/1
 * @Contact
 */
public class BasePresenter<R, V extends IBaseView> implements IBasePresenter {
    protected BaseApplication mApplication;
    protected R mRepository;
    protected V mRootView;
    private CompositeDisposable mCompositeSubscription;

    @Inject
    public BasePresenter(V view, R repository, BaseApplication application) {
        this.mRootView = view;
        this.mRepository = repository;
        this.mApplication = application;
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().register(this);// 注册到事件主线
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);// 将所有 subscription 放入,集中处理
    }

    private void unSubscribe() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.dispose();// 保证 activity 结束时取消所有正在执行的订阅
        }
    }


    @Override
    public boolean isAttachView() {
        return mRootView != null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {
        unSubscribe();
        detachView();
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
            EventBus.getDefault().unregister(this);
    }

    private void detachView() {
        mRootView = null;
        mRepository = null;
    }

    private boolean useEventBus() {
        return false;
    }

}
