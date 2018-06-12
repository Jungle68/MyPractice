package com.changsukuaidi.www.modules.task.grab_task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.WindowManager;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.bean.TaskBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.PopupWindowForCallPhoneBinding;
import com.changsukuaidi.www.databinding.PopupWindowForSureGrayCancelBinding;
import com.changsukuaidi.www.modules.task.detail.TaskDetailActivity;
import com.changsukuaidi.www.modules.task.end_point.EndPointActivity;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/6
 * @Contact 605626708@qq.com
 */

public class GrabTaskAdapter extends CommonAdapter<TaskBean> {

    // 是否是历史订单
    private CommonPopupWindow mSureWindow;

    GrabTaskAdapter(Context context, List<TaskBean> list) {
        super(context, R.layout.item_grab_task, list);
    }

    @Override
    protected void convert(ViewHolder holder, TaskBean task, int position) {
        if (task == null) return;
        // 地址, 配送相关
        holder.setText(R.id.dispatch_type, "药品配送");

        if (task.getPriority() == 1) {
            holder.setVisible(R.id.prior, true);
            holder.setText(R.id.prior, "优先");
        } else {
            holder.setVisible(R.id.prior, false);
        }

        holder.setText(R.id.start_pos, task.getStartAddress());
        holder.setText(R.id.start_distance, task.getStartDistance() + "公里");
        holder.setText(R.id.end_pos, task.getEndAddress());
        holder.setText(R.id.end_distance, task.getEndDistance() + "公里");
        holder.setText(R.id.consignee, task.getTaskType() == 3 ? "" : task.getReceiverName());
        holder.setText(R.id.consignee_phone, task.getTaskType() == 3 ? "电话： " + task.getReceiverPhone() : task.getReceiverPhone());
        holder.setText(R.id.drug_time, task.getMedicineTime());

        // 设置事件监听
        setListener(holder, task);
    }

    private void setListener(ViewHolder holder, TaskBean task) {
        // 查看任务详情
        holder.setOnClickListener(R.id.detail, view ->
                holder.getContext().startActivity(new Intent(holder.getContext(), TaskDetailActivity.class)));

        // 拨打收件人电话弹窗
        holder.setOnClickListener(R.id.consignee_phone, view ->
                showSureWindow(holder, task, R.id.consignee_phone)
        );

        // 抢单
        holder.setOnClickListener(R.id.grab_task, v ->
                showSureWindow(holder, task, R.id.grab_task));

        // 查看终点
        holder.setOnClickListener(R.id.view_end, v ->
                mContext.startActivity(new Intent(mContext, EndPointActivity.class).putExtra("task_bean", task)));
    }

    private void showSureWindow(ViewHolder holder, TaskBean bean, int resId) {
        // 是否到拨打电话页面
        if (resId == R.id.consignee_phone) {
            mSureWindow = new CommonPopupWindow.Builder(holder.getContext())
                    .setView(R.layout.popup_window_for_call_phone)
                    .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowForCallPhoneBinding phoneBinding = (PopupWindowForCallPhoneBinding) view;
                        phoneBinding.phone.setText(bean.getReceiverPhone());
                        phoneBinding.phone.setOnClickListener(v1 -> {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + bean.getReceiverPhone()));
                            mContext.startActivity(intent);
                        });

                        // cancel
                        phoneBinding.cancel.setOnClickListener(v2 -> mSureWindow.dismiss());
                    })
                    .create();
            mSureWindow.setBackGroundLevel(0.5f);
            mSureWindow.showAtLocation(holder.getConvertView(), Gravity.BOTTOM, 0, 0);
        } else if (resId == R.id.grab_task) { // 抢单弹窗
            mSureWindow = new CommonPopupWindow.Builder(holder.getContext())
                    .setView(R.layout.popup_window_for_sure_gray_cancel)
                    .setWidthAndHeight(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowForSureGrayCancelBinding binding = (PopupWindowForSureGrayCancelBinding) view;
                        binding.title.setText(holder.getResources().getString(R.string.ensure_grab_task));
                        binding.ok.setText(holder.getResources().getString(R.string.grabing_task));
                        binding.cancel.setText(holder.getResources().getString(R.string.cancel_grab_task));
                        RxView.clicks(binding.ok)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    ToastUtils.showToast("抢单成功");
                                    mSureWindow.dismiss();
                                });
                        RxView.clicks(binding.cancel)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> mSureWindow.dismiss());
                    })
                    .setAnimationStyle(0)
                    .create();
            mSureWindow.setBackGroundLevel(0.5f);
            mSureWindow.showAtLocation(holder.getConvertView(), Gravity.CENTER, 0, 0);
        }
    }
}
