package com.thc.www.practice.sample.loading

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.thc.www.practice.R
import com.thc.www.practice.utils.DisplayUtil
import java.util.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/28
 */
class LeafLoadingView(context: Context?,
                      attrs: AttributeSet?,
                      defStyleAttr: Int) :
        View(context, attrs, defStyleAttr) {
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?) : this(context, null)

    companion object {
        // 淡白色
        private const val WHITE_COLOR = -0x21c67
        // 橙色
        private const val ORANGE_COLOR = -0x5800
        // 中等振幅大小
        private const val MIDDLE_AMPLITUDE = 13
        // 不同类型之间的振幅差距
        private const val AMPLITUDE_DISPARITY = 5
        // 总进度
        private const val TOTAL_PROGRESS = 100
        // 叶子飘动一个周期所花的时间
        private const val LEAF_FLOAT_TIME: Long = 3000
        // 叶子旋转一周需要的时间
        private const val LEAF_ROTATE_TIME: Long = 2000

        // 用于控制绘制的进度条距离左／上／下的距离
        private const val LEFT_MARGIN = 9
        // 用于控制绘制的进度条距离右的距离
        private const val RIGHT_MARGIN = 25
        // 最大叶子数
        private const val MAX_LEAF = 8
    }

    private var mLeftMargin: Int = 0
    private var mRightMargin: Int = 0
    // 中等振幅大小
    private var mMiddleAmplitude = MIDDLE_AMPLITUDE
    // 振幅差
    private var mAmplitudeDisparity = AMPLITUDE_DISPARITY

    // 叶子飘动一个周期所花的时间
    private var mLeafFloatTime = LEAF_FLOAT_TIME
    // 叶子旋转一周需要的时间
    private var mLeafRotateTime = LEAF_ROTATE_TIME
    private val mResources: Resources by lazy {
        resources
    }
    private var mLeafBitmap: Bitmap? = null
    private var mLeafWidth: Int = 0
    private var mLeafHeight: Int = 0

    private var mOuterBitmap: Bitmap? = null
    private var mOuterSrcRect: Rect? = null
    private var mOuterDestRect: Rect? = null
    private var mOuterWidth: Int = 0
    private var mOuterHeight: Int = 0

    private var mTotalWidth: Int = 0
    private var mTotalHeight: Int = 0

    private var mBitmapPaint: Paint? = null
    private var mWhitePaint: Paint? = null
    private var mOrangePaint: Paint? = null
    private var mWhiteRectF: RectF? = null
    private var mOrangeRectF: RectF? = null
    private var mArcRectF: RectF? = null
    // 当前进度
    private var mProgress: Int = 0
    // 所绘制的进度条部分的宽度
    private var mProgressWidth: Int = 0
    // 当前所在的绘制的进度条的位置
    private var mCurrentProgressPosition: Int = 0
    // 弧形的半径
    private var mArcRadius: Int = 0

    // arc的右上角的x坐标，也是矩形x坐标的起始点
    private var mArcRightLocation: Int = 0
    // 用于产生叶子信息
    private var mLeafFactory: LeafFactory = LeafFactory()
    // 产生出的叶子信息
    private val mLeafInfos: List<Leaf> by lazy {
        mLeafFactory.generateLeafs()
    }
    // 用于控制随机增加的时间不抱团
    private var mAddTime: Int = 0

    init {
        mLeftMargin = DisplayUtil.dp2px(LEFT_MARGIN.toFloat(), context).toInt()
        mRightMargin = DisplayUtil.dp2px(RIGHT_MARGIN.toFloat(), context).toInt()

        mLeafFloatTime = LEAF_FLOAT_TIME
        mLeafRotateTime = LEAF_ROTATE_TIME

        initBitmap()
        initPaint()
        mLeafFactory = LeafFactory()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制进度条和叶子
        // 之所以把叶子放在进度条里绘制，主要是层级原因
        drawProgressAndLeafs(canvas)
        // drawLeafs(canvas);

        canvas.drawBitmap(mOuterBitmap, mOuterSrcRect, mOuterDestRect, mBitmapPaint)

        postInvalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTotalWidth = w
        mTotalHeight = h
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2

        mOuterSrcRect = Rect(0, 0, mOuterWidth, mOuterHeight)
        mOuterDestRect = Rect(0, 0, mTotalWidth, mTotalHeight)

        mWhiteRectF = RectF((mLeftMargin + mCurrentProgressPosition).toFloat(), mLeftMargin.toFloat(), (mTotalWidth - mRightMargin).toFloat(),
                (mTotalHeight - mLeftMargin).toFloat())
        mOrangeRectF = RectF((mLeftMargin + mArcRadius).toFloat(), mLeftMargin.toFloat(),
                mCurrentProgressPosition.toFloat(), (mTotalHeight - mLeftMargin).toFloat())

        mArcRectF = RectF(mLeftMargin.toFloat(), mLeftMargin.toFloat(), (mLeftMargin + 2 * mArcRadius).toFloat(),
                (mTotalHeight - mLeftMargin).toFloat())
        mArcRightLocation = mLeftMargin + mArcRadius
    }

    private fun drawProgressAndLeafs(canvas: Canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = TOTAL_PROGRESS
        }
        // mProgressWidth为进度条的宽度，根据当前进度算出进度条的位置
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS
        // 即当前位置在图中所示1范围内
        if (mCurrentProgressPosition < mArcRadius) {
            // 1.绘制白色ARC，绘制orange ARC
            // 2.绘制白色矩形

            // 1.绘制白色ARC
            canvas.drawArc(mArcRectF, 90f, 180f, false, mWhitePaint)

            // 2.绘制白色矩形
            mWhiteRectF?.left = mArcRightLocation.toFloat()
            canvas.drawRect(mWhiteRectF, mWhitePaint)

            // 绘制叶子
            drawLeafs(canvas)

            // 3.绘制棕色 ARC
            // 单边角度
            val angle = Math.toDegrees(Math.acos(((mArcRadius - mCurrentProgressPosition) / mArcRadius.toFloat()).toDouble())).toInt()
            // 起始的位置
            val startAngle = 180 - angle
            // 扫过的角度
            val sweepAngle = 2 * angle
            canvas.drawArc(mArcRectF, startAngle.toFloat(), sweepAngle.toFloat(), false, mOrangePaint)
        } else {
            // 1.绘制white RECT
            // 2.绘制Orange ARC
            // 3.绘制orange RECT
            // 这个层级进行绘制能让叶子感觉是融入棕色进度条中

            // 1.绘制white RECT
            mWhiteRectF?.left = mCurrentProgressPosition.toFloat()
            canvas.drawRect(mWhiteRectF, mWhitePaint)
            // 绘制叶子
            drawLeafs(canvas)
            // 2.绘制Orange ARC
            canvas.drawArc(mArcRectF, 90f, 180f, false, mOrangePaint)
            // 3.绘制orange RECT
            mOrangeRectF?.left = mArcRightLocation.toFloat()
            mOrangeRectF?.right = mCurrentProgressPosition.toFloat()
            canvas.drawRect(mOrangeRectF, mOrangePaint)

        }
    }

    /**
     * 绘制叶子
     *
     * @param canvas
     */
    private fun drawLeafs(canvas: Canvas) {
        mLeafRotateTime = if (mLeafRotateTime <= 0) LEAF_ROTATE_TIME else mLeafRotateTime
        val currentTime = System.currentTimeMillis()
        for (i in mLeafInfos.indices) {
            val leaf = mLeafInfos[i]
            if (currentTime > leaf.startTime && leaf.startTime != 0L) {
                // 绘制叶子－－根据叶子的类型和当前时间得出叶子的（x，y）
                getLeafLocation(leaf, currentTime)
                // 根据时间计算旋转角度
                canvas.save()
                // 通过Matrix控制叶子旋转
                val matrix = Matrix()
                val transX = mLeftMargin + leaf.x
                val transY = mLeftMargin + leaf.y
                matrix.postTranslate(transX, transY)
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                val rotateFraction = (currentTime - leaf.startTime) % mLeafRotateTime / mLeafRotateTime.toFloat()
                val angle = (rotateFraction * 360).toInt()
                // 根据叶子旋转方向确定叶子旋转角度
                val rotate = if (leaf.rotateDirection == 0)
                    angle + leaf.rotateAngle
                else
                    -angle + leaf.rotateAngle
                matrix.postRotate(rotate.toFloat(), transX + mLeafWidth / 2, transY + mLeafHeight / 2)
                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint)
                canvas.restore()
            }
        }
    }

    private fun getLeafLocation(leaf: Leaf, currentTime: Long) {
        val intervalTime = currentTime - leaf.startTime
        mLeafFloatTime = if (mLeafFloatTime <= 0) LEAF_FLOAT_TIME else mLeafFloatTime
        if (intervalTime < 0) {
            return
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis() + Random().nextInt(mLeafFloatTime.toInt())
        }

        val fraction = intervalTime.toFloat() / mLeafFloatTime
        leaf.x = (mProgressWidth - mProgressWidth * fraction).toInt().toFloat()
        leaf.y = getLocationY(leaf).toFloat()
    }

    // 通过叶子信息获取当前叶子的Y值
    private fun getLocationY(leaf: Leaf): Int {
        // y = A(wx+Q)+h
        val w = (2.toFloat() * Math.PI / mProgressWidth).toFloat()
        var a = mMiddleAmplitude.toFloat()
        when (leaf.startType) {
        // 小振幅 ＝ 中等振幅 － 振幅差
            Leaf.START_TYPE_LITTLE -> a = (mMiddleAmplitude - mAmplitudeDisparity).toFloat()
            Leaf.START_TYPE_MIDDLE -> a = mMiddleAmplitude.toFloat()
        // 小振幅 ＝ 中等振幅 + 振幅差
            Leaf.START_TYPE_BIG -> a = (mMiddleAmplitude + mAmplitudeDisparity).toFloat()
        }
        return (a * Math.sin((w * leaf.x).toDouble())).toInt() + mArcRadius * 2 / 3
    }

    private fun initBitmap() {
        mLeafBitmap = (mResources.getDrawable(R.mipmap.leaf) as BitmapDrawable).bitmap.apply {
            mLeafWidth = width
            mLeafHeight = height
        }

        mOuterBitmap = (mResources.getDrawable(R.mipmap.leaf_kuang) as BitmapDrawable).bitmap.apply {
            mOuterWidth = width
            mOuterHeight = height
        }
    }

    private fun initPaint() {
        mBitmapPaint = Paint().apply {
            isAntiAlias = true
            isDither = true
            isFilterBitmap = true
        }

        mWhitePaint = Paint().apply {
            isAntiAlias = true
            color = WHITE_COLOR
        }

        mOrangePaint = Paint().apply {
            isAntiAlias = true
            color = ORANGE_COLOR
        }
    }

    /**
     * 设置中等振幅
     *
     * @param amplitude
     */
    fun setMiddleAmplitude(amplitude: Int) {
        this.mMiddleAmplitude = amplitude
    }

    /**
     * 设置振幅差
     *
     * @param disparity
     */
    fun setMplitudeDisparity(disparity: Int) {
        this.mAmplitudeDisparity = disparity
    }

    /**
     * 获取中等振幅
     *
     */
    fun getMiddleAmplitude(): Int {
        return mMiddleAmplitude
    }

    /**
     * 获取振幅差
     *
     */
    fun getMplitudeDisparity(): Int {
        return mAmplitudeDisparity
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    fun setProgress(progress: Int) {
        this.mProgress = progress
        postInvalidate()
    }

    /**
     * 设置叶子飘完一个周期所花的时间
     *
     * @param time
     */
    fun setLeafFloatTime(time: Long) {
        this.mLeafFloatTime = time
    }

    /**
     * 设置叶子旋转一周所花的时间
     *
     * @param time
     */
    fun setLeafRotateTime(time: Long) {
        this.mLeafRotateTime = time
    }

    /**
     * 获取叶子飘完一个周期所花的时间
     */
    fun getLeafFloatTime(): Long {
        mLeafFloatTime = if (mLeafFloatTime == 0L) LEAF_FLOAT_TIME else mLeafFloatTime
        return mLeafFloatTime
    }

    /**
     * 获取叶子旋转一周所花的时间
     */
    fun getLeafRotateTime(): Long {
        mLeafRotateTime = if (mLeafRotateTime == 0L) LEAF_ROTATE_TIME else mLeafRotateTime
        return mLeafRotateTime
    }

    private class Leaf {
        companion object {
            const val START_TYPE_LITTLE = 0
            const val START_TYPE_MIDDLE = 1
            const val START_TYPE_BIG = 2
        }

        //  在绘制部分的位置
        var x: Float = 0F
        var y: Float = 0F
        // 控制叶子飘动的幅度
        var startType: Int = START_TYPE_BIG
        // 旋转角度
        var rotateAngle: Int = 0
        // 旋转方向：0、顺时针  1、逆时针
        var rotateDirection: Int = 0
        // 起始时间(ms)
        var startTime: Long = 0L
    }

    private inner class LeafFactory {
        // 生成一个叶子信息
        fun generateLeaf() = Leaf().apply {
            val random = Random()
            val randomType = random.nextInt(3)
            // 随机振幅
            startType = when (randomType) {
                1 -> Leaf.START_TYPE_LITTLE
                2 -> Leaf.START_TYPE_BIG
                else -> Leaf.START_TYPE_MIDDLE
            }
            // 随机起始的旋转角度
            rotateAngle = random.nextInt(360)
            // 随机旋转方向
            rotateDirection = random.nextInt(2)
            // 为了产生交错的感觉，让开始的时间有一定的随机性
            mLeafFloatTime = if (mLeafFloatTime <= 0) LEAF_FLOAT_TIME else mLeafFloatTime
            mAddTime += random.nextInt((mLeafFloatTime * 2).toInt())
            startTime = System.currentTimeMillis() + mAddTime
        }

        // 根据最大叶子数产生叶子信息
        fun generateLeafs() = generateLeafs(MAX_LEAF)

        // 根据传入的叶子数量产生叶子信息
        fun generateLeafs(leafSize: Int) = LinkedList<Leaf>().apply {
            for (i in 0 until leafSize) {
                add(generateLeaf())
            }
        }
    }
}
