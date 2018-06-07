package com.thc.www.myinfo

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nightonke.wowoviewpager.WoWoViewPager
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter
import com.thc.www.myinfo.utils.DisplayUtil

abstract class MyBaseActivity : AppCompatActivity() {
    private lateinit var mToast: Toast
    private var animationAdded = false
    protected var screenW: Int = 0
    protected var screenH: Int = 0
    protected lateinit var wowo: WoWoViewPager

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        addAnimations()
    }

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
        mToast = Toast.makeText(this@MyBaseActivity, "", Toast.LENGTH_SHORT)

        wowo.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mToast.setText(position.toString())
                mToast.show()
            }
        })
    }

    protected abstract fun contentViewRes(): Int

    protected abstract fun fragmentNumber(): Int

    protected open fun addAnimations() {
        if (animationAdded) return
        animationAdded = true
    }

    protected open fun fragmentColorsRes(): Array<Int> {
        return arrayOf(R.color.blue_1, R.color.blue_2, R.color.blue_3, R.color.blue_4, R.color.blue_5)
    }

    protected fun color(colorRes: Int): Int {
        return ContextCompat.getColor(this, colorRes)
    }
}
