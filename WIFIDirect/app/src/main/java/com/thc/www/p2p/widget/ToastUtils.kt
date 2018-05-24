package com.thc.www.p2p.widget

import android.content.Context
import android.widget.Toast

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/7
 */
object ToastUtils {
    fun showMessage(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showMessageLong(context: Context?, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}