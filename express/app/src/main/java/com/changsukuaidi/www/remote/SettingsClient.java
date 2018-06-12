package com.changsukuaidi.www.remote;

import com.changsukuaidi.www.bean.BaseJson;
import com.google.gson.JsonObject;

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

public interface SettingsClient {
    @GET("Member/checkOldpwd")
    Observable<BaseJson<String>> checkOldPwd(@Query("password") String password);

    @POST("Member/changepwd")
    @FormUrlEncoded
    Observable<BaseJson<String>> modifyPwd(@Field("password") String password);

    /**
     * 上传图片
     *
     * @param base64Bitmap
     * @return
     */
    @FormUrlEncoded
    @POST("File/upPic")
    Observable<BaseJson<JsonObject>> uploadImage(@Field("cover_video_img") String base64Bitmap);

    /**
     * 设置用户头像
     *
     * @param avatar_id 上传图片返回的Id
     * @return 设置状态
     */
    @GET("Member/editProfile")
    Observable<BaseJson<JsonObject>> setUserHeadIcon(@Query("avatar_id") String avatar_id);
}
