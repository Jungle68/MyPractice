package com.changsukuaidi.www.base.i;

/**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/12/1
 * @Contact
 */
public interface IBaseView {

    void showSuccessMessage(String message);

    void showFailedMessage(String message);

    void showLoading(String msg);

    void showLoading();

    void hideLoading();

    void onNetError();

    void showEmpty();
}
