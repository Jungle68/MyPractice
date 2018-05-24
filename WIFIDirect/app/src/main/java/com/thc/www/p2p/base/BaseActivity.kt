package com.thc.www.p2p.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.thc.www.wifi_direct.R

/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
abstract class BaseActivity<out F : BaseFragment<*>> : AppCompatActivity() {
    abstract fun getFragment(): F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        supportFragmentManager.beginTransaction().add(R.id.fl_container, getFragment()).commit()
    }
}