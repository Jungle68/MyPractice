package com.changsukuaidi.www.modules.home;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.baidumap.utils.MapUtils;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.common.utils.DisplayUtils;
import com.changsukuaidi.www.databinding.FragmentHomeBinding;
import com.changsukuaidi.www.databinding.WindowMyTaskBinding;
import com.changsukuaidi.www.modules.colleague_pos.ColleaguePosActivity;
import com.changsukuaidi.www.modules.drive_record.DriveRecordActivity;
import com.changsukuaidi.www.modules.punch.PunchActivity;
import com.changsukuaidi.www.modules.settings.SettingsActivity;
import com.changsukuaidi.www.modules.task.MyTaskActivity;
import com.changsukuaidi.www.modules.task.grab_task.GrabTaskActivity;
import com.changsukuaidi.www.modules.task.history.HistoryActivity;
import com.changsukuaidi.www.scan.CaptureActivity;
import com.changsukuaidi.www.service.LocateAndOrientationService;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.ActivityUtils;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class HomeFragment extends BaseFragment<HomePresenter, FragmentHomeBinding> implements IHomeView, CommonPopupWindow.ViewInterface<WindowMyTaskBinding> {
    // 百度地图
    BaiduMap mBaiduMap;
    // 侧边栏上的头像
    protected ImageView mNavigationHeaderIcon;
    private TextView mNavigationName;
    // 我的任务弹窗
    protected CommonPopupWindow mTaskPopupWindow;
    private boolean isFirstLocate = true; // 第一次定位需要移动到对应位置

    // 上一次选中的全部, 我的任务, 可抢订单的位置
    protected int index; // 0 全部, 1 我的任务, 2 可抢订单
    private BDLocation mBDLocation; // 定位的数据

    private ArrayList<LatLng> mLatLngs; // 测试数据
    private UserBean mUserBean;

    @Override
    protected void setPresenter() {
        DaggerHomeComp.builder()
                .appComp(BaseApplication.getAppComp())
                .homeModule(new HomeModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        mPresenter.loadUserInfo();
    }

    @Override
    protected void initView() {
        super.initView();
        // 初始化NavigationView
        initNavigationView();

        // 打开 drawer
        mViewBinding.user.setOnClickListener(v -> mViewBinding.drawer.openDrawer(Gravity.START));

        // 我的任务
        mViewBinding.myTask.setOnClickListener(v -> startActivity(new Intent(getContext(), MyTaskActivity.class)));

        // 当位未定位时，显示默认定位中
        mViewBinding.location.setText("定位中...");

        // 当前显示图表 : 全部, 我的任务, 可抢订单
        mViewBinding.all.setSelected(true);
        mViewBinding.all.setOnClickListener(this::selectOne);
        mViewBinding.canGrabList.setOnClickListener(this::selectOne);

        mViewBinding.myTask.setOnClickListener(v -> {
            if (mTaskPopupWindow == null)
                mTaskPopupWindow = new CommonPopupWindow.Builder(getContext())
                        .setView(R.layout.window_my_task)
                        .setViewOnclickListener(this)
                        .create();
            if (index == 1) {
                ActivityUtils.setWindowBackgroundAlpha(getContext(), 0.5f);
                mTaskPopupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
            } else {
                selectOne(v);
            }
        });

        RxView.clicks(mViewBinding.taskList)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> startActivity(new Intent(getContext(), MyTaskActivity.class)));

        // 可抢订单
        RxView.clicks(mViewBinding.grabList)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> startActivity(new Intent(getActivity(), GrabTaskActivity.class)));

        // 刷新按钮
        RxView.clicks(mViewBinding.mapRefresh)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> ToastUtils.showToast("刷新"));

        // 地图相关初始化
        mBaiduMap = mViewBinding.map.getMap();
        mViewBinding.map.showZoomControls(false);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        // 设置默认的缩放级别 3~19 , 19最近
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));
        // 启用室内图
        mBaiduMap.setIndoorEnable(true);

        if (LocateAndOrientationService.sBDLocation != null) {
            mBDLocation = LocateAndOrientationService.sBDLocation;
        }

        if (mBDLocation != null && TextUtils.equals(mBDLocation.getCountryCode(), "0")) {
            moveToLocation(mBDLocation);
        }

        // 添加测试数据
        addTestDatas();
        // 点击走马灯视图的item后
        mViewBinding.marquee.setOnItemClickListener((position, textView) -> ToastUtils.showToast(textView.getText().toString()));
        showMarquee();
    }

    private void addTestDatas() {
        mLatLngs = new ArrayList<>();
        mLatLngs.add(new LatLng(30.579236547019285, 104.06556720795137));
        mLatLngs.add(new LatLng(30.563814047246407, 104.07794585787887));
        mLatLngs.add(new LatLng(30.564055042853063, 104.06254890143785));
        mLatLngs.add(new LatLng(30.56349531019283, 104.0687472094567));
        mLatLngs.add(new LatLng(30.587040134189788, 104.07969755362333));
        mLatLngs.add(new LatLng(30.57513241396209, 104.06397720719872));

        for (int i = 0; i < mLatLngs.size(); i++) {
            BitmapDescriptor bitmap = null;
            if (i == 0) {
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_drug);
            } else if (i == 1) {
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_priority);
            } else if (i == 2) {
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_material);
            } else if (i == 3) {
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_single);
            } else if (i == 4) {
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ico_courier_center);
            } else if (i == 5) {
                bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ico_driver_center);
            }
            OverlayOptions options = new MarkerOptions()
                    .position(mLatLngs.get(i))  //设置Marker的位置
                    .icon(bitmap)  //设置Marker图标
                    .anchor(0.5f, 1f)
                    .zIndex(11)  //设置Marker所在层级
                    .draggable(false);  //设置手势拖拽
            //将Marker添加到地图上
            mBaiduMap.addOverlay(options);
        }
    }

    private void showMarquee() {
        mViewBinding.llMarquee.setVisibility(View.VISIBLE);

        List<String> info = new ArrayList<>();
        info.add("你收到一个药品配送任务,点击查看");
        info.add("你收到一个物料配送任务,点击查看");
        info.add("你收到一个药品中转任务,点击查看");

        mViewBinding.marquee.startWithList(info);
    }

    private void initNavigationView() {
        // 图标恢复原色
        mViewBinding.navigation.setItemIconTintList(null);
        // 宽度调整为半屏
        mViewBinding.navigation.getLayoutParams().width = DisplayUtils.getWindowWidth(getContext()) / 12 * 7;
        // menu'事件
        mViewBinding.navigation.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.punch:
                    intent = new Intent(getContext(), PunchActivity.class);
                    intent.putExtra("is_off_work", true);
                    startActivity(intent);
                    break;
                case R.id.my_task:
                    intent = new Intent(getContext(), MyTaskActivity.class);
                    startActivity(intent);
                    break;
                case R.id.driving_record:
                    intent = new Intent(getContext(), DriveRecordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.historical_order:
                    intent = new Intent(getContext(), HistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.view_colleague_pos:
                    intent = new Intent(getContext(), ColleaguePosActivity.class);
                    startActivity(intent);
                    break;
                case R.id.batch_scanning_pickup:
                    Observable.just(1)
                            .compose(mRxPermissions.ensure(Manifest.permission.CAMERA))
                            .subscribe(granted -> {
                                if (granted)
                                    startActivity(new Intent(getActivity(), CaptureActivity.class));
                            });
                    break;
                case R.id.setting:
                    intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        View navigationHeader = mViewBinding.navigation.getHeaderView(0);
        mNavigationHeaderIcon = navigationHeader.findViewById(R.id.menu_head);
        mNavigationName = navigationHeader.findViewById(R.id.menu_name);

        setUserInfo();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewBinding.marquee.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewBinding.marquee.stopFlipping();
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
                .into(mNavigationHeaderIcon);
        mNavigationName.setText(mUserBean == null ? "" : mUserBean.getNickname());
        if (mUserBean != null) {
            ((BaseApplication) getActivity().getApplication()).updateEntityInfo(mUserBean.getNickname(), "110", "hehheheh", "快递员", "hhhhhh", "3123123");
            ((BaseApplication) getActivity().getApplication()).initTraceServiceAndStartTrace(155338, mUserBean.getNickname());
        }
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
        setUserInfo();
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

    @Override
    public void getChildViewBinding(WindowMyTaskBinding view, int layoutResId) {
        RxView.clicks(view.materialDistribution)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    selectOne(mViewBinding.myTask);
                    mViewBinding.myTask.setText(getString(R.string.material_distribution));
                    mTaskPopupWindow.dismiss();
                });

        RxView.clicks(view.drugTransit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    selectOne(mViewBinding.myTask);
                    mViewBinding.myTask.setText(getString(R.string.drug_transit));
                    mTaskPopupWindow.dismiss();
                });

        RxView.clicks(view.drugDelivery)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    selectOne(mViewBinding.myTask);
                    mViewBinding.myTask.setText(getString(R.string.drug_delivery));
                    mTaskPopupWindow.dismiss();
                });

    }

    /**
     * 当前显示图表 : 全部, 我的任务, 可抢订单
     *
     * @param view 当前选中的项
     */
    private void selectOne(View view) {
        if (view == mViewBinding.all) {
            index = 0;
            mViewBinding.all.setSelected(true);
            mViewBinding.myTask.setSelected(false);
            mViewBinding.canGrabList.setSelected(false);
        } else if (view == mViewBinding.myTask) {
            index = 1;
            mViewBinding.all.setSelected(false);
            mViewBinding.myTask.setSelected(true);
            mViewBinding.canGrabList.setSelected(false);
        } else {
            index = 2;
            mViewBinding.all.setSelected(false);
            mViewBinding.myTask.setSelected(false);
            mViewBinding.canGrabList.setSelected(true);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 移动到定位的地点
     *
     * @param location 定位数据
     */
    private void moveToLocation(@NonNull BDLocation location) {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        if (isFirstLocate) {
            isFirstLocate = false;
            // 移动地图到定位点
            MapUtils.moveToLocation(mBaiduMap, location);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                    true, BitmapDescriptorFactory.fromResource(R.mipmap.ico_main_my), 0x00000000, 0x00000000);
            mBaiduMap.setMyLocationConfiguration(config);
        }
    }

    /**
     * 更新位置信息
     *
     * @param location 定位服务发过来的定位信息
     */
    @Subscribe
    public void updateLoc(@NonNull BDLocation location) {
        // 设置定位城市
        mViewBinding.location.setText(location.getCity());
        moveToLocation(location);
        mBDLocation = location;
    }

    /**
     * 更新手机的方向信息
     *
     * @param orientationDegrees 传递过来的方向信息
     */
    @Subscribe
    public void updateOrientation(float[] orientationDegrees) {
        if (mBaiduMap != null && mBDLocation != null) {
            // 构造定位数据
            mBaiduMap.setMyLocationData(new MyLocationData.Builder()
                    .accuracy(mBDLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(orientationDegrees[0])
                    .latitude(mBDLocation.getLatitude())
                    .longitude(mBDLocation.getLongitude())
                    .build());
        }
    }

    @Override
    public void loadUserInfo(int status) {
        setUserInfo();
    }
}
