package com.changsukuaidi.www.modules.settings.modify_pwd;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.utils.ToastUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

class ModifyPresenter extends BasePresenter<ModifyPwdRepository, ModifyPwdView> {

    @Inject
    ModifyPresenter(ModifyPwdView view, ModifyPwdRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    void modifyPwd(String old, String newPwd) {
        mRootView.showLoading();
        mRepository.checkOldPwd(old)
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSuccess(String message, String s) {
                        modifyPwd(newPwd);
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

    private void modifyPwd(String newPwd) {
        mRepository.modifyPwd(newPwd)
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSuccess(String message, String s) {
                        ToastUtils.showToast(message);
                        mRootView.modifySuccess();
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
