package com.thc.www.p2p.adapter

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/17
 */
class PagerViewAdapter(manager: FragmentManager?) : FragmentStatePagerAdapter(manager) {
    private lateinit var mFragments: ArrayList<Fragment>
    private var mTitles: Array<String>? = null

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size

    fun bindDatas(list: ArrayList<Fragment>, titles: Array<String>? = null) {
        mFragments = list
        mTitles = titles
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? = mTitles?.get(position)
            ?: super.getPageTitle(position)

    override fun saveState(): Parcelable? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        val view = fragment.view
        if (view != null) {
            container.addView(view)
        }
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        var view: View? = null
        if (position < mFragments.size) {
            view = mFragments[position].view
        }
        if (view != null) {
            container.removeView(view)
        }
    }
}