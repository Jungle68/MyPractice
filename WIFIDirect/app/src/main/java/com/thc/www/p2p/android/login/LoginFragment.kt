package com.thc.www.p2p.android.login

import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent
import com.thc.www.p2p.base.BaseApplication
import com.thc.www.p2p.base.BaseFragment
import com.thc.www.p2p.comp.DaggerLoginComp
import com.thc.www.p2p.module.LoginModule
import com.thc.www.wifi_direct.R
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
class LoginFragment : BaseFragment<LoginPresenter>() {
    override fun setPresenter() {
        DaggerLoginComp.builder()
                .appComp((activity!!.application as BaseApplication).getAppComp())
                .loginModule(LoginModule(this))
                .build()
                .injectMembers(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun initView() {
        setToolbarTitle("登录")

        Observable.combineLatest(RxTextView.afterTextChangeEvents(det_login_account),
                RxTextView.afterTextChangeEvents(det_login_pwd),
                BiFunction<TextViewAfterTextChangeEvent, TextViewAfterTextChangeEvent, Boolean> { s1, s2 ->
                    s1.editable()!!.length >= 6 && s2.editable()!!.length >= 6
                }).subscribe({
            btn_login.isEnabled = it
        })

        btn_login.setOnClickListener {
            mPresenter?.login(det_login_account.text.toString(), det_login_pwd.text.toString())
        }
    }

    override fun useToolBar(): Boolean = true
}