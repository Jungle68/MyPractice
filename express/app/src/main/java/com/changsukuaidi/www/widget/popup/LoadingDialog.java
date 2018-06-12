package com.changsukuaidi.www.widget.popup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.changsukuaidi.www.R;


/**
 * @Describe
 * @Author thc
 * @Date 2018/3/1
 */

public class LoadingDialog extends DialogFragment {
    private TextView mMessage;

    public static LoadingDialog newInstance(String msg) {
        LoadingDialog loadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        loadingDialog.setArguments(bundle);
        return loadingDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.loading_dialog_view, container, false);
        mMessage = rootView.findViewById(R.id.text);
        if (getArguments() != null) {
            mMessage.setText(getArguments().getString("msg"));
        }
        // 设置宽度为屏宽、靠近屏幕底部。
        getDialog().setCanceledOnTouchOutside(false);
        return rootView;
    }

    public void setMessage(String message) {
        mMessage.setText(message);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0;
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }
}
