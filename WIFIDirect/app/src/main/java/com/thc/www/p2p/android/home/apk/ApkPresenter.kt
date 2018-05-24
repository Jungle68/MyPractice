package com.thc.www.p2p.android.home.apk

import android.content.pm.ApplicationInfo
import android.graphics.drawable.BitmapDrawable
import com.thc.www.p2p.base.BasePresenter
import com.thc.www.p2p.bean.AppInfoBean
import com.thc.www.p2p.utils.FileUtils
import com.thc.www.wifi_direct.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class ApkPresenter @Inject constructor(repository: ApkRepository, view: ApkFragment)
    : BasePresenter<ApkRepository, ApkFragment>(repository, view) {
    fun loadAppInfo() {
        val appInfos = arrayListOf<AppInfoBean>()
        Flowable.fromIterable(mApplication?.packageManager?.getInstalledPackages(0))
                .filter({
                    it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //非系统应用
                    val appName = it.applicationInfo.loadLabel(mApplication?.packageManager).toString()
                    val packageName = it.packageName
                    val versionName = it.versionName
                    val versionCode = it.versionCode
                    val bd = it.applicationInfo.loadIcon(mApplication?.packageManager) as BitmapDrawable
                    val appIcon = bd.bitmap
                    val sourceDir = it.applicationInfo.sourceDir
                    val appInfo = AppInfoBean(appName,
                            packageName,
                            versionName,
                            versionCode.toString(),
                            sourceDir,
                            "data:image/png;base64," +FileUtils.bitmap2StrByBase64WithColor(appIcon, mApplication!!.resources.getColor(R.color.colorHomePageAppRecyclerBg)))
                    appInfos.add(appInfo)
                }, {

                }, {
                    mRootView.finishLoadAppInfo(appInfos)
                })
    }
}