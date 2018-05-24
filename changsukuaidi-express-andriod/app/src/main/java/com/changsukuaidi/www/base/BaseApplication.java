package com.changsukuaidi.www.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.DistrictSearchRequest;
import com.baidu.trace.api.entity.FilterCondition;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.entity.UpdateEntityRequest;
import com.baidu.trace.api.entity.UpdateEntityResponse;
import com.baidu.trace.api.track.DistanceRequest;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.TransportMode;
import com.changsukuaidi.www.R;
import com.changsukuaidi.www.baidumap.utils.TraceUtils;
import com.changsukuaidi.www.bean.TokenBean;
import com.changsukuaidi.www.dagger.comp.AppComp;
import com.changsukuaidi.www.dagger.comp.BaseComp;
import com.changsukuaidi.www.dagger.comp.DaggerAppComp;
import com.changsukuaidi.www.dagger.comp.DaggerBaseComp;
import com.changsukuaidi.www.dagger.module.BaseModule;
import com.changsukuaidi.www.dagger.module.ClientModule;
import com.changsukuaidi.www.dagger.module.HttpModule;
import com.changsukuaidi.www.service.LocateAndOrientationService;
import com.changsukuaidi.www.utils.ACache;
import com.changsukuaidi.www.utils.Content;
import com.changsukuaidi.www.utils.log.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Describe
 * @Author zhiyicx
 * @Date 2017/11/30
 * @Contact
 */

public class BaseApplication extends Application implements OnTraceListener, OnCustomAttributeListener {
    private static BaseApplication mApplication;
    private static AppComp appComp;
    private TokenBean mTokenBean;  // token信息
    private LBSTraceClient mTraceClient;
    private Trace mTrace;
    private int mServiceID;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
            layout.setPrimaryColorsId(android.R.color.white, R.color.common_text_grey);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init();
        //LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        mApplication = this;

        BaseComp baseComp = DaggerBaseComp.builder()
                .baseModule(new BaseModule(this))
                .httpModule(new HttpModule())
                .build();

        appComp = DaggerAppComp.builder()
                .baseComp(baseComp)
                .clientModule(new ClientModule())
                .build();

        // init baidu map
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        initTraceClient();

        // 开启定位和方向传感器Service
        startLocAndOrientationService();
    }

    private void initTraceClient() {
        mTraceClient = new LBSTraceClient(this);
        mTraceClient.setOnCustomAttributeListener(this);
    }

    /**
     * 开启定位和方向传感器Service
     */
    private void startLocAndOrientationService() {
        startService(new Intent(this, LocateAndOrientationService.class));
    }

    public static BaseApplication getApplication() {
        return mApplication;
    }

    public static AppComp getAppComp() {
        return appComp;
    }

    /**
     * 获取一个唯一的字符串
     *
     * @return
     */
    public String getUUID() {
        @SuppressLint("HardwareIds")
        String androidID = Settings.Secure.getString(mApplication.getContentResolver(), Settings.Secure.ANDROID_ID);
        @SuppressLint("HardwareIds")
        String serialId = Build.SERIAL;
        UUID deviceUuid = new UUID(androidID.hashCode(), serialId.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 获取用户登录token
     *
     * @return 返回token, token为null时需要登录, 否则直接进入
     */
    public TokenBean getToken() {
        if (mTokenBean == null)
            mTokenBean = (TokenBean) ACache.get(mApplication).getAsObject(Content.TOKEN);
        return mTokenBean;
    }

    /**
     * 清空token
     */
    public void clearTokenAndUser() {
        mTokenBean = null;
        // 清空token
        ACache.get(this).put(Content.TOKEN, (Serializable) null);
        ACache.get(this).put(Content.USER, (Serializable) null);
        ACache.get(this).put(Content.CAR_INFO, (Serializable) null);
    }

    /**
     * 开启鹰眼服务
     */
    public void initTraceService(int id, String name) {
        if (mTrace == null)
            mTrace = TraceUtils.createTrace(id, name);
        // 开启服务
        mTraceClient.startTrace(mTrace, this);
    }

    /**
     * 开启鹰眼服务并开始采集轨迹数据
     */
    public void initTraceServiceAndStartTrace(int id, String name) {
        mServiceID = id;
        if (mTrace == null)
            mTrace = TraceUtils.createTrace(id, name);
        // 开启服务
        mTraceClient.startTrace(mTrace, this);
        // 开启采集
        mTraceClient.startGather(this);
    }

    /**
     * 开始采集轨迹数据
     */
    public void startTrace() {
        if (mTraceClient != null) {
            // 开启采集
            mTraceClient.startGather(this);
        }
    }

    /**
     * 停止采集轨迹数据
     */
    public void stopTrace() {
        // 停止采集
        mTraceClient.stopGather(this);
    }

    /**
     * 停止服务同时会停止轨迹采集
     */
    public void stopTraceService() {
        // 停止服务
        mTraceClient.stopTrace(mTrace, this);
    }

    /**
     * 更新Entity的信息
     */
    public void updateEntityInfo(String name, String mobile, String img, String type, String desc, String address) {
        UpdateEntityRequest request = new UpdateEntityRequest();
        request.setServiceId(mServiceID);
        Map<String, String> map = new HashMap<>(); // 修改自定义属性的值
        map.put("user_img", img);
        map.put("mobile", mobile);
        map.put("type", type);
        map.put("address", address);
        request.setColumns(map);
        request.setEntityName(name); // 设置需要修改的Entity，name是Entity的标识
        request.setEntityDesc(desc);// 修改描述信息
        mTraceClient.updateEntity(request, new OnEntityListener() {
            @Override
            public void onUpdateEntityCallback(UpdateEntityResponse updateEntityResponse) {
                super.onUpdateEntityCallback(updateEntityResponse);
            }
        });
    }

    /**
     * 获取当前城市的同事的位置
     *
     * @param type
     * @param city
     * @param listener
     */
    public void getEntities(String type, String city, OnEntityListener listener) {
        DistrictSearchRequest request = new DistrictSearchRequest();
        FilterCondition condition = new FilterCondition();
        Map<String, String> map = new HashMap<>(); // 查询自定义属性的值
        if (!TextUtils.isEmpty(type)) {
            map.put("type", type);
        }
        condition.setColumns(map);
        condition.setActiveTime(System.currentTimeMillis() / 1000 - 5 * 60); // 获取5分钟内的活跃的司机或快递员
        request.setFilterCondition(condition);
        request.setServiceId(mServiceID);
        request.setKeyword(city);
        mTraceClient.districtSearchEntity(request, listener);
    }

    /**
     * 获取一段时间内的里程
     *
     * @param entityName
     * @param startTime
     * @param endTime
     * @param listener
     */
    public void getDistance(String entityName, long startTime, long endTime, OnTrackListener listener) {
        // 请求标识
        int tag = 2;
        // 轨迹服务ID
        long serviceId = mServiceID;
        // 创建里程查询请求实例
        DistanceRequest distanceRequest = new DistanceRequest(tag, serviceId, entityName);
        // 设置开始时间
        distanceRequest.setStartTime(startTime);
        // 设置结束时间
        distanceRequest.setEndTime(endTime);
        // 设置需要纠偏
        distanceRequest.setProcessed(true);
        // 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(true);
        // 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.driving);
        // 设置纠偏选项
        distanceRequest.setProcessOption(processOption);
        // 设置里程填充方式为驾车
        distanceRequest.setSupplementMode(SupplementMode.driving);
        // 查询里程
        mTraceClient.queryDistance(distanceRequest, listener);
    }

    /**
     * 获取一段时间内的历史轨迹
     *
     * @param entityName
     * @param startTime
     * @param endTime
     * @param listener
     */
    public void getHistoryTrace(String entityName, long startTime, long endTime, OnTrackListener listener) {
        // 请求标识
        int tag = 1;
        // 创建历史轨迹请求实例
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, mServiceID, entityName);
        // 设置开始时间
        historyTrackRequest.setStartTime(startTime);
        // 设置结束时间
        historyTrackRequest.setEndTime(endTime);
        // 设置需要纠偏
        historyTrackRequest.setProcessed(true);
        // 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要抽稀
        processOption.setNeedVacuate(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(true);
        // 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(500);
        // 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.driving);
        // 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
        // 设置里程填充方式为驾车
        historyTrackRequest.setSupplementMode(SupplementMode.driving);
        // 查询轨迹
        mTraceClient.queryHistoryTrack(historyTrackRequest, listener);
    }

    @Override
    public void onBindServiceCallback(int i, String s) {

    }

    @Override
    public void onStartTraceCallback(int i, String s) {

    }

    @Override
    public void onStopTraceCallback(int i, String s) {

    }

    @Override
    public void onStartGatherCallback(int i, String s) {

    }

    @Override
    public void onStopGatherCallback(int i, String s) {

    }

    @Override
    public void onPushCallback(byte b, PushMessage pushMessage) {
    }

    @Override
    public void onInitBOSCallback(int i, String s) {

    }

    @Override
    public Map<String, String> onTrackAttributeCallback() {
        return null;
    }

    @Override
    public Map<String, String> onTrackAttributeCallback(long locTime) {
        return null;
    }
}
