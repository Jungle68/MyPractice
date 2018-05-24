package com.thc.www.p2p.base

import io.reactivex.FlowableSubscriber
import org.reactivestreams.Subscription

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
abstract class BaseSubscriber<T> : FlowableSubscriber<T> {
    override fun onComplete() {
    }

    abstract override fun onSubscribe(s: Subscription)

    abstract override fun onNext(t: T)

    override fun onError(t: Throwable?) {
    }
}