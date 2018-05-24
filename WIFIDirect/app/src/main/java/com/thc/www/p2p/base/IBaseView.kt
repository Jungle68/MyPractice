package com.thc.www.p2p.base

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
interface IBaseView {
    fun showSuccessMessage(message: String)

    fun showFailedMessage(message: String)

    fun showLoading(msg: String)

    fun showLoading()

    fun hideLoading()

    fun onNetError()

    fun showEmpty()

    fun finishActivity()
}