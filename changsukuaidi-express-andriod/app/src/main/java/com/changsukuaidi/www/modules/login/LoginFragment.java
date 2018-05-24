package com.changsukuaidi.www.modules.login;

import android.content.Intent;
import android.text.TextUtils;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.databinding.FragmentLoginBinding;
import com.changsukuaidi.www.modules.home.HomeActivity;
import com.changsukuaidi.www.modules.login.forget_password.ForgetPwdActivity;
import com.changsukuaidi.www.modules.punch.PunchActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class LoginFragment extends BaseFragment<LoginPresenter, FragmentLoginBinding> {

    @Override
    protected void setPresenter() {
        DaggerLoginComp.builder()
                .appComp(BaseApplication.getAppComp())
                .loginModule(new LoginModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        // 设置顶部Toolbar
        setToolbarTitle(getString(R.string.login));

        // 监听输入框输入
        Observable.combineLatest(RxTextView.textChanges(mViewBinding.phoneText), RxTextView.textChanges(mViewBinding.passwordText),
                (charSequence, charSequence2) -> !TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty(charSequence2))
                .subscribe(aBoolean -> mViewBinding.loginBtn.setEnabled(aBoolean));

        RxView.clicks(mViewBinding.loginBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        // 密码登录
                        mPresenter.login(mViewBinding.phoneText.getText().toString(), mViewBinding.passwordText.getText().toString()));

        RxView.clicks(mViewBinding.forgetPwdText)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        // 忘记密码
                        startActivity(new Intent(getActivity(), ForgetPwdActivity.class)));

    }

    /**
     * 登录成功去获取打卡和车辆信息
     */
    public void loginSuccess() {
        mPresenter.getCarInfo();
    }

    /**
     * 判断是否是打卡，进入打卡页面获取主页
     *
     * @param carBean
     */
    public void finishGetCarInfo(CarBean carBean) {
        if (carBean.getPunchInfo() == null || TextUtils.isEmpty(carBean.getPunchInfo().getStime())) {
            startActivity(new Intent(getActivity(), PunchActivity.class));
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getActivity().finish();
    }
}
