package com.changsukuaidi.www.modules.login.forget_password;

import android.content.Intent;
import android.text.TextUtils;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.databinding.FragmentForgetPasswordTwoBinding;
import com.changsukuaidi.www.modules.login.LoginActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class ForgetPwdFragmentTwo extends BaseFragment<ForgetPwdPresenter, FragmentForgetPasswordTwoBinding> implements ForgetPwdContact.ForgetPwdView {
    private String mPhone; // 电话号码
    private String mVerifyCode; // 验证码

    @Override
    protected void setPresenter() {
        DaggerForgetPwdTwoComp.builder()
                .appComp(BaseApplication.getAppComp())
                .forgetPwdModule(new ForgetPwdModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_forget_password_two;
    }

    @Override
    protected void initIntent() {
        mPhone = getActivity().getIntent().getStringExtra("phone");
        mVerifyCode = getActivity().getIntent().getStringExtra("verifyCode");
    }

    @Override
    protected void initView() {
        // 设置顶部Toolbar
        setToolbarTitle(getString(R.string.reset_password));

        // 监听输入框的输入
        Observable.combineLatest(RxTextView.textChanges(mViewBinding.passwordFirst),
                RxTextView.textChanges(mViewBinding.passwordConfirm),
                (charSequence, charSequence2) ->
                        !TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty(charSequence2))
                .subscribe(aBoolean -> mViewBinding.confirmBtn.setEnabled(aBoolean));

        RxView.clicks(mViewBinding.confirmBtn)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> mPresenter.modifyPwd(mPhone, mViewBinding.passwordFirst.getText().toString(),
                        mViewBinding.passwordConfirm.getText().toString(), mVerifyCode));
    }

    @Override
    public void modifyPwdSuccess() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void refreshGetVerify(int number) {
        // ForgetPwdFragmentTwo用不到ForgetPwdFragmentOne需要的方法
    }

    @Override
    public void verifyCodePass() {
        // ForgetPwdFragmentTwo用不到ForgetPwdFragmentOne需要的方法
    }
}
