package com.thc.www.myinfo

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.nightonke.wowoviewpager.WoWoViewPager
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter
import com.thc.www.myinfo.utils.DisplayUtil

abstract class MyBaseActivity : AppCompatActivity() {
    protected var animationAdded = false
    protected var screenW: Int = 0
    protected var screenH: Int = 0
    protected lateinit var wowo: WoWoViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewRes())
        wowo = findViewById(R.id.wowo_viewpager)
        wowo.adapter = WoWoViewPagerAdapter.builder()
                .fragmentManager(supportFragmentManager)
                .count(fragmentNumber())                        // Fragment Count
                .colorsRes(*fragmentColorsRes())                // Colors of fragments
                .build()
        screenW = DisplayUtil.getScreenWidth(this)
        screenH = DisplayUtil.getScreenHeight(this)
    }

    protected abstract fun contentViewRes(): Int

    protected abstract fun fragmentNumber(): Int

    protected open fun fragmentColorsRes(): Array<Int> {
        return arrayOf(R.color.blue_1, R.color.blue_2, R.color.blue_3, R.color.blue_4, R.color.blue_5)
    }

    protected fun color(colorRes: Int): Int {
        return ContextCompat.getColor(this, colorRes)
    }
}
