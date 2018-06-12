package com.changsukuaidi.www.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.i.IBasePresenter;
import com.changsukuaidi.www.base.i.IBaseView;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.databinding.ToolbarContentBinding;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.widget.popup.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @Describe
 * @Author thc
 * @update zhouhao
 * @Date 2017/11/30
 * @Contact 605626708@qq.com
 */

public abstract class BaseFragment<P extends IBasePresenter, VBD extends ViewDataBinding> extends RxFragment implements IBaseView {
    @Inject
    protected P mPresenter;

    protected View rootView;

    protected ToolbarContentBinding mTitleViewBinding;
    protected VBD mViewBinding;

    protected RxPermissions mRxPermissions;
    protected LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initIntent();

        mViewBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false);

        // 添加title
        if (useTitle()) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            // 有标题的时候都给statusBar的背景设置为themeColor
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.themeColor));
            }

            // title layout
            mTitleViewBinding = DataBindingUtil.inflate(inflater, R.layout.toolbar_content, container, false);
            setToolbarLeftEnable();

            // add title
            linearLayout.addView(mTitleViewBinding.getRoot());
            // add content
            linearLayout.addView(mViewBinding.getRoot(), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            rootView = linearLayout;
        } else {
            if (showFullScreen()) {
                // 设置全屏显示,占用statusBar背景位置
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 设置状态栏背景为themeColor
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.themeColor));
            }
            rootView = mViewBinding.getRoot();
        }

        setPresenter();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        if (usePermissions()) {
            //noinspection ConstantConditions
            mRxPermissions = new RxPermissions(getActivity());
        }
        initView();
        initData();
        return rootView;
    }

    protected void initIntent() {

    }

    protected abstract void setPresenter();

    protected abstract int layoutId();

    protected void initView() {

    }

    protected void initData() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null)
            mPresenter.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (useEventBus())
            EventBus.getDefault().unregister(this);
    }

    protected boolean onBackPressed() {
        return false;
    }

    protected boolean useEventBus() {
        return false;
    }

    protected boolean usePermissions() {
        return false;
    }

    @Override
    public void showSuccessMessage(String message) {
        ToastUtils.showToast(getContext(), message);
    }

    @Override
    public void showFailedMessage(String message) {
        ToastUtils.showToast(getContext(), message);
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.newInstance(msg);
        }
        mLoadingDialog.show(getFragmentManager(), "loading");
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.newInstance("请稍后...");
        }
        mLoadingDialog.show(getFragmentManager(), "loading");
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void onNetError() {

    }

    @Override
    public void showEmpty() {

    }

    /**
     * 确定是否需要全屏展示
     */
    public boolean showFullScreen() {
        return false;
    }

    public boolean useTitle() {
        return true;
    }

    protected void setToolbarTitle(String title) {
        mTitleViewBinding.toolbarTitle.setText(title);
    }

    protected void setToolbarTitleWithRightDrawable(String title, int resId, View.OnClickListener listener) {
        mTitleViewBinding.toolbarTitle.setText(title);
        mTitleViewBinding.toolbarTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        mTitleViewBinding.toolbarTitle.setCompoundDrawablePadding(DisplayUtils.dp2px(getContext(), 10));
        mTitleViewBinding.toolbarTitle.setOnClickListener(listener);
    }

    protected void setToolbarLeftEnable() {
        mTitleViewBinding.toolbarLeft.setVisibility(View.VISIBLE);
        mTitleViewBinding.toolbarLeft.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_back));
        mTitleViewBinding.toolbarLeft.setOnClickListener(view -> getActivity().onBackPressed());
    }

    protected void setToolbarLeftDisEnable() {
        mTitleViewBinding.toolbarLeft.setVisibility(View.INVISIBLE);
    }

    protected void setToolbarRightInvisible() {
        mTitleViewBinding.toolbarRight.setVisibility(View.INVISIBLE);
    }

    protected void setToolbarRight(String text, View.OnClickListener listener) {
        mTitleViewBinding.toolbarRight.setVisibility(View.VISIBLE);
        mTitleViewBinding.toolbarRight.setText(text);
        mTitleViewBinding.toolbarRight.setOnClickListener(listener);
    }

    protected void setToolbarRight(String text, int color, View.OnClickListener listener) {
        mTitleViewBinding.toolbarRight.setTextColor(color);
        setToolbarRight(text, listener);
    }

    protected void setToolbarRightText(String text, int color, View.OnClickListener listener) {
        mTitleViewBinding.toolbarRight.setText(text);
        mTitleViewBinding.toolbarRight.setTextColor(color);
        mTitleViewBinding.toolbarRight.setVisibility(View.VISIBLE);
        mTitleViewBinding.toolbarRight.setOnClickListener(listener);
    }

    protected void setToolbarRightDrawable(int resId, View.OnClickListener listener) {
        mTitleViewBinding.toolbarRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
        mTitleViewBinding.toolbarRight.setVisibility(View.VISIBLE);
        mTitleViewBinding.toolbarRight.setOnClickListener(listener);
    }

    protected void setToolbarRightVisible(boolean isVisible) {
        mTitleViewBinding.toolbarRight.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}
