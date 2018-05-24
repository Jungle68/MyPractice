package com.thc.www.practice.android_21.transition

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_android_21_transition_place_holder.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/5/11
 */
class PlaceHolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_21_transition_place_holder)
        save.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            template_action.setContentId(it.id)
        }

        cancel.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            template_action.setContentId(it.id)
        }
        edit.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            template_action.setContentId(it.id)
        }
        delete.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            template_action.setContentId(it.id)
        }

    }
}