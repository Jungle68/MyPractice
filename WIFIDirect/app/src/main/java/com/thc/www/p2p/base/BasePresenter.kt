package com.thc.www.p2p.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
open class BasePresenter<R, V : IBaseView> @Inject constructor(open var mRepository: R, open var mRootView: V) : IBasePresenter {
    @Inject
    @JvmField
    var mApplication: BaseApplication? = null
    private var mCompositeSubscription: CompositeDisposable? = null

    fun addSubscribe(disposable: Disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeDisposable()
        }
        mCompositeSubscription?.add(disposable)
    }

    private fun unSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription!!.isDisposed) {
            mCompositeSubscription?.dispose()
        }
    }

    override fun onStart() {
    }

    override fun onDestroy() {
        unSubscribe()
    }
}