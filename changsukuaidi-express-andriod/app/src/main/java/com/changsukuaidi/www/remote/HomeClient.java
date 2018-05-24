package com.changsukuaidi.www.remote;

import com.changsukuaidi.www.bean.BaseJson;
import com.changsukuaidi.www.bean.UserBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/2/12
 * @Contact 605626708@qq.com
 */

public interface HomeClient {
    @GET("Member/detail")
    Observable<BaseJson<UserBean>> getUserInfo();
}
