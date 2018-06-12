package com.changsukuaidi.www.modules.home;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

class HomePresenter extends BasePresenter<HomeRepository, IHomeView> {
    @Inject
    HomePresenter(IHomeView view, HomeRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    void loadUserInfo() {
        mRootView.showLoading();
        mRepository.getUserInfo()
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    public void onSuccess(String message, UserBean userBean) {
                        mRootView.hideLoading();
                        ACache.get(mApplication).put(Content.USER, userBean);
                        mRootView.loadUserInfo(1);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }
}
