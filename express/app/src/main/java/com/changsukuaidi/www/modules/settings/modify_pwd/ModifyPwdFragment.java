package com.changsukuaidi.www.modules.settings.modify_pwd;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseActivity;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.databinding.FragmentModifyPwdBinding;
import com.changsukuaidi.www.modules.login.LoginActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class ModifyPwdFragment extends BaseFragment<ModifyPresenter, FragmentModifyPwdBinding> implements ModifyPwdView {

    @Override
    protected void setPresenter() {
        DaggerModifyPwdComp.builder()
                .appComp(BaseApplication.getAppComp())
                .modifyPwdModule(new ModifyPwdModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_modify_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        // set title
        setToolbarTitle(getString(R.string.modify_pwd));

        RxView.clicks(mViewBinding.btnDone)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (mViewBinding.detNewPwd.getText().toString().length() > 0 && TextUtils.equals(mViewBinding.detNewPwd.getText().toString(), mViewBinding.detNewPwdOnce.getText().toString())) {
                        mViewBinding.errorPwdHit.setVisibility(View.INVISIBLE);
                        mPresenter.modifyPwd(mViewBinding.detOldPwd.getText().toString(), mViewBinding.detNewPwd.getText().toString());
                    } else {
                        mViewBinding.errorPwdHit.setVisibility(View.VISIBLE);
                    }
                });

        Observable.combineLatest(RxTextView.textChanges(mViewBinding.detOldPwd), RxTextView.textChanges(mViewBinding.detNewPwd),
                RxTextView.textChanges(mViewBinding.detNewPwdOnce), (charSequence, charSequence2, charSequence3) ->
                        charSequence.length() > 5 && charSequence2.length() > 5 && charSequence3.length() > 5)
                .subscribe(aBoolean -> mViewBinding.btnDone.setEnabled(aBoolean));
        RxTextView.textChanges(mViewBinding.detNewPwd).subscribe(charSequence -> mViewBinding.errorPwdHit.setVisibility(View.INVISIBLE));
        RxTextView.textChanges(mViewBinding.detNewPwdOnce).subscribe(charSequence -> mViewBinding.errorPwdHit.setVisibility(View.INVISIBLE));
        mViewBinding.errorPwdHit.setVisibility(View.INVISIBLE);
    }

    @Override
    public void modifySuccess() {
        ((BaseApplication) getActivity().getApplication()).clearTokenAndUser();
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
