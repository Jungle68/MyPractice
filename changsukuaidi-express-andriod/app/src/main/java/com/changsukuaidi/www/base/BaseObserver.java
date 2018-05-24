package com.changsukuaidi.www.base;

import com.changsukuaidi.www.bean.BaseJson;

import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/24
 * @Contact 605626708@qq.com
 */

public abstract class BaseObserver<D> implements Observer<BaseJson<D>> {

    public static final int DEFAULT_ERROR = -1000;
    private static final int DEFAULT_SUCCESS = 1;
    public static final int DEFAULT_INVADED = -5;

    @Override
    public void onError(Throwable e) {
        String msg = e.getMessage();
        if (e instanceof UnknownHostException) {
            msg = "网络连接失败, 请稍后再试";
        }
        onFailed(DEFAULT_ERROR, msg);
    }

    @Override
    public void onNext(BaseJson<D> dBaseJson) {
        if (dBaseJson.getStatus() == DEFAULT_SUCCESS) {
            onSuccess(dBaseJson.getMessage(), dBaseJson.getData());
        } else {
            onFailed(dBaseJson.getStatus(), dBaseJson.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    public abstract void onSuccess(String message, D d);

    public abstract void onFailed(int status, String message);
}
