package com.thc.www.p2p.base

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.thc.www.p2p.utils.StatusBarUtils
import com.thc.www.p2p.widget.ToastUtils
import com.thc.www.wifi_direct.R
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


/**
 * @Describe
 * @Author thc
 * @Date  2018/4/20
 */
abstract class BaseFragment<P : IBasePresenter> : Fragment(), IBaseView {
    @Inject
    @JvmField
    var mPresenter: P? = null
    @Inject
    @JvmField
    var mApplication: BaseApplication? = null
    private var toolBar: View? = null // title栏

    /**
     * 重写该方法，判断是否需要添加title栏
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        if (useToolBar()) {
            return LinearLayout(activity).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                orientation = LinearLayout.VERTICAL
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    activity!!.window.statusBarColor = resources.getColor(R.color.colorTheme)
                toolBar = inflater.inflate(R.layout.toolbar_layout, container, false)
                addView(toolBar)
                addView(view, layoutParams)
            }
        } else {
            if (fullScreen()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity!!.window.statusBarColor = Color.TRANSPARENT
                    StatusBarUtils.setStatusTheme(activity!!.window, true)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity!!.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    activity!!.window.statusBarColor = Color.TRANSPARENT
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity!!.window.statusBarColor = resources.getColor(R.color.colorTheme)
                }
            }
        }
        return view
    }

    /**
     * 判断一个一个点的灰阶
     * 灰阶小于 < 142时认为应该使用白色状态栏文字
     */
    private fun isLightStatus(bitmap: Bitmap): Boolean {
        val color = bitmap.getPixel(bitmap.width * 2 / 3, 40)
        return (Color.red(color) * 0.299 + Color.green(color) * 0.587 + Color.blue(color) * 0.114) < 142
    }

    /**
     * 重写该方法做一些初始化操作
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPresenter()
        if (useEventBus())
            EventBus.getDefault().register(this)
        initView()
        initData()
    }

    /**
     * 重写该方法，销毁资源
     */
    override fun onDestroyView() {
        super.onDestroyView()
        if (useEventBus())
            EventBus.getDefault().unregister(this)
    }

    /**
     * 设置ToolBar的title
     */
    protected fun setToolbarTitle(title: String) {
        toolBar?.findViewById<TextView>(R.id.toolbar_title)?.text = title
    }

    /**
     * 设置左侧返回按钮可用
     */
    protected fun setToolbarLeftEnable() {
        toolBar?.findViewById<ImageView>(R.id.toolbar_left)?.run {
            visibility = View.VISIBLE
            setImageBitmap(BitmapFactory.decodeResource(resources, R.mipmap.icon_back))
            setOnClickListener({ activity!!.onBackPressed() })
        }
    }

    /**
     * 设置左侧返回按钮不可用
     */
    protected fun setToolbarLeftDisEnable() {
        toolBar?.findViewById<ImageView>(R.id.toolbar_left)?.visibility = View.INVISIBLE
    }

    /**
     * 设置右侧返回按钮不可用
     */
    protected fun setToolbarRightDisEnable() {
        toolBar?.findViewById<TextView>(R.id.toolbar_right)?.visibility = View.INVISIBLE
    }

    /**
     * 设置右侧返回按钮可用，根据传入的参数显示对应的东西
     */
    protected fun setToolbarRight(resId: Int = 0, text: String = "", color: Int = 0, listener: View.OnClickListener? = null) {
        toolBar?.findViewById<TextView>(R.id.toolbar_right)?.run {
            if (resId != 0)
                setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
            if (!TextUtils.isEmpty(text))
                this.text = text
            if (color != 0)
                setTextColor(color)
            if (listener != null)
                setOnClickListener(listener)
        }
    }

    protected fun setToolbarRightVisible(isVisible: Boolean) {
        toolBar?.findViewById<TextView>(R.id.toolbar_right)?.visibility =
                if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDestroy()
    }

    /**
     * 是否显示title栏
     */
    protected open fun useToolBar() = false

    /**
     * 是否全屏显示
     */
    protected open fun fullScreen() = false

    /**
     * 是否使用EventBus
     */
    protected open fun useEventBus() = false

    /**
     * 初始化View
     */
    protected open fun initView() {

    }

    /**
     * 初始化数据
     */
    protected open fun initData() {

    }

    /**
     * 展示加载成功的消息
     */
    override fun showSuccessMessage(message: String) {
        ToastUtils.showMessage(context, message)
    }

    /**
     * 展示加载失败的消息
     */
    override fun showFailedMessage(message: String) {
        ToastUtils.showMessage(context, message)
    }

    /**
     * 展示加载中视图
     */
    override fun showLoading(msg: String) {
    }

    /**
     * 展示加载中视图
     */
    override fun showLoading() {
    }

    /**
     * 隐藏加载中视图
     */
    override fun hideLoading() {
    }

    /**
     * 网络错误时调用
     */
    override fun onNetError() {
    }

    /**
     * 没有数据时显示空布局
     */
    override fun showEmpty() {
    }

    /**
     * 关闭activity
     */
    override fun finishActivity() {
        activity?.finish()
    }

    /**
     * 设置mPresenter
     */
    protected abstract fun setPresenter()

    /**
     * 设置布局layout
     */
    abstract fun getLayoutId(): Int
}