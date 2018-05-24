package com.changsukuaidi.www.modules.login;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.TokenBean;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class LoginPresenter extends BasePresenter<LoginRepository, LoginFragment> {

    @Inject
    LoginPresenter(LoginFragment fragment, LoginRepository repository, BaseApplication application) {
        super(fragment, repository, application);
    }

    /**
     * 登录
     *
     * @param account
     * @param pwd
     */
    public void login(String account, String pwd) {
        mRootView.showLoading("登录中...");
        int checkCode = checkLoginByPwd(account, pwd);
        if (checkCode == -1) {
            mRepository.login(account, pwd, mApplication.getUUID())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<TokenBean>() {
                        @Override
                        public void onSuccess(String message, TokenBean s) {
                            // 暂时将token作为 永久 存储..
                            ACache.get(mApplication).put(Content.TOKEN, s);
                            mRootView.loginSuccess();
                        }

                        @Override
                        public void onFailed(int status, String message) {
                            mRootView.hideLoading();
                            ToastUtils.showToast(message);
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addSubscribe(d);
                        }
                    });

        }
    }

    /**
     * 获取车辆信息和打卡信息
     */
    void getCarInfo() {
        mRepository.getCarInfo()
                .subscribe(new BaseObserver<CarBean>() {
                    @Override
                    public void onSuccess(String message, CarBean o) {
                        mRootView.hideLoading();
                        ACache.get(mApplication).put(Content.CAR_INFO, o);
                        mRootView.finishGetCarInfo(o);
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast(message);
                        mRootView.hideLoading();
                        mApplication.clearTokenAndUser();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }

    /**
     * 检测账号密码登录是否正确
     */
    private int checkLoginByPwd(String account, String pwd) {
        if (account.equals("")) {
            ToastUtils.showToast(BaseApplication.getApplication(), "账号不能为空");
            return 0;
        } else if (pwd.equals("")) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码不能为空");
            return 1;
        } else if (pwd.length() < 6) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码长度不得小于6位");
            return 3;
        } else if (pwd.length() > 18) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码长度不得大于18位");
            return 4;
        }
        return -1;
    }
}
