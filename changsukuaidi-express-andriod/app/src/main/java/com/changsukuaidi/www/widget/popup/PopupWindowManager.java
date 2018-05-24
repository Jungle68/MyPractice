package com.changsukuaidi.www.widget.popup;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/25
 */

public class PopupWindowManager{
    private int layoutResId;//布局id
    private Context context;
    private PopupWindow popupWindow;
    private ViewDataBinding mViewBinding;//弹窗布局View
    private ViewDataBinding mView;

    PopupWindowManager(Context context, PopupWindow popupWindow) {
        this.context = context;
        this.popupWindow = popupWindow;
    }

    public void setView(int layoutResId) {
        mView = null;
        this.layoutResId = layoutResId;
        installContent();
    }

    public void setView(ViewDataBinding view) {
        mView = view;
        this.layoutResId = 0;
        installContent();
    }

    ViewDataBinding getViewBinding() {
        return mViewBinding;
    }

    private void installContent() {
        if (layoutResId != 0) {
            mViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context),layoutResId, null, false);
        } else if (mView != null) {
            mViewBinding = mView;
        }
        popupWindow.setContentView(mViewBinding.getRoot());
    }

    /**
     * 设置宽度
     *
     * @param width  宽
     * @param height 高
     */
    private void setWidthAndHeight(int width, int height) {
        if (width == 0 || height == 0) {
            //如果没设置宽高，默认是WRAP_CONTENT
            popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
        }
    }

    /**
     * 设置动画
     */
    private void setAnimationStyle(int animationStyle) {
        popupWindow.setAnimationStyle(animationStyle);
    }

    /**
     * 设置Outside是否可点击
     *
     * @param touchable 是否可点击
     */
    private void setOutsideTouchable(boolean touchable) {
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));//设置透明背景
        popupWindow.setOutsideTouchable(touchable);//设置outside可点击
        popupWindow.setFocusable(touchable);
    }


    static class PopupParams {
        int layoutResId;//布局id
        Context mContext;
        int mWidth, mHeight;//弹窗的宽和高
        boolean isShowAnim;
        int animationStyle;//动画Id
        ViewDataBinding mViewBinding;
        boolean isTouchable = true;

        PopupParams(Context mContext) {
            this.mContext = mContext;
        }

        void apply(PopupWindowManager manager) {
            if (mViewBinding != null) {
                manager.setView(mViewBinding);
            } else if (layoutResId != 0) {
                manager.setView(layoutResId);
            } else {
                throw new IllegalArgumentException("PopupView's contentView is null");
            }
            manager.setWidthAndHeight(mWidth, mHeight);
            manager.setOutsideTouchable(isTouchable);//设置outside可点击

            if (isShowAnim) {
                manager.setAnimationStyle(animationStyle);
            }
        }
    }
}
