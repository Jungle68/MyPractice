package com.changsukuaidi.www.baidumap.utils;

import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.AroundSearchRequest;
import com.baidu.trace.api.entity.FilterCondition;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.LatLng;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;

import java.util.Map;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/1
 */

public class TraceUtils {
    // 创建轨迹
    public static Trace createTrace(int id, String name) {
        // 轨迹服务ID
        long serviceId = id;
        // 设备标识
        String entityName = name;
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。
        // 注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，
        // 且需导入bos-android-sdk-1.0.2.jar。
        boolean isNeedObjectStorage = false;
        // 初始化轨迹服务
        Trace mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        return mTrace;
    }
}
