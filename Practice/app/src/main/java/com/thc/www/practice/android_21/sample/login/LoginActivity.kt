package com.thc.www.practice.android_21.sample.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.View
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_android_21_sample_login_login.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_21_sample_login_login)
        bt_go.setOnClickListener{
            val explode = Explode()
            explode.duration = 500

            window.exitTransition = explode
            window.enterTransition = explode
            val oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity)
            val i2 = Intent(this@LoginActivity, LoginSuccessActivity::class.java)
            startActivity(i2, oc2.toBundle())
        }
        fab.setOnClickListener {
            window.exitTransition = null
            window.enterTransition = null
            val options = ActivityOptions.makeSceneTransitionAnimation(this@LoginActivity, fab, fab.transitionName)
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java), options.toBundle())
        }
    }

    override fun onRestart() {
        super.onRestart()
        fab.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        fab.visibility = View.VISIBLE
    }
}