package com.thc.www.p2p.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.thc.www.wifi_direct.R


/**
 * @Describe 可全删除的EditText
 * @Author zhouhao
 * @Date 2017/8/18
 * @Contact 605626708@qq.com
 */

class DeleteEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatEditText(context, attrs), TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {
    private val gestureDetector: GestureDetector

    companion object {
        const val DEFAULT_DELETE_ICON = R.mipmap.icon_delete
        var DEFAULT_PADDING_RIGHT: Int = 0
    }

    init {
        DEFAULT_PADDING_RIGHT = resources.getDimension(R.dimen.margin_small).toInt()
        addTextChangedListener(this)
        setOnTouchListener(this)
        onFocusChangeListener = this
        val listener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                text.clear()
                return true
            }
        }
        gestureDetector = GestureDetector(context, listener)
        setPadding(paddingLeft, paddingTop, DEFAULT_PADDING_RIGHT, paddingBottom)
    }


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        if (TextUtils.isEmpty(s)) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, DEFAULT_DELETE_ICON, 0)
        }
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val drawableWidth = resources.getDrawable(DEFAULT_DELETE_ICON).intrinsicWidth
        val drawableHeight = resources.getDrawable(DEFAULT_DELETE_ICON).intrinsicHeight
        if (event.x >= v.width - DEFAULT_PADDING_RIGHT - drawableWidth
                && event.y >= v.height / 2 - drawableHeight
                && event.y <= v.height / 2 + drawableHeight
                && compoundDrawables[2] != null) {
            gestureDetector.onTouchEvent(event)
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChange(view: View, b: Boolean) {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, if (b && !TextUtils.isEmpty(text)) DEFAULT_DELETE_ICON else 0, 0)
    }
}
