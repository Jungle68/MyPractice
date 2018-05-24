package com.changsukuaidi.www.modules.colleague_pos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.api.entity.DistrictSearchResponse;
import com.baidu.trace.api.entity.EntityInfo;
import com.baidu.trace.api.entity.OnEntityListener;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.baidumap.utils.MapUtils;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.GlideApp;
import com.changsukuaidi.www.bean.ColleagueBean;
import com.changsukuaidi.www.bean.UserBean;
import com.changsukuaidi.www.databinding.FragmentColleaguePositionBinding;
import com.changsukuaidi.www.databinding.PopupWindowColleagueTypeBinding;
import com.changsukuaidi.www.databinding.PopupWindowForCallPhoneBinding;
import com.changsukuaidi.www.service.LocateAndOrientationService;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.ToastUtils;
import com.changsukuaidi.www.widget.popup.CommonPopupWindow;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

public class ColleaguePosFragment extends BaseFragment<ColleaguePresenter, FragmentColleaguePositionBinding> implements IColleaguePosView, BaiduMap.OnMarkerClickListener, BaiduMap.OnMapClickListener {
    // 百度地图
    private BaiduMap mBaiduMap;
    private MyLocationData locData;                     // 定位的数据
    private BDLocation mBDLocation;                     // 定位的数据
    private Marker mLastClickMarker;                    // 之前点击的Marker
    private CommonPopupWindow mColleagueTypePop;        // 选择显示同事种类的弹窗
    private boolean isFirstLocate = true;               // 第一次定位需要移动到对应位置
    private CommonPopupWindow mPhonePop;                // 但电话的弹窗
    private OnEntityListener mEntityListener;
    private ArrayList<ColleagueBean> mColleagueBeans;
    private ArrayList<Marker> mMarkers;
    private String mCurrentType;
    private boolean isRefresh = false;

    @Override
    protected void setPresenter() {
        DaggerColleaguePosComp.builder()
                .appComp(BaseApplication.getAppComp())
                .colleaguePosModule(new ColleaguePosModule(this))
                .build()
                .injectMembers(this);
    }

    @Override
    protected void initView() {
        setToolbarTitleWithRightDrawable("所有同事", R.mipmap.ico_main_task, view -> showColleagueTypePop());
        setToolbarRightDrawable(R.mipmap.ico_refresh, view -> {
            ((BaseApplication) getActivity().getApplication()).getEntities(mCurrentType, mBDLocation.getCity(), mEntityListener);
            isRefresh = true;
        });

        mBaiduMap = mViewBinding.map.getMap();
        mViewBinding.map.showZoomControls(false);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mViewBinding.clColleagueView.setOnClickListener(view -> Log.i(getClass().getName(), "防止点击同事信息View，会点击到地图"));
        // 设置默认的缩放级别 3~19 , 19最近
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18.0f));
        // 启用室内图
        mBaiduMap.setIndoorEnable(true);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);

        if (LocateAndOrientationService.sBDLocation != null) {
            mBDLocation = LocateAndOrientationService.sBDLocation;
        }

        if (mBDLocation != null && TextUtils.equals(mBDLocation.getCountryCode(), "0")) {
            moveToLocation(mBDLocation);
        }

        mColleagueBeans = new ArrayList<>();
        mMarkers = new ArrayList<>();
        /**
         * 获取Entity的监听
         */
        mEntityListener = new OnEntityListener() {
            @Override
            public void onDistrictSearchCallback(DistrictSearchResponse districtSearchResponse) {
                if (isRefresh) {
                    ToastUtils.showToast("刷新成功");
                    isRefresh = false;
                }
                mColleagueBeans.clear();
                mMarkers.clear();
                mBaiduMap.clear();
                ArrayList<EntityInfo> entityInfos = (ArrayList<EntityInfo>) districtSearchResponse.getEntities();
                for (EntityInfo entityInfo : entityInfos) {
                    Map<String, String> map = entityInfo.getColumns();
                    int type = TextUtils.equals("司机", map.get("type")) ? 1 : 2;
                    String cover = map.get("user_img");
                    String name = entityInfo.getEntityName();
                    if (TextUtils.equals(name, ((UserBean) ACache.get(getContext()).getAsObject(Content.USER)).getNickname()))
                        continue;
                    String phone = map.get("mobile");
                    String address = map.get("address");
                    com.baidu.trace.model.LatLng latLng = entityInfo.getLatestLocation().getLocation();
                    ColleagueBean bean = new ColleagueBean(type, cover, name, phone, address, latLng.getLatitude(), latLng.getLongitude());
                    mColleagueBeans.add(bean);
                }
                updateUI();
            }
        };
        // 进来就需要加载全部同事的位置
        ((BaseApplication) getActivity().getApplication()).getEntities("", mBDLocation.getCity(), mEntityListener);
    }

    /**
     * 更新司机和快递员视图
     */
    private void updateUI() {
        int size = mColleagueBeans.size();
        for (int i = 0; i < size; i++) {
            ColleagueBean colleagueBean = mColleagueBeans.get(i);
            int type = colleagueBean.getType();
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(type == 1 ? R.mipmap.ico_driver_center : R.mipmap.ico_courier_center);
            Bundle extra = new Bundle();
            extra.putParcelable("colleague", colleagueBean);

            OverlayOptions options = new MarkerOptions()
                    .position(new LatLng(colleagueBean.getLatitude(), colleagueBean.getLongitude()))  //设置Marker的位置
                    .icon(bitmap)  //设置Marker图标
                    .anchor(0.5f, 1f)
                    .extraInfo(extra)
                    .zIndex(11)  //设置Marker所在层级
                    .draggable(false);  //设置手势拖拽
            //将Marker添加到地图上
            mMarkers.add((Marker) mBaiduMap.addOverlay(options));
        }
    }

    @Override
    protected boolean useEventBus() {
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

    @Override
    public boolean useTitle() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_colleague_position;
    }

    /**
     * 更新位置信息
     *
     * @param location 定位服务发过来的定位信息
     */
    @Subscribe
    public void updateLoc(@Nonnull BDLocation location) {
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
     * 移动到定位的地点
     *
     * @param location 定位数据
     */
    private void moveToLocation(@Nonnull BDLocation location) {
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        hideAll();
        ColleagueBean click = marker.getExtraInfo().getParcelable("colleague");
        if (mLastClickMarker == null) {
            int type = click.getType();
            marker.setIcon(BitmapDescriptorFactory.fromResource(type == 1 ? R.mipmap.ico_driver_selected : R.mipmap.ico_courier_selected));
            mLastClickMarker = marker;
            marker.setVisible(true);
            showColleagueInfo(click);
        }
        if (mLastClickMarker != null && mLastClickMarker != marker) {
            changeLastClickMarker();
        }

        return false;
    }

    public void hideAll() {
        for (Marker marker : mMarkers) {
            marker.setVisible(false);
        }
    }

    public void showAll() {
        for (Marker marker : mMarkers) {
            marker.setVisible(true);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        changeLastClickMarker();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        changeLastClickMarker();
        return false;
    }


    /**
     * 展示点击的同事的信息
     *
     * @param colleagueBean 该同事的数据类
     */
    private void showColleagueInfo(ColleagueBean colleagueBean) {
        mViewBinding.clColleagueView.setVisibility(View.VISIBLE);
        mViewBinding.tvColleagueAddress.setText(String.format("当前位置： %s", colleagueBean.getAddress()));
        mViewBinding.tvColleagueName.setText(colleagueBean.getName());
        GlideApp.with(getActivity())
                .load(colleagueBean.getCover())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .circleCrop()
                .into(mViewBinding.ivColleagueHeader);
        RxView.clicks(mViewBinding.ivColleagueNumber)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> showPhonePop(colleagueBean));
    }

    private void showPhonePop(ColleagueBean colleagueBean) {
        if (mPhonePop == null) {
            mPhonePop = new CommonPopupWindow.Builder(getActivity())
                    .setView(R.layout.popup_window_for_call_phone)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowForCallPhoneBinding binding = (PopupWindowForCallPhoneBinding) view;
                        binding.phone.setText(colleagueBean.getPhone());
                        RxView.clicks(binding.phone)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(o -> startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + colleagueBean.getPhone()))));
                        RxView.clicks(binding.cancel)
                                .throttleFirst(1, TimeUnit.SECONDS)
                                .subscribe(o -> mPhonePop.dismiss());
                    })
                    .setWidthAndHeight(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    .create();
        }
        mPhonePop.setBackGroundLevel(0.5F);
        mPhonePop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 展示选择同事弹窗
     */
    private void showColleagueTypePop() {
        if (mColleagueTypePop == null) {
            mColleagueTypePop = new CommonPopupWindow.Builder(getActivity())
                    .setView(R.layout.popup_window_colleague_type)
                    .setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
                    .setViewOnclickListener((view, layoutResId) -> {
                        PopupWindowColleagueTypeBinding colleagueTypeBinding = (PopupWindowColleagueTypeBinding) view;
                        RxView.clicks(colleagueTypeBinding.all)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    changeLastClickMarker();
                                    mColleagueTypePop.dismiss();
                                    mCurrentType = "";
                                    setToolbarTitle("所有同事");
                                    ((BaseApplication) getActivity().getApplication()).getEntities(mCurrentType, mBDLocation.getCity(), mEntityListener);
                                });
                        RxView.clicks(colleagueTypeBinding.driver)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    changeLastClickMarker();
                                    mColleagueTypePop.dismiss();
                                    mCurrentType = "司机";
                                    setToolbarTitle("司机");
                                    ((BaseApplication) getActivity().getApplication()).getEntities(mCurrentType, mBDLocation.getCity(), mEntityListener);
                                });
                        RxView.clicks(colleagueTypeBinding.courier)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> {
                                    changeLastClickMarker();
                                    mColleagueTypePop.dismiss();
                                    mCurrentType = "快递员";
                                    setToolbarTitle("快递员");
                                    ((BaseApplication) getActivity().getApplication()).getEntities(mCurrentType, mBDLocation.getCity(), mEntityListener);
                                });
                        RxView.clicks(colleagueTypeBinding.cancel)
                                .throttleFirst(2, TimeUnit.SECONDS)
                                .subscribe(o -> mColleagueTypePop.dismiss());
                    })
                    .create();
        }
        mColleagueTypePop.setBackGroundLevel(0.5f);
        mColleagueTypePop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 将最后一个点击的marker设置为未点击状态
     */
    private void changeLastClickMarker() {
        showAll();
        if (mLastClickMarker != null) {
            int type = ((ColleagueBean) mLastClickMarker.getExtraInfo().getParcelable("colleague")).getType();
            mLastClickMarker.setIcon(BitmapDescriptorFactory.fromResource(type == 1 ? R.mipmap.ico_driver_center : R.mipmap.ico_courier_center));
            mLastClickMarker = null;
        }
        mViewBinding.clColleagueView.setVisibility(View.GONE);
    }
}
