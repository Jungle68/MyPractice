package com.changsukuaidi.www.modules.punch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.baidu.location.BDLocation;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.modules.home.HomeActivity;
import com.changsukuaidi.www.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class StartPunchFragment extends PunchFragment {

    @Override
    protected void initView() {
        super.initView();
        mViewBinding.startWorkPunch.setVisibility(View.VISIBLE);
        // 查看任务
        RxView.clicks(mViewBinding.performTask)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(o -> {
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
                });
        // 上班打卡
        RxView.clicks(mViewBinding.llStartWorkPunch)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(o -> {
                    if (mBDLocation != null) {
                        mPresenter.startPunch(mBDLocation.getAddrStr());
                    } else {
                        ToastUtils.showToast("正在获取当前位置");
                    }
                });

        // 设置当前时间
        mViewBinding.tvPunchTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void punchSuccess() {
        super.punchSuccess();
        mViewBinding.tvStartWorkPunchTime.setText(String.format("%s %s", getString(R.string.punch_card_time), mViewBinding.tvPunchTime.getText()));
        mViewBinding.llStartWorkPunch.setVisibility(View.GONE);
        mViewBinding.performTask.setVisibility(View.VISIBLE);
        mViewBinding.tvStartWorkPunchTime.setTextColor(getResources().getColor(R.color.common_text));
        ((BaseApplication) getActivity().getApplication()).initTraceServiceAndStartTrace(155338, mUserBean.getNickname());
        ((BaseApplication) getActivity().getApplication()).updateEntityInfo(mUserBean.getNickname(), "110", "hehheheh", "快递员", "hhhhhh", mBDLocation.getAddrStr());
    }

    @Override
    public void updateTime(String time) {
        mViewBinding.tvPunchTime.setText(time);
    }

    @Subscribe
    public void updateLoc(@NonNull BDLocation location) {
        if (!punchSuccess) {
            mBDLocation = location;
            mViewBinding.tvStartWorkPunchAddress.setText(location.getAddrStr());
            mViewBinding.startLocationProgress.setVisibility(View.GONE);
        }
    }
}
