package com.changsukuaidi.www.remote;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.UserBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/8
 * @Contact 605626708@qq.com
 */

public interface PunchClient {
    @GET("Member/detail")
    Observable<BaseJson<UserBean>> getUserInfo();

    @GET("Driver/Api/signIn")
    Observable<BaseJson<CarBean>> getCarInfo();

    @POST("Driver/Api/signIn")
    @FormUrlEncoded
    Observable<BaseJson<Object>> startPunch(@Field("type") String type,
                                            @Field("area1") String area);

    @POST("Driver/Api/signIn")
    @FormUrlEncoded
    Observable<BaseJson<Object>> offWorkPunch(@Field("type") String type,
                                              @Field("area2") String area,
                                              @Field("distance") String distance,
                                              @Field("position") String position,
                                              @Field("oils") double oils);
}
