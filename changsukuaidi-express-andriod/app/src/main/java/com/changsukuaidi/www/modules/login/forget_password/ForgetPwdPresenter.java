package com.changsukuaidi.www.modules.login.forget_password;

import android.view.View;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.modules.login.LoginRepository;
import com.changsukuaidi.www.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class ForgetPwdPresenter extends BasePresenter<LoginRepository, ForgetPwdContact.ForgetPwdView>
        implements ForgetPwdContact.ForgetPwdPresenter {

    @Inject
    ForgetPwdPresenter(ForgetPwdContact.ForgetPwdView view, LoginRepository repository, BaseApplication application) {
        super(view, repository, application);
    }


    @Override
    public void checkVerifyCode(String type, String phone, String verifyCode) {
        mRootView.showLoading();
        mRepository.checkVerifyCode(type, phone, verifyCode)
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSuccess(String message, String s) {
                        ToastUtils.showToast(message);
                        mRootView.verifyCodePass();
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast(message);
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }

    @Override
    public void getVerifyCode(String type, String phone, View timeView) {
        mRootView.showLoading();
        mRepository.getVerifyCode(type, phone)
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSuccess(String message, String s) {
                        ToastUtils.showToast(message);
                        Disposable disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .take(61) // 最开始也算一次
                                .subscribe(aLong -> mRootView.refreshGetVerify((int) (60 - aLong)));
                        addSubscribe(disposable);
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast(message);
                        mRootView.hideLoading();
                        timeView.setEnabled(true);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }

    @Override
    public void modifyPwd(String phone, String pwd, String confirmPwd, String verifyCode) {
        int checkCode = checkPwd(pwd, confirmPwd);
        if (checkCode == -1) {
            mRootView.showLoading();
            mRepository.resetPassword(phone, pwd)
                    .subscribe(new BaseObserver<String>() {
                        @Override
                        public void onSuccess(String message, String s) {
                            ToastUtils.showToast(message);
                            mRootView.modifyPwdSuccess();
                            mRootView.hideLoading();
                        }

                        @Override
                        public void onFailed(int status, String message) {
                            ToastUtils.showToast(message);
                            mRootView.hideLoading();
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addSubscribe(d);
                        }
                    });
        }
    }

    // 检验密码是否合格
    private int checkPwd(String pwd, String confirmPwd) {
        if (pwd.length() < 6) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码长度不得小于6");
            return 0;
        } else if (pwd.length() > 18) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码长度不得大于18");
            return 1;
        } else if (!pwd.matches("^[0-9a-zA-Z]*$")) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码只能是数字和字母");
            return 2;
        } else if (confirmPwd.length() == 0) {
            ToastUtils.showToast(BaseApplication.getApplication(), "请再次输入密码");
            return 4;
        } else if (!pwd.equals(confirmPwd)) {
            ToastUtils.showToast(BaseApplication.getApplication(), "密码不一致，请重新输入");
            return 4;
        }
        return -1;
    }
}
