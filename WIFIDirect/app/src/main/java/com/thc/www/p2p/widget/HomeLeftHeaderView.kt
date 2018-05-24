package com.thc.www.p2p.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.FrameLayout
import com.thc.www.wifi_direct.R


/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

class HomeLeftHeaderView : FrameLayout {
    private val paint = Paint()
    private val path = Path()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        // 启用onDraw();
        setWillNotDraw(false)

        paint.run {
            style = Paint.Style.FILL_AND_STROKE
            color = resources.getColor(R.color.colorTheme)
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        path.apply {
            moveTo(0F, 0F)
            lineTo(width.toFloat(), 0F)
            lineTo(0f, height.toFloat())
            close()
        }
        canvas.drawPath(path, paint)
    }
}
