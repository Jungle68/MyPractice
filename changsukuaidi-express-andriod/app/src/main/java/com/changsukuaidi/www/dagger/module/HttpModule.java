package com.changsukuaidi.www.dagger.module;

import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.changsukuaidi.www.base.BaseActivity;
import com.changsukuaidi.www.base.BaseApplication;
import com.changsukuaidi.www.modules.login.LoginActivity;
import com.changsukuaidi.www.net.RequestIntercept;
import com.changsukuaidi.www.net.listener.RequestInterceptListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2017/12/6
 * @Contact 605626708@qq.com
 */
@Module
public class HttpModule {
    private static final String BASE_URL = "http://csd.jipushop.com/";
    private static final int CONNECTED_TOME_OUT = 15;
    private static final int WRITE_TOME_OUT = 15;
    private static final int READE_TOME_OUT = 15;
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;// 缓存文件最大值为 10Mb

    @Provides
    static Retrofit provideRetrofit(Cache cache, RequestIntercept intercept) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(CONNECTED_TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(READE_TOME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TOME_OUT, TimeUnit.SECONDS)
                .cache(cache)//设置缓存
                .addNetworkInterceptor(intercept)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    Cache provideCache(File file) {
        return new Cache(file, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
    }

    @Provides
    File provideCacheFile(BaseApplication context) {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && isExternalCache) {
//            File file = null;
//            file = context.getExternalCacheDir();//获取系统管理的sd卡缓存文件
//            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
//                file = new File(getCacheFilePath(context));
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//            }
//            return file;
//        } else {
        return context.getCacheDir();// 获取app缓存目录，glide就是默认缓存在这儿的
//        }
    }

    @Provides
    RequestIntercept provideIntercept(BaseApplication application) {
        return new RequestIntercept(new RequestInterceptListener() {
            @Override
            public Response onHttpResponse(String httpResult, Interceptor.Chain chain, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(httpResult);
                    // 被挤下线了
                    if (jsonObject.has("status") && jsonObject.optInt("status") == -5) {
                        BaseActivity.getLastActivity().runOnUiThread(() -> new AlertDialog.Builder(BaseActivity.getLastActivity())
                                .setTitle("已在其他客户端登录请重新登录.")
                                .setPositiveButton("确定", (dialog1, which) -> {
                                    // 清空token
                                    application.clearTokenAndUser();
                                    // 跳转登录
                                    Intent intent = new Intent(BaseActivity.getLastActivity(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    BaseActivity.getLastActivity().startActivity(intent);
                                    dialog1.dismiss();
                                })
                                .setCancelable(false)
                                .create()
                                .show());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的 request 如增加 header,不做操作则返回 request
                if (application.getToken() == null) {
                    HttpUrl originalHttpUrl = request.url();
                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("device_code", "hEQeri57-NYvrvDSXPR2Fp365Qot87r2xTUWG6gV9SQ")
                            .build();
                    return chain.request().newBuilder()
                            .url(url)
                            .header("Accept", "application/json")
                            .build();
                } else {
                    HttpUrl originalHttpUrl = request.url();
                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("device_code", application.getToken().getDevice_code())
                            .build();
                    return chain.request().newBuilder()
                            .url(url)
                            .header("Accept", "application/json")
                            .build();
                }
            }
        });
    }
}
