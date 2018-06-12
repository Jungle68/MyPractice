package com.changsukuaidi.www.modules.task.end_point;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.baidumap.utils.MapUtils;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.bean.TaskBean;
import com.changsukuaidi.www.databinding.FragmentEndPointBinding;
import com.changsukuaidi.www.databinding.PopupWindowForCallPhoneBinding;
import com.changsukuaidi.www.databinding.PopupWindowForSureGrayCancelBinding;
import com.changsukuaidi.www.databinding.PopupWindowOpenUseOtherBinding;
import com.changsukuaidi.www.modules.task.detail.TaskDetailActivity;
import com.changsukuaidi.www.service.LocateAndOrientationService;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

public class EndPointFragment extends BaseFragment<EndPointPresenter, FragmentEndPointBinding> implements IEndPointView, BaiduMap.OnMarkerClickListener {
    private TaskBean mTaskBean;
    // 百度地图
    private BaiduMap mBaiduMap;
    private MyLocationData locData; // 定位的数据
    private BDLocation mBDLocation; // 定位的数据
    private CommonPopupWindow mSureWindow; // 弹窗
    private Marker endMarker; // 终点标记
    private Marker startMarker; // 起点标记
    private int clickMarker; // 0、未点击，1、起点，2，终点
    private boolean isFirstLocate = true; // 第一次定位需要移动到对应位置

    @Override
    protected void setPresenter() {
        DaggerEndPointComp.builder()
                .appComp(BaseApplication.getAppComp())
                .endPointModule(new EndPointModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_end_point;
    }

    @Override
    protected void initIntent() {
        mTaskBean = getActivity().getIntent().getParcelableExtra("task_bean");
        if (mTaskBean == null) {
            ToastUtils.showToast("传入无用的参数");
        }
    }

    @Override
    protected void initView() {
        super.initView();

        // 初始化任务视图
        initTaskView();
        //
        RxView.clicks(mViewBinding.fabUseOtherOpen)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> showSureWindow(R.id.fab_use_other_open));
        RxView.clicks(mViewBinding.cancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> getActivity().finish());

        mBaiduMap = mViewBinding.map.getMap();
        mViewBinding.map.showZoomControls(false);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        // 设置默认的缩放级别 3~19 , 19最近
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));
        // 启用室内图
        mBaiduMap.setIndoorEnable(true);
        mBaiduMap.setOnMarkerClickListener(this);

        if (LocateAndOrientationService.sBDLocation != null) {
            mBDLocation = LocateAndOrientationService.sBDLocation;
        }

        if (mBDLocation != null && TextUtils.equals(mBDLocation.getCountryCode(), "0")) {
            moveToLocation(mBDLocation);
        }

        endMarker = (Marker) mBaiduMap.addOverlay(MapUtils.createMarker(new LatLng(mTaskBean.getEndLatitude(), mTaskBean.getEndLongitude()), R.mipmap.ico_main_single));
        startMarker = (Marker) mBaiduMap.addOverlay(MapUtils.createMarker(new LatLng(mTaskBean.getStartLatitude(), mTaskBean.getStartLongitude()), R.mipmap.ico_main_origin));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker == endMarker) {
            clickMarker = 2;
            endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ico_single_selected));
            startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_origin));
        } else if (marker == startMarker) {
            clickMarker = 1;
            endMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_single));
            startMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_origin_selected));
        }
        return false;
    }

    @Override
    public boolean showFullScreen() {
        return true;
    }

    @Override
    public boolean useTitle() {
        return false;
    }

    @Override
    protected boolean usePermissions() {
        return true;
    }

    @Override
    public void onResume() {
        mViewBinding.map.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mViewBinding.map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mViewBinding.map.onDestroy();
        super.onDestroyView();
    }

    /**
     * 更新位置信息
     *
     * @param location 定位服务发过来的定位信息
     */
    @Subscribe
    public void updateLoc(@NonNull BDLocation location) {
        if (mBDLocation == null) {
            moveToLocation(location);
        }
        mBDLocation = location;
    }

    /**
     * 更新手机的方向信息
     *
     * @param orientationDegrees 传递过来的方向信息
     */
    @Subscribe
    public void updateOrientation(float[] orientationDegrees) {
        if (locData != null && mBaiduMap != null && mBDLocation != null) {
            // 构造定位数据
            locData = new MyLocationData.Builder()
                    .accuracy(mBDLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(orientationDegrees[0])
                    .latitude(mBDLocation.getLatitude())
                    .longitude(mBDLocation.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);
        }
    }

    /**
     * 初始化任务视图
     */
    private void initTaskView() {
        // 任务类型
        int type = mTaskBean.getTaskType();
        if (type == 1) {
            mViewBinding.dispatchType.setText("药品配送");
        } else if (type == 2) {
            mViewBinding.dispatchType.setText("药品中转");
        } else if (type == 3) {
            mViewBinding.dispatchType.setText("物料配送");
        }

        // 是否是优先件
        if (mTaskBean.getPriority() == 1) {
            mViewBinding.prior.setVisibility(View.VISIBLE);
            mViewBinding.prior.setText("优先");
        } else {
            mViewBinding.prior.setVisibility(View.GONE);
        }

        // 是否是中转件
        if (mTaskBean.getIsTransfer() == 1) {
            mViewBinding.transfer.setVisibility(View.VISIBLE);
            mViewBinding.transfer.setText("【转】");
        } else {
            mViewBinding.transfer.setVisibility(View.GONE);
        }
        mViewBinding.startPos.setText(mTaskBean.getStartAddress());
        mViewBinding.startDistance.setText(String.format("%s公里", mTaskBean.getStartDistance()));
        mViewBinding.endPos.setText(mTaskBean.getEndAddress());
        mViewBinding.endDistance.setText(String.format("%s公里", mTaskBean.getEndDistance()));
        mViewBinding.consignee.setText(mTaskBean.getTaskType() == 3 ? "" : mTaskBean.getReceiverName());
        mViewBinding.consigneePhone.setText(mTaskBean.getTaskType() == 3 ? "电话： " + mTaskBean.getReceiverPhone() : mTaskBean.getReceiverPhone());
        mViewBinding.drugTime.setText(mTaskBean.getMedicineTime());

        // 查看任务详情
        RxView.clicks(mViewBinding.detail)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> startActivity(new Intent(getActivity(), TaskDetailActivity.class)));

        // 拨打收件人电话弹窗
        RxView.clicks(mViewBinding.consigneePhone)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> showSureWindow(R.id.consignee_phone));

    }

    // 初始化弹出，打电话，其他应用打开，抢单
    private void showSureWindow(int resId) {
        if (resId == R.id.consignee_phone) {
            // 是否到拨打电话页面
            mSureWindow = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_for_call_phone)
                    .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowForCallPhoneBinding phoneBinding = (PopupWindowForCallPhoneBinding) view;
                        phoneBinding.phone.setText(mTaskBean.getReceiverPhone());
                        phoneBinding.phone.setOnClickListener(v1 -> {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTaskBean.getReceiverPhone()));
                            startActivity(intent);
                        });

                        // cancel
                        phoneBinding.cancel.setOnClickListener(v2 -> mSureWindow.dismiss());
                    })
                    .create();
            mSureWindow.setBackGroundLevel(0.5f);
            mSureWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        } else if (resId == R.id.grab_task) { // 抢单弹窗
            mSureWindow = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_for_sure_gray_cancel)
                    .setWidthAndHeight(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowForSureGrayCancelBinding binding = (PopupWindowForSureGrayCancelBinding) view;
                        binding.title.setText(getString(R.string.ensure_grab_task));
                        binding.ok.setText(getString(R.string.grabing_task));
                        binding.cancel.setText(getString(R.string.cancel_grab_task));
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
            mSureWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else if (resId == R.id.fab_use_other_open) { // 用其他地图打开
            if (clickMarker != 1 && clickMarker != 2) {
                ToastUtils.showToast("请点击图标选中终点");
                return;
            }
            mSureWindow = new CommonPopupWindow.Builder(getContext())
                    .setView(R.layout.popup_window_open_use_other)
                    .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowOpenUseOtherBinding binding = (PopupWindowOpenUseOtherBinding) view;
                        RxView.clicks(binding.mapBaidu)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    mSureWindow.dismiss();
                                    if (MapUtils.isBaiduMapInstalled()) {
                                        // 百度地图驾车路线规划-App版-不带途经点
                                        Intent intent = new Intent();
                                        if (clickMarker == 1) {
                                            intent.setData(MapUtils.getBaiduDirectionAppUri(mTaskBean.getStartLatitude(),
                                                    mTaskBean.getStartLongitude(), mTaskBean.getStartAddress()));
                                            startActivity(intent);
                                        } else if (clickMarker == 2) {
                                            intent.setData(MapUtils.getBaiduDirectionAppUri(mTaskBean.getEndLatitude(),
                                                    mTaskBean.getEndLongitude(), mTaskBean.getEndAddress()));
                                            startActivity(intent);
                                        }
                                    } else {
                                        // 百度地图驾车路线规划-网页不带途经点
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        if (clickMarker == 1) {
                                            intent.setData(MapUtils.getBaiduDirectionUri(mTaskBean.getStartLatitude(),
                                                    mTaskBean.getStartLongitude(), mTaskBean.getStartAddress()));
                                            startActivity(intent);
                                        } else if (clickMarker == 2) {
                                            intent.setData(MapUtils.getBaiduDirectionUri(mTaskBean.getEndLatitude(),
                                                    mTaskBean.getEndLongitude(), mTaskBean.getEndAddress()));
                                            startActivity(intent);
                                        }
                                    }
                                });
                        RxView.clicks(binding.mapGaode)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    mSureWindow.dismiss();
                                    if (MapUtils.isGaoDeMapInstalled()) {
                                        // 高德地图驾车路线规划-App不带途经点
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        intent.addCategory("android.intent.category.DEFAULT");
                                        intent.setPackage("com.autonavi.minimap");
                                        if (clickMarker == 1) {
                                            intent.setData(MapUtils.getGaodeDirectionAppUri(mTaskBean.getStartLatitude(),
                                                    mTaskBean.getStartLongitude(), mTaskBean.getStartAddress()));
                                            startActivity(intent);
                                        } else if (clickMarker == 2) {
                                            intent.setData(MapUtils.getGaodeDirectionAppUri(mTaskBean.getEndLatitude(),
                                                    mTaskBean.getEndLongitude(), mTaskBean.getEndAddress()));
                                            startActivity(intent);
                                        }
                                    } else {
                                        // 高德地图驾车路线规划-网页不带途经点
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        if (clickMarker == 1) {
                                            intent.setData(MapUtils.getGaodeDirectionUri(mTaskBean.getStartLatitude(),
                                                    mTaskBean.getStartLongitude(), mTaskBean.getStartAddress()));
                                            startActivity(intent);
                                        } else if (clickMarker == 2) {
                                            intent.setData(MapUtils.getGaodeDirectionUri(mTaskBean.getEndLatitude(),
                                                    mTaskBean.getEndLongitude(), mTaskBean.getEndAddress()));
                                            startActivity(intent);
                                        }
                                    }
                                });
                        RxView.clicks(binding.mapTencent)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    mSureWindow.dismiss();
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    if (clickMarker == 1) {
                                        intent.setData(MapUtils.getTencentDirectionUri(mTaskBean.getStartLatitude(),
                                                mTaskBean.getStartLongitude(), mTaskBean.getStartAddress()));
                                        startActivity(intent);
                                    } else if (clickMarker == 2) {
                                        intent.setData(MapUtils.getTencentDirectionUri(mTaskBean.getEndLatitude(),
                                                mTaskBean.getEndLongitude(), mTaskBean.getEndAddress()));
                                        startActivity(intent);
                                    }
                                });
                        RxView.clicks(binding.cancel)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> mSureWindow.dismiss());
                    })
                    .create();
            mSureWindow.setBackGroundLevel(0.5f);
            mSureWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 移动到定位的地点
     *
     * @param location 定位数据
     */
    private void moveToLocation(@NonNull BDLocation location) {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                true, BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_my), 0x00000000, 0x00000000);
        mBaiduMap.setMyLocationConfiguration(config);
        if (isFirstLocate) {
            isFirstLocate = false;
            // 移动地图到定位点
            MapUtils.moveToLocation(mBaiduMap, location);
        }
    }
}
