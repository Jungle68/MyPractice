package com.changsukuaidi.www.modules.punch;

import android.Manifest;
import android.content.Intent;

import com.baidu.location.BDLocation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.databinding.FragmentPunchBinding;
import com.changsukuaidi.www.service.LocateAndOrientationService;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public class PunchFragment extends BaseFragment<PunchPresenter, FragmentPunchBinding> implements IPunchView {
    protected BDLocation mBDLocation;
    protected boolean punchSuccess = false;
    protected UserBean mUserBean;
    protected CarBean mCarBean;

    @Override
    protected void setPresenter() {
        DaggerPunchComp.builder()
                .appComp(BaseApplication.getAppComp())
                .punchModule(new PunchModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initIntent() {
        super.initIntent();
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_punch;
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置标题栏标题
        setToolbarTitle(getString(R.string.punch));

        // 定位服务可能没有开启，如果没有开启则开启
        mViewBinding.punchLabel.postDelayed(() -> getActivity().startService(new Intent(getActivity(), LocateAndOrientationService.class)), 100);

        // 不断更新当前时间
        mPresenter.updateTime();

        // 初始化View
        setUserInfo();
        setCarInfo();
    }

    @Override
    protected void initData() {
        mPresenter.loadUserInfo();
        mPresenter.getCarInfo();
    }

    @Override
    public void loadUserInfo(int status) {
        setUserInfo();
    }

    @Override
    public void finishLoadCarInfo(int status) {
        setCarInfo();
    }

    /**
     * 设置用户相关信息
     */
    private void setUserInfo() {
        mUserBean = (UserBean) ACache.get(getActivity()).getAsObject(Content.USER);
        // 侧边栏上的头像
        GlideApp.with(getContext())
                .load(mUserBean == null ? "" : mUserBean.getAvatar())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .apply(new RequestOptions().transform(new CircleCrop()))
                .into(mViewBinding.head);
        mViewBinding.name.setText(mUserBean == null ? "" : mUserBean.getNickname());
        mViewBinding.jobType.setText("司机");
        mViewBinding.date.setText(new SimpleDateFormat("YYYY.MM.dd", Locale.CHINA).format(new Date(System.currentTimeMillis())));
    }

    /**
     * 设置车辆相关信息和打卡相关信息
     */
    protected void setCarInfo() {
        mCarBean = (CarBean) ACache.get(getActivity()).getAsObject(Content.CAR_INFO);
        if (mCarBean != null) {
            CarBean.Device device = mCarBean.getDevice();
            mViewBinding.carName.setText(device.getTitle());
            mViewBinding.carDesc.setText(String.format("%s   油耗：%s", device.getSn(), device.getOil_cost()));
            mViewBinding.carIcon.setImageResource(device.getType() == 1 ? R.mipmap.icon_truck : R.mipmap.icon_motorbike);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable.just(1)
                .compose(mRxPermissions.ensure(Manifest.permission.ACCESS_FINE_LOCATION))
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        ToastUtils.showToast("您拒绝了定位权限！请手动打开！");
                    }
                });
    }

    @Override
    public void punchSuccess() {
        punchSuccess = true;
        mPresenter.stopUpdateTime();
    }

    @Override
    public void updateTime(String time) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
