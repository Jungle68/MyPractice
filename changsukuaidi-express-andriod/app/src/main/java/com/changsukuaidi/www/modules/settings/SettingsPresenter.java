package com.changsukuaidi.www.modules.settings;

import android.graphics.Bitmap;

import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseObserver;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.utils.FileUtils;
import com.changsukuaidi.www.utils.ToastUtils;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

class SettingsPresenter extends BasePresenter<SettingsRepository, SettingsView> {

    @Inject
    SettingsPresenter(SettingsView view, SettingsRepository repository, BaseApplication application) {
        super(view, repository, application);
    }

    /**
     * 上传头像图片
     *
     * @param bitmap
     */
    void uploadHeadIcon(Bitmap bitmap) {
        mRootView.showLoading("上传头像中...");
        mRepository.uploadHeadIcon("data:image/png;base64," + FileUtils.bitmap2StrByBase64(bitmap))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<JsonObject>() {
                    @Override
                    public void onSuccess(String message, JsonObject jsonObject) {
                        setUserIcon(jsonObject.get("img_id").getAsString());
                    }

                    @Override
                    public void onFailed(int status, String message) {
                        ToastUtils.showToast("上传头像失败");
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addSubscribe(d);
                    }
                });
    }

    /**
     * 根据上传图片的id设置头像
     *
     * @param img_id
     */
    private void setUserIcon(String img_id) {
        mRepository.setUserHeadIcon(img_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<JsonObject>() {
                    @Override
                    public void onSuccess(String message, JsonObject jsonObject) {
                        ToastUtils.showToast("设置头像成功");
                        mRootView.hideLoading();
                        mRootView.uploadCoverFinish(jsonObject.get("url").getAsString() + jsonObject.get("avatar").getAsString());
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


