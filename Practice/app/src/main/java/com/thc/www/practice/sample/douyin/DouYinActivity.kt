package com.thc.www.practice.sample.douyin

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.VideoView
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_sample_douyin.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/6/5
 */
class DouYinActivity : AppCompatActivity() {
    private val imgs = intArrayOf(R.mipmap.img_video_1, R.mipmap.img_video_2)
    private val videos = intArrayOf(R.raw.video_1, R.raw.video_2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        setFlymeStatusBarLightMode(window, false)
        setContentView(R.layout.activity_sample_douyin)

        rv_dou_yin.run {
            layoutManager = PageLayoutManger(this@DouYinActivity).apply {
                setOnViewPagerListener(object : OnViewPagerListener {
                    override fun onPageRelease(isNext: Boolean, position: Int) {
                        releaseVideo(if (isNext) 0 else 1)
                    }

                    override fun onPageSelected(position: Int, isBottom: Boolean) {
                        playVideo(0)
                    }

                    override fun onLayoutComplete() {
                        playVideo(0)
                    }
                })
            }
            adapter = object : RecyclerView.Adapter<ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                        ViewHolder(layoutInflater.inflate(R.layout.item_douyin_rv, parent, false))

                override fun getItemCount() = 20

                override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)
            }
        }
    }

    private fun playVideo(position: Int) {
        val itemView = rv_dou_yin.getChildAt(position)
        val holder = rv_dou_yin.getChildViewHolder(itemView) as ViewHolder

        val mediaPlayer = arrayOfNulls<MediaPlayer>(1)
        holder.videoView.start()
        holder.videoView.setOnInfoListener({ mp, _, _ ->
            mediaPlayer[0] = mp
            mp.isLooping = true
            holder.imgThumb.animate().alpha(0f).setDuration(200).start()
            false
        })
        holder.videoView.setOnPreparedListener({ })


        holder.imgPlay.setOnClickListener(object : View.OnClickListener {
            internal var isPlaying = true
            override fun onClick(v: View) {
                isPlaying = if (holder.videoView.isPlaying) {
                    holder.imgPlay.animate().alpha(1f).start()
                    holder.videoView.pause()
                    false
                } else {
                    holder.imgPlay.animate().alpha(0f).start()
                    holder.videoView.start()
                    true
                }
            }
        })
    }

    private fun releaseVideo(index: Int) {
        val itemView = rv_dou_yin.getChildAt(index)
        val holder = rv_dou_yin.getChildViewHolder(itemView) as ViewHolder
        holder.videoView.stopPlayback()
        holder.imgThumb.animate().alpha(1f).start()
        holder.imgPlay.animate().alpha(0f).start()
    }

    /**
     * 魅族设置黑白状态栏文字
     */
    private fun setFlymeStatusBarLightMode(window: Window?, dark: Boolean = true): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgThumb: ImageView = itemView.findViewById(R.id.img_thumb)
        var videoView: VideoView = itemView.findViewById(R.id.video_view)
        var imgPlay: ImageView = itemView.findViewById(R.id.img_play)
        var rootView: ConstraintLayout = itemView.findViewById(R.id.root_view)

        fun bindView(position: Int) {
            imgThumb.setImageResource(imgs[position % 2])
            videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + videos[position % 2]))
        }
    }
}