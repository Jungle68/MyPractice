package com.changsukuaidi.www.widget.popup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.changsukuaidi.www.R;


/**
 * @Describe
 * @Author thc
 * @Date 2018/3/1
 */

public class MyAlertDialog extends DialogFragment {
    private View.OnClickListener mOkListener;
    private View.OnClickListener mCancelListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_alter_dialog_view, container, false);
        TextView titleView = rootView.findViewById(R.id.title);
        TextView contentView = rootView.findViewById(R.id.msg);
        TextView cancelView = rootView.findViewById(R.id.cancel);
        TextView okView = rootView.findViewById(R.id.ok);
        View dividerOne = rootView.findViewById(R.id.divider1);
        View dividerVertical = rootView.findViewById(R.id.divider3);

        String title = getArguments().getString("title");
        String content = getArguments().getString("content");
        String cancel = getArguments().getString("cancel");
        String ok = getArguments().getString("ok");
        boolean cancelable = getArguments().getBoolean("cancelable");

        if (TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.GONE);
            dividerOne.setVisibility(View.GONE);
        } else {
            titleView.setText(title);
        }

        if (TextUtils.isEmpty(content)) {
            contentView.setVisibility(View.GONE);
            dividerOne.setVisibility(View.GONE);
        } else {
            contentView.setText(content);
        }

        if (TextUtils.isEmpty(cancel)) {
            cancelView.setVisibility(View.GONE);
            dividerVertical.setVisibility(View.GONE);
        } else {
            cancelView.setText(cancel);
        }

        if (TextUtils.isEmpty(ok)) {
            okView.setVisibility(View.GONE);
            dividerVertical.setVisibility(View.GONE);
        } else {
            okView.setText(ok);
        }

        cancelView.setOnClickListener(mCancelListener);
        okView.setOnClickListener(mOkListener);

        setCancelable(cancelable);
        return rootView;
    }

    public void setOkListener(View.OnClickListener listener) {
        mOkListener = listener;
    }

    public void setCancelListener(View.OnClickListener listener) {
        mCancelListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static class Builder {
        private String title;
        private String content;
        private String cancel;
        private String ok;
        private boolean cancelable;
        private View.OnClickListener okListener;
        private View.OnClickListener cancelListener;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setCancel(String cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setOk(String ok) {
            this.ok = ok;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOkClick(View.OnClickListener listener) {
            this.okListener = listener;
            return this;
        }

        public Builder setCancelClick(View.OnClickListener listener) {
            this.cancelListener = listener;
            return this;
        }

        public MyAlertDialog build() {
            MyAlertDialog loadingDialog = new MyAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("content", content);
            bundle.putString("cancel", cancel);
            bundle.putString("ok", ok);
            bundle.putBoolean("cancelable", cancelable);
            loadingDialog.setOkListener(okListener);
            loadingDialog.setCancelListener(cancelListener);
            loadingDialog.setArguments(bundle);
            return loadingDialog;
        }
    }
}
