package com.thc.www.p2p.android.launcher

import android.content.Intent
import com.thc.www.p2p.android.main.MainActivity
import com.thc.www.p2p.android.login.LoginActivity
import com.thc.www.p2p.base.BasePresenter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LauncherPresenter @Inject constructor(repository: LauncherRepository, view: LauncherFragment)
    : BasePresenter<LauncherRepository, LauncherFragment>(repository, view) {

    fun judeToPage() {
        addSubscribe(Flowable.just(mRepository)
                .map {
                    it.getLoginInfo()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.token.isEmpty()) {
                        mRootView.activity?.startActivity(Intent(mRootView.activity, LoginActivity::class.java))
                    } else {
                        mRootView.activity?.startActivity(Intent(mRootView.activity, MainActivity::class.java))
                    }
                    mRootView.finishActivity()
                }, {

                }))
    }
}