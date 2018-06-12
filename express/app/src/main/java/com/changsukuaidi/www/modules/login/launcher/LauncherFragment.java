package com.changsukuaidi.www.modules.login.launcher;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.changsukuaidi.www.R;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.databinding.FragmentLauncherBinding;
import com.changsukuaidi.www.modules.home.HomeActivity;
import com.changsukuaidi.www.modules.login.LoginActivity;
import com.changsukuaidi.www.modules.punch.PunchActivity;
import com.changsukuaidi.www.widget.popup.MyAlertDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public class LauncherFragment extends BaseFragment<LauncherPresenter, FragmentLauncherBinding> {

    @Override
    protected void setPresenter() {
        DaggerLauncherComp.builder()
                .appComp(BaseApplication.getAppComp())
                .launcherModule(new LauncherModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_launcher;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mPresenter.getCarInfo();
    }

    @Override
    public boolean useTitle() {
        return false;
    }

    /**
     * 判断是否登录进入登录页面
     * 判断是否是打卡，进入打卡页面或者主页
     *
     * @param carBean
     */
    public void finishGetCarInfo(CarBean carBean) {
        if (((BaseApplication) getActivity().getApplication()).getToken() == null || carBean == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (carBean.getPunchInfo() == null || TextUtils.isEmpty(carBean.getPunchInfo().getStime())) {
            startActivity(new Intent(getActivity(), PunchActivity.class));
        } else {
            startActivity(new Intent(getActivity(), HomeActivity.class));
        }
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getActivity().finish();
    }

    /**
     * 展示网络未连接弹窗，点击退出应用
     */
    public void showNetError() {
        new MyAlertDialog.Builder()
                .setOk("确定")
                .setContent("网络未连接，请查看你的网络！")
                .setCancelable(false)
                .setOkClick((View v) -> getActivity().finish())
                .build().show(getFragmentManager(), "alter");
    }
}

