package com.changsukuaidi.www.modules.task.list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.bean.TaskBean;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.PopupWindowForCallPhoneBinding;
import com.changsukuaidi.www.databinding.PopupWindowForSureBinding;
import com.changsukuaidi.www.databinding.PopupWindowForSureGrayCancelBinding;
import com.changsukuaidi.www.modules.pickup.manual.PickupManualActivity;
import com.changsukuaidi.www.modules.pickup.sign.ConfirmReceiptActivity;
import com.changsukuaidi.www.modules.task.detail.TaskDetailActivity;
import com.changsukuaidi.www.modules.task.end_point.EndPointActivity;
import com.changsukuaidi.www.modules.task.exception_back.ExceptionBackActivity;
import com.changsukuaidi.www.modules.task.tripartite_dispatch.TripartiteActivity;
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

public class TaskAdapter extends CommonAdapter<TaskBean> {
    private OnClickTaskOperateListener mClickTaskOperateListener;
    // 是否是历史订单
    private boolean isHistory;

    private CommonPopupWindow mSureWindow;

    TaskAdapter(Context context, List<TaskBean> list, boolean isHistory, OnClickTaskOperateListener listener) {
        super(context, R.layout.item_task, list);
        this.isHistory = isHistory;
        this.mClickTaskOperateListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, TaskBean task, int position) {
        if (task == null) return;
        // 地址, 配送相关
        int type = task.getTaskType();
        if (type == 1) {
            holder.setText(R.id.dispatch_type, "药品配送");
        } else if (type == 2) {
            holder.setText(R.id.dispatch_type, "药品中转");
        } else if (type == 3) {
            holder.setText(R.id.dispatch_type, "物料配送");
        }

        if (task.getPriority() == 1) {
            holder.setVisible(R.id.prior, true);
            holder.setText(R.id.prior, "优先");
        } else {
            holder.setVisible(R.id.prior, false);
        }

        if (task.getIsTransfer() == 1) {
            holder.setVisible(R.id.transfer, true);
            holder.setText(R.id.transfer, "【转】");
        } else {
            holder.setVisible(R.id.transfer, false);
        }
        holder.setText(R.id.start_pos, task.getStartAddress());
        holder.setText(R.id.start_distance, task.getStartDistance() + "公里");
        holder.setText(R.id.end_pos, task.getEndAddress());
        holder.setText(R.id.end_distance, task.getEndDistance() + "公里");
        holder.setText(R.id.consignee, task.getTaskType() == 3 ? "" : task.getReceiverName());
        holder.setText(R.id.consignee_phone, task.getTaskType() == 3 ? "电话： " + task.getReceiverPhone() : task.getReceiverPhone());
        holder.setText(R.id.drug_time, task.getMedicineTime());

        int status = task.getStatus();
        if (!isHistory) {
            // 显示或者隐藏操作按钮
            hideOrShowOperate(holder, position, type, status);
        } else {
            holder.getView(R.id.start_distance).setVisibility(View.INVISIBLE);
            holder.getView(R.id.end_distance).setVisibility(View.INVISIBLE);
        }

        // 设置事件监听
        setListener(holder, task);
    }

    private void setListener(ViewHolder holder, TaskBean task) {
        // 查看任务详情
        holder.setOnClickListener(R.id.detail, view ->
                holder.getContext().startActivity(new Intent(holder.getContext(), TaskDetailActivity.class)));

        // 拨打收件人电话弹窗
        holder.setOnClickListener(R.id.consignee_phone, view -> showSureWindow(holder, task, R.id.consignee_phone));

        if (isHistory) return;
        // 扫描取件
        holder.setOnClickListener(R.id.scan_piece, v -> mClickTaskOperateListener.onClickTaskOperate(1, task));

        // 手动取件
        holder.setOnClickListener(R.id.manual_piece, v -> mContext.startActivity(new Intent(mContext, PickupManualActivity.class)));

        // 确认送达
        holder.setOnClickListener(R.id.confirm_delivery, v -> showSureWindow(holder, task, R.id.confirm_delivery));

        // 确认签收
        holder.setOnClickListener(R.id.confirm_receipt, v -> mContext.startActivity(new Intent(mContext, ConfirmReceiptActivity.class)));

        // 二次配送
        holder.setOnClickListener(R.id.second_dispatch, v -> showSureWindow(holder, task, R.id.second_dispatch));

        // 第三方配送
        holder.setOnClickListener(R.id.tripartite_dispatch, v ->
                holder.getContext().startActivity(new Intent(holder.getContext(), TripartiteActivity.class)));

        // 退回调拨中心
        holder.setOnClickListener(R.id.return_transfer_center, v -> showSureWindow(holder, task, R.id.return_transfer_center));

        // 签收更正
        holder.setOnClickListener(R.id.receipt_update, v -> ToastUtils.showToast("签收更正"));

        // 查看终点
        holder.setOnClickListener(R.id.view_end, v -> mContext.startActivity(new Intent(mContext, EndPointActivity.class).putExtra("task_bean", task)));
    }

    // 显示或者隐藏操作按钮
    private void hideOrShowOperate(ViewHolder holder, int position, int type, int status) {
        // 物料配送 --- 代取件
        if (status == 1 && type == 3) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_dqj);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, true);
            holder.setVisible(R.id.confirm_delivery, false);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, true);
            holder.setVisible(R.id.view_end, true);
        }
        // 物料配送 --- 配送中
        else if (status == 2 && type == 3) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_psz);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, false);
            holder.setVisible(R.id.confirm_delivery, true);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, false);
            holder.setVisible(R.id.view_end, true);
        }

        // 药品配送,药品中转 --- 代取件
        else if (status == 1 && (type == 1 || type == 2)) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_dqj);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, true);
            holder.setVisible(R.id.confirm_delivery, false);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, true);
            holder.setVisible(R.id.view_end, true);
        }
        // 药品配送 --- 配送中
        else if (status == 2 && type == 1) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_psz);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, true);
            holder.setVisible(R.id.scan_piece, false);
            holder.setVisible(R.id.confirm_delivery, false);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, false);
            holder.setVisible(R.id.view_end, true);
        }
        // 药品中转 --- 配送中
        else if (status == 2 && type == 2) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_psz);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, true);
            holder.setVisible(R.id.confirm_delivery, true);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, false);
            holder.setVisible(R.id.view_end, true);
        }
        // 异常件
        else if (status == 3) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_unusual);
            holder.setVisible(R.id.divider2, true);
            holder.setVisible(R.id.drug_time_label, true);
            holder.setVisible(R.id.drug_time, true);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, false);
            holder.setVisible(R.id.confirm_delivery, false);
            holder.setVisible(R.id.return_transfer_center, true);
            holder.setVisible(R.id.tripartite_dispatch, true);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, true);
            holder.setVisible(R.id.manual_piece, false);
            holder.setVisible(R.id.view_end, false);
        }
        // 已完成
        else if (status == 4) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_done);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, false);
            holder.setVisible(R.id.confirm_delivery, false);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, true);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, false);
            holder.setVisible(R.id.view_end, false);
        } else if (status == 5) {
            holder.setImageResource(R.id.task_identify, R.mipmap.icon_main_cancel);
            holder.setVisible(R.id.divider2, false);
            holder.setVisible(R.id.drug_time_label, false);
            holder.setVisible(R.id.drug_time, false);
            holder.setVisible(R.id.confirm_receipt, false);
            holder.setVisible(R.id.scan_piece, false);
            holder.setVisible(R.id.confirm_delivery, false);
            holder.setVisible(R.id.return_transfer_center, false);
            holder.setVisible(R.id.tripartite_dispatch, false);
            holder.setVisible(R.id.receipt_update, false);
            holder.setVisible(R.id.second_dispatch, false);
            holder.setVisible(R.id.manual_piece, false);
            holder.setVisible(R.id.view_end, false);
        }
    }

    private void showSureWindow(ViewHolder holder, TaskBean bean, int resId) {
        if (resId == R.id.return_transfer_center) {
            // 是否返回调拨中心
            mSureWindow = new CommonPopupWindow.Builder(holder.getContext())
                    .setView(R.layout.popup_window_for_sure)
                    .setWidthAndHeight(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        // 设置popupWindow 标题
                        ((PopupWindowForSureBinding) view).title.setText(holder.getResources().getString(R.string.exception_for_sure));

                        // ok
                        ((PopupWindowForSureBinding) view).ok.setOnClickListener(v1 -> {
                            // 跳入异常申报环节
                            holder.getContext().startActivity(new Intent(holder.getContext(), ExceptionBackActivity.class).putExtra("task_type", 1));
                            mSureWindow.dismiss();
                        });

                        // cancel
                        ((PopupWindowForSureBinding) view).cancel.setOnClickListener(v2 -> mSureWindow.dismiss());
                    })
                    .create();
            mSureWindow.setBackGroundLevel(0.5f);
            mSureWindow.showAtLocation(holder.getConvertView(), Gravity.CENTER, 0, 0);
        } else if (resId == R.id.second_dispatch) {
            // 是否二次派送
            mSureWindow = new CommonPopupWindow.Builder(holder.getContext())
                    .setView(R.layout.popup_window_for_sure)
                    .setWidthAndHeight(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        // 设置popupWindow 标题
                        ((PopupWindowForSureBinding) view).title.setText(holder.getResources().getString(R.string.second_dispatch_for_sure));

                        // ok
                        ((PopupWindowForSureBinding) view).ok.setOnClickListener(v1 -> {
                            // 跳入异常申报环节
                            holder.getContext().startActivity(new Intent(holder.getContext(), ExceptionBackActivity.class));
                            mSureWindow.dismiss();
                        });

                        // cancel
                        ((PopupWindowForSureBinding) view).cancel.setOnClickListener(v2 -> mSureWindow.dismiss());
                    })
                    .create();
            mSureWindow.setBackGroundLevel(0.5f);
            mSureWindow.showAtLocation(holder.getConvertView(), Gravity.CENTER, 0, 0);
        } else if (resId == R.id.consignee_phone) {
            // 是否到拨打电话页面
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
        } else if (resId == R.id.confirm_delivery) {
            mSureWindow = new CommonPopupWindow.Builder(holder.getContext())
                    .setView(R.layout.popup_window_for_sure_gray_cancel)
                    .setWidthAndHeight(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowForSureGrayCancelBinding binding = (PopupWindowForSureGrayCancelBinding) view;
                        binding.title.setText("确认送达？");
                        binding.ok.setText("确认");
                        binding.cancel.setText("取消");
                        RxView.clicks(binding.ok)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    ToastUtils.showToast("确认送达");
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

    public interface OnClickTaskOperateListener {
        /**
         * 当点击了任务的操作按钮
         *
         * @param type     操作类型：
         *                 1、扫描取件
         *                 2、手动取件
         *                 3、确认签收
         *                 4、确认送达
         *                 5、二次配送
         *                 6、第三方配送
         *                 7、退回调拨中心
         *                 8、签收更正
         *                 9、查看终点
         * @param taskBean 被操作的任务
         */
        void onClickTaskOperate(int type, TaskBean taskBean);
    }
}
