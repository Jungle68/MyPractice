package com.changsukuaidi.www.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.utils.ViewUtils;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/25
 */

public class CommonPopupWindow extends PopupWindow {
    final PopupWindowManager mManager;
    private Context mContext;

    @Override
    public int getWidth() {
        return mManager.getViewBinding().getRoot().getMeasuredWidth();
    }

    @Override
    public int getHeight() {
        return mManager.getViewBinding().getRoot().getMeasuredHeight();
    }

    public interface ViewInterface<VBD extends ViewDataBinding> {
        void getChildViewBinding(VBD view, int layoutResId);
    }

    private CommonPopupWindow(Context context) {
        mContext = context;
        mManager = new PopupWindowManager(context, this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackGroundLevel(1.0f);
    }

    /**
     * 设置背景灰色程度
     *
     * @param level 0.0f-1.0f
     */
    public void setBackGroundLevel(float level) {
        if (mContext != null) {
            Window mWindow = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams params = mWindow.getAttributes();
            params.alpha = level;
            mWindow.setAttributes(params);
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor);
        } else {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int y = location[1];
            showAtLocation(anchor, Gravity.NO_GRAVITY, 0, y + anchor.getHeight());
        }
        update();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor, xoff, yoff);
        } else {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int y = location[1];
            showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, y + anchor.getHeight() + yoff);
        }
        update();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            int y = location[1];
            showAtLocation(anchor, gravity, xoff, y + anchor.getHeight() + yoff);
        }
        update();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        update();
    }

    public static class Builder {
        private final PopupWindowManager.PopupParams params;
        private ViewInterface listener;

        public Builder(Context context) {
            params = new PopupWindowManager.PopupParams(context);
            setDefaultStyle(context);
        }

        /**
         * @param layoutResId 设置PopupWindow 布局ID
         * @return Builder
         */
        public Builder setView(int layoutResId) {
            params.mViewBinding = null;
            params.layoutResId = layoutResId;
            return this;
        }

        /**
         * @param view 设置PopupWindow布局
         * @return Builder
         */
        public Builder setView(ViewDataBinding view) {
            params.mViewBinding = view;
            params.layoutResId = 0;
            return this;
        }

        /**
         * 设置子View
         *
         * @param listener ViewInterface
         * @return Builder
         */
        public Builder setViewOnclickListener(ViewInterface listener) {
            this.listener = listener;
            return this;
        }

        /**
         * 设置宽度和高度 如果不设置 默认是wrap_content
         *
         * @param width 宽
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            params.mWidth = width;
            params.mHeight = height;
            return this;
        }

        /**
         * 是否可点击Outside消失
         *
         * @param touchable 是否可点击
         * @return Builder
         */
        public Builder setOutsideTouchable(boolean touchable) {
            params.isTouchable = touchable;
            return this;
        }

        /**
         * 设置动画
         *
         * @return Builder
         */
        public Builder setAnimationStyle(int animationStyle) {
            params.isShowAnim = true;
            params.animationStyle = animationStyle;
            return this;
        }

        private Builder setDefaultStyle(Context context) {
            return setAnimationStyle(R.style.pop_anim)
                    .setWidthAndHeight(DisplayUtils.getWindowWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        public CommonPopupWindow create() {
            final CommonPopupWindow popupWindow = new CommonPopupWindow(params.mContext);
            params.apply(popupWindow.mManager);
            if (listener != null && popupWindow.mManager.getViewBinding() != null) {
                listener.getChildViewBinding(popupWindow.mManager.getViewBinding(), params.layoutResId);
            }
            ViewUtils.measureWidthAndHeight(popupWindow.mManager.getViewBinding().getRoot());
            return popupWindow;
        }
    }
}
