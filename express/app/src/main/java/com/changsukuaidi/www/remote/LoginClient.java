package com.changsukuaidi.www.remote;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.CarBean;
import com.changsukuaidi.www.bean.TokenBean;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Describe
 * @Author thc
 * @Date 2017/12/6
 */

public interface LoginClient {
    @FormUrlEncoded
    @POST("User/login")
    Observable<BaseJson<TokenBean>> login(@Query("type") int type,
                                          @Query("msn") String msn,
                                          @Field("username") String account,
                                          @Field("password") String pwd);

    @POST("User/doResetPwd")
    @FormUrlEncoded
    Observable<BaseJson<String>> resetPassword(@Field("username") String username,
                                               @Field("password") String password);

    @GET("User/getRandCode")
    Observable<BaseJson<String>> getVerifyCode(@Query("type") String type,
                                             @Query("mobile") String mobile);

    @GET("User/checkRandCode")
    Observable<BaseJson<String>> checkVerifyCode(@Query("type") String type,
                                                 @Query("mobile") String mobile,
                                                 @Query("code") String code);

    @GET("Driver/Api/signIn")
    Observable<BaseJson<CarBean>> getCarInfo();
}
