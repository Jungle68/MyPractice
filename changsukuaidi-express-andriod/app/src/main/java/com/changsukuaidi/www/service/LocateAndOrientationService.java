package com.changsukuaidi.www.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.changsukuaidi.www.baidumap.utils.LocationUtils;
import com.changsukuaidi.www.base.BaseApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * @Describe
 * @Author thc
 * @Date 2018/3/14
 */

public class LocateAndOrientationService extends Service implements SensorEventListener {
    public static BDLocation sBDLocation; // 定位数据
    private LocationClient mLocationClient; // 定位client
    private SensorManager mSensorManager;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private Disposable mDisposable;
    private float lastDegrees = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startOrientationSensor(this);
        registerSensorEventListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLocationClient == null){
            mLocationClient = new LocationClient(BaseApplication.getApplication());
            mLocationClient.setLocOption(LocationUtils.createLocationClientOptionDefault());
            mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    sBDLocation = bdLocation;
                    Log.e("Test", "===>>" + bdLocation.getLocType() + "---" + bdLocation.getLocTypeDescription());
                    if (bdLocation != null && TextUtils.equals(bdLocation.getCountryCode(), "0"))
                        EventBus.getDefault().post(bdLocation);
                }

            });
        }
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterSensorEventListener();
    }

    /**
     * 开启方向Sensor，根据方向改变调整等位图标
     */
    private void startOrientationSensor(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);   // 加速度传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);       // 地磁场传感器
    }

    /**
     * 注册sensor监听
     */
    private void registerSensorEventListener() {
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, accelerometer, Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, magnetic, Sensor.TYPE_MAGNETIC_FIELD);
        }
        mDisposable = Flowable.interval(300, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> calculateOrientation());
    }

    /**
     * 注销sensor监听
     */
    private void unregisterSensorEventListener() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.isDisposed();
        }
    }

    /**
     * 计算方向
     */
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);

        if (Math.abs(values[0] - lastDegrees) > 5) {
            EventBus.getDefault().post(values);
            lastDegrees = values[0];
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = sensorEvent.values;
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = sensorEvent.values;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
