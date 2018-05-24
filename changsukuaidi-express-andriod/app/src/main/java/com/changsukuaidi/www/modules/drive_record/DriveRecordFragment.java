package com.changsukuaidi.www.modules.drive_record;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseListFragment;
import com.changsukuaidi.www.bean.DriveRecordBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.MultiItemTypeAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.PopupWindowDatePickBinding;
import com.changsukuaidi.www.modules.drive_record.drive_detail.DriveDetailActivity;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

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

public class DriveRecordFragment extends BaseListFragment<DriveRecordPresenter, DriveRecordBean> implements DriveRecordViewList, CommonPopupWindow.ViewInterface<PopupWindowDatePickBinding> {
    private CommonPopupWindow mDatePickPop;
    private int yearIndex;
    private int monthIndex;
    private String time;

    @Override
    protected void setPresenter() {
        DaggerDriveRecordComp.builder()
                .appComp(BaseApplication.getAppComp())
                .driveRecordModule(new DriveRecordModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initView() {
        super.initView();
        // set title
        setToolbarTitle(getString(R.string.drive_record));
        setToolbarRightDrawable(R.mipmap.ico_date_center, view -> showDatePickPop());

        ((CommonAdapter) mAdapter).setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<DriveRecordBean>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, DriveRecordBean driveRecordBean, int position) {
                startActivity(new Intent(getActivity(), DriveDetailActivity.class).putExtra("drive_record", driveRecordBean));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, DriveRecordBean driveRecordBean, int position) {
                return false;
            }
        });
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<DriveRecordBean>(getContext(), R.layout.delegate_drive_record, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, DriveRecordBean driveRecordBean, int position) {
                holder.setText(R.id.tv_record_date, getStimeFormat(driveRecordBean.getEtime()));
                holder.setText(R.id.tv_mileage_value, driveRecordBean.getDistance() + "km");
                holder.setText(R.id.tv_oil_count, driveRecordBean.getOils() + "升");
            }
        };
    }

    // 格式化显示的时间的格式
    private String getStimeFormat(String seconds) {
        if (seconds.isEmpty()) return "";
        long day = Long.parseLong(seconds) / 24 / 60 / 60;
        long currentDay = System.currentTimeMillis() / 1000 / 24 / 60 / 60;
        if ((day - currentDay) == 0) {
            return "今天";
        } else if ((day - currentDay) == -1) {
            return "昨天";
        } else if ((day - currentDay) == -2) {
            return "前天";
        } else {
            return new SimpleDateFormat("YYYY-MM-dd", Locale.CHINA).format(new Date(Long.parseLong(seconds) * 1000));
        }
    }

    // 展示相册选择弹窗
    private void showDatePickPop() {
        if (mDatePickPop == null) {
            mDatePickPop = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_date_pick)
                    .setViewOnclickListener(this)
                    .create();
        }
        mDatePickPop.setBackGroundLevel(0.8f);
        mDatePickPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public String getTime() {
        return TextUtils.isEmpty(time) ? "" : time;
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    @Override
    public void getChildViewBinding(PopupWindowDatePickBinding datePickBinding, int layoutResId) {
        String[] years = new String[]{"全部", "2016年", "2017年", "2018年"};
        String[] months = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        datePickBinding.wvYears.setItems(years);
        datePickBinding.wvMonths.setItems(new String[]{"全部"});
        datePickBinding.wvYears.setOnItemSelectListener(index -> {
            yearIndex = index;
            if (index == 0) {
                datePickBinding.wvMonths.setItems(new String[]{"全部"});
                datePickBinding.wvMonths.setSelectedIndex(0);
            } else {
                datePickBinding.wvMonths.setItems(months);
                datePickBinding.wvMonths.setSelectedIndex(0);
            }
        });
        datePickBinding.wvMonths.setOnItemSelectListener(index -> monthIndex = index);
        RxView.clicks(datePickBinding.tvCancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> mDatePickPop.dismiss());
        RxView.clicks(datePickBinding.tvDone)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                            mDatePickPop.dismiss();
                            if (yearIndex == 0) {
                                mPresenter.getRecordList(1, "", false);
                                time = "";
                            } else {
                                String year = years[datePickBinding.wvYears.getSelectedIndex()];
                                String month = months[datePickBinding.wvMonths.getSelectedIndex()];
                                year = year.substring(0, year.length() - 1);
                                month = month.substring(0, month.length() - 1);
                                time = year + "-" + month;
                                mPresenter.getRecordList(1, time, false);
                            }
                        }
                );
    }
}
