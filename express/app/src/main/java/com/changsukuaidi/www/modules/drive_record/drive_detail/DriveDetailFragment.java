package com.changsukuaidi.www.modules.drive_record.drive_detail;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.baidumap.utils.MapUtils;
import com.changsukuaidi.www.base.BaseFragment;
import com.changsukuaidi.www.base.BasePresenter;
import com.changsukuaidi.www.base.i.IBaseView;
import com.changsukuaidi.www.bean.DriveRecordBean;
import com.changsukuaidi.www.databinding.FragmentDriveHistoryDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/8
 * @Contact 605626708@qq.com
 */

public class DriveDetailFragment extends BaseFragment<BasePresenter, FragmentDriveHistoryDetailBinding> implements IBaseView {
    private DriveRecordBean mDriveRecordBean;
    private ArrayList<LatLng> mLatLngs; // 测试数据
    private BaiduMap mBaiduMap;

    @Override
    protected void setPresenter() {

    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_drive_history_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolbarTitle("行车记录");
        mBaiduMap = mViewBinding.map.getMap();
        mViewBinding.map.showZoomControls(false);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mBaiduMap.setIndoorEnable(true);

        if (mLatLngs != null && mLatLngs.size() > 0) {
            MapUtils.moveToLocation(mBaiduMap, mLatLngs.get(0));

            MapUtils.addOverlays(mBaiduMap,
                    MapUtils.createMarker(mLatLngs.get(0), R.mipmap.ico_end_center),
                    MapUtils.createMarker(mLatLngs.get(mLatLngs.size() - 1), R.mipmap.ico_main_origin));

            ArrayList<BitmapDescriptor> bitmapDescriptors = new ArrayList<>();
            bitmapDescriptors.add(BitmapDescriptorFactory.fromResource(R.mipmap.ico_green));
            ArrayList<Integer> indexs = new ArrayList<>();
            indexs.add(0);

            MapUtils.addOverlays(mBaiduMap, MapUtils.createPolylineOptions(mLatLngs, bitmapDescriptors, indexs, true));
        }
    }

    @Override
    protected void initIntent() {
        mDriveRecordBean = getActivity().getIntent().getParcelableExtra("drive_record");
        DriveRecordBean.Position position = mDriveRecordBean.getPosition();
        mLatLngs = new ArrayList<>();
        if (position != null) {
            String json = position.getPosition().replaceAll("(&quot;)", "\"");
            Gson gson = new Gson();
            mLatLngs = gson.fromJson(json, new TypeToken<ArrayList<LatLng>>(){}.getType());
        }
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
    public void onDestroy() {
        mViewBinding.map.onDestroy();
        super.onDestroy();
    }
}
