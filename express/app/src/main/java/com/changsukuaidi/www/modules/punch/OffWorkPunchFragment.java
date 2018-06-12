package com.changsukuaidi.www.modules.punch;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.utils.ConvertUtils;
import com.changsukuaidi.www.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class OffWorkPunchFragment extends PunchFragment {
    private String result;
    private long startPunchTime;

    @Override
    protected void initView() {
        super.initView();
        setToolbarLeftDisEnable();

        mViewBinding.offWorkPunch.setVisibility(View.VISIBLE);
        // 下班
        RxView.clicks(mViewBinding.llOffWorkPunch)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(o -> {
                    if (mBDLocation != null) {
                        long offWorkPunchTime = System.currentTimeMillis() / 1000;
                        ((BaseApplication) getActivity().getApplication()).getHistoryTrace(mUserBean.getNickname(),
                                startPunchTime,
                                offWorkPunchTime, new OnTrackListener() {
                                    @Override
                                    public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                                        result = "[";
                                        List<TrackPoint> points = historyTrackResponse.getTrackPoints();
                                        Flowable.fromIterable(points)
                                                .map(trackPoint -> "{\"latitude\":"
                                                        + trackPoint.getLocation().getLatitude()
                                                        + ",\"longitude\":"
                                                        + trackPoint.getLocation().getLongitude()
                                                        + "}")
                                                .subscribe(s -> result += s + ",",
                                                        throwable -> {
                                                        }, () -> {
                                                            if (result.length() > 1) {
                                                                result = result.substring(0, result.length() - 1) + "]";
                                                            } else {
                                                                result = "";
                                                            }
                                                            double distance = ConvertUtils.double2Double(historyTrackResponse.getDistance() / 1000, 2);
                                                            double oils = ConvertUtils.double2Double(distance * Double.parseDouble(mCarBean.getDevice().getOil_cost()) / 100, 2);
                                                            mPresenter.offWorkPunch(mBDLocation.getAddrStr(),
                                                                    distance,
                                                                    result,
                                                                    oils
                                                                    );
                                                        });
                                    }
                                });
                    } else {
                        ToastUtils.showToast("正在获取当前位置");
                    }
                });

        // 设置当前时间
        mViewBinding.tvOffPunchTime.setText(new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis())));
    }

    @Override
    public void punchSuccess() {
        super.punchSuccess();
        mViewBinding.tvOffWorkPunchTime.setText(String.format("%s %s", getString(R.string.off_work_punch_time), mViewBinding.tvOffPunchTime.getText()));
        mViewBinding.llOffWorkPunch.setVisibility(View.GONE);
        mViewBinding.tvOffWorkPunchTime.setTextColor(getResources().getColor(R.color.common_text));
    }

    @Override
    public void updateTime(String time) {
        mViewBinding.tvOffPunchTime.setText(time);
    }

    @Subscribe
    public void updateLoc(@NonNull BDLocation location) {
        if (!punchSuccess) {
            mBDLocation = location;
            mViewBinding.tvOffWorkPunchAddress.setText(location.getAddrStr());
            mViewBinding.offLocationProgress.setVisibility(View.GONE);
        }
    }

    /**
     * 设置打卡信息
     */
    @Override
    protected void setCarInfo() {
        super.setCarInfo();
        if (mCarBean == null) {
            return;
        }
        // 设置上班打卡的信息
        CarBean.PunchInfo punchInfo = mCarBean.getPunchInfo();
        if (punchInfo == null) {
            return;
        }
        startPunchTime = Long.parseLong(punchInfo.getStime());
        mViewBinding.tvStartWorkPunchTimeOld.setText(String.format("%s %s", getString(R.string.punch_card_time),
                new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(startPunchTime * 1000))));
        mViewBinding.tvStartWorkPunchAddressOld.setText(punchInfo.getArea1());
        // 如果已经打过下班的卡，展现下班打卡的信息
        if (!TextUtils.isEmpty(punchInfo.getEtime())) {
            punchSuccess = true;
            mViewBinding.tvOffWorkPunchTime.setText(String.format("%s %s", getString(R.string.off_work_punch_time),
                    new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date(Long.parseLong(punchInfo.getEtime()) * 1000))));
            mViewBinding.tvOffWorkPunchAddress.setText(punchInfo.getArea2());
            mViewBinding.offLocationProgress.setVisibility(View.GONE);
            mViewBinding.llOffWorkPunch.setVisibility(View.GONE);
            mViewBinding.tvOffWorkPunchTime.setTextColor(getResources().getColor(R.color.common_text));
        }
    }
}
