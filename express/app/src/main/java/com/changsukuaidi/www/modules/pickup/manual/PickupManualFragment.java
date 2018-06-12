package com.changsukuaidi.www.modules.pickup.manual;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.common.widget.recyclerview.adapter.CommonAdapter;
import com.changsukuaidi.www.common.widget.recyclerview.base.ViewHolder;
import com.changsukuaidi.www.databinding.FragmentPickupManualBinding;
import com.changsukuaidi.www.utils.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class PickupManualFragment extends BaseFragment<PickupManualPresenter, FragmentPickupManualBinding> implements PickupManualView {
    private List<String> mExpressCodes;

    @Override
    protected void setPresenter() {
        DaggerPickupManualComp.builder()
                .appComp(BaseApplication.getAppComp())
                .pickupManualModule(new PickupManualModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbarTitle("手动取件");
        setToolbarRight("确认取件", view -> ToastUtils.showToast("确认取件"));

        mExpressCodes = new ArrayList<>();
        mExpressCodes.add("");
        mViewBinding.rvPickupManual.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mViewBinding.rvPickupManual.setNestedScrollingEnabled(false);
        mViewBinding.rvPickupManual.setAdapter(new CommonAdapter<String>(getContext(), R.layout.delegate_pickup_manual_item, mExpressCodes) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                RxTextView.textChanges(holder.getConvertView().findViewById(R.id.et_express_code))
                        .subscribe(o -> {
                            mExpressCodes.remove(position);
                            mExpressCodes.add(position, o.toString());
                        });
            }
        });

        RxView.clicks(mViewBinding.tvAddExpressCode)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    for (String code : mExpressCodes) {
                        if (TextUtils.isEmpty(code)) {
                            ToastUtils.showToast("请先完成已添加条目的填写");
                            return;
                        }
                    }
                    mExpressCodes.add("");
                    mViewBinding.rvPickupManual.getAdapter().notifyItemInserted(mExpressCodes.size() - 1);
                });
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_pickup_manual;
    }
}
