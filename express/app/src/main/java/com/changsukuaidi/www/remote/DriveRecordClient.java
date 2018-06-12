package com.changsukuaidi.www.remote;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.DriveRecordBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public interface DriveRecordClient {
    @GET("Driver/Api/logList")
    Observable<BaseJson<List<DriveRecordBean>>> getRecordList(@Query("p") int page, @Query("time_stp") String time);
}
