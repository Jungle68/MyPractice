package com.thc.www.p2p.android.login

import android.content.Intent
import com.thc.www.p2p.android.main.MainActivity
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
class LoginPresenter @Inject constructor(repository: LoginRepository, view: LoginFragment)
    : BasePresenter<LoginRepository, LoginFragment>(repository, view) {

    fun login(account: String, pwd: String) {
        addSubscribe(Flowable.just(mRepository)
                .map {
                    mRepository.login(account, pwd)
                }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it) {
                        mRootView.showFailedMessage("登录失败")
                    } else {
                        mRootView.showSuccessMessage("登录成功")
                        mRootView.activity?.startActivity(Intent(mRootView.activity, MainActivity::class.java))
                        mRootView.finishActivity()
                    }
                }, {

                }))
    }
}