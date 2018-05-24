package com.changsukuaidi.www.modules.login.forget_password;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.databinding.FragmentForgetPasswordOneBinding;
import com.changsukuaidi.www.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class ForgetPwdFragmentOne extends BaseFragment<ForgetPwdPresenter, FragmentForgetPasswordOneBinding> implements ForgetPwdContact.ForgetPwdView {

    @Override
    protected void setPresenter() {
        DaggerForgetPwdOneComp.builder()
                .appComp(BaseApplication.getAppComp())
                .forgetPwdModule(new ForgetPwdModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_forget_password_one;
    }

    @Override
    protected void initView() {
        // 设置顶部Toolbar
        setToolbarTitle(getString(R.string.find_back_password));
        setToolbarLeftEnable();

        // 监听输入框的输入
        Observable.combineLatest(RxTextView.textChanges(mViewBinding.phoneTextForgetPassword), RxTextView.textChanges(mViewBinding.verifyCodeForgetPwd),
                (charSequence, charSequence2) ->
                        !TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty(charSequence2))
                .subscribe(aBoolean -> mViewBinding.nextBtnForgetPwd.setEnabled(aBoolean));

        // 下一步
        RxView.clicks(mViewBinding.nextBtnForgetPwd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> mPresenter.checkVerifyCode("forgetpwd", mViewBinding.phoneTextForgetPassword.getText().toString(), mViewBinding.verifyCodeForgetPwd.getText().toString()));

        // 获取验证码
        RxView.clicks(mViewBinding.getVerifyCodeForgetPwd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    String phone = mViewBinding.phoneTextForgetPassword.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        ToastUtils.showToast("请输入手机号码");
                    } else if (phone.length() != 11) {
                        ToastUtils.showToast("请输入正确的手机号码");
                    } else {
                        mViewBinding.getVerifyCodeForgetPwd.setEnabled(false);
                        mPresenter.getVerifyCode("forgetpwd", mViewBinding.phoneTextForgetPassword.getText().toString(), mViewBinding.getVerifyCodeForgetPwd);
                    }
                });
    }

    @Override
    public void refreshGetVerify(int number) {
        if (number == 0) {
            mViewBinding.getVerifyCodeForgetPwd.setEnabled(true);
            mViewBinding.getVerifyCodeForgetPwd.setText(R.string.get_verify_code);
        } else {
            mViewBinding.getVerifyCodeForgetPwd.setTextColor(Color.parseColor("#CCCCCC"));
            mViewBinding.getVerifyCodeForgetPwd.setText(String.format(getString(R.string.get_verify_again), number));
        }
    }

    @Override
    public void verifyCodePass() {
        Intent intent = new Intent(getActivity(), ForgetPwdActivityTwo.class);
        intent.putExtra("phone", mViewBinding.phoneTextForgetPassword.getText().toString());
        intent.putExtra("verifyCode", mViewBinding.verifyCodeForgetPwd.getText().toString());
        startActivity(intent);
    }

    @Override
    public void modifyPwdSuccess() {
        // ForgetPwdFragmentOne用不到ForgetPwdFragmentTwo需要的方法
    }
}
