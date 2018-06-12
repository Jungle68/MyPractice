package com.thc.www.practice.android_21.transition

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import com.thc.www.practice.R
import kotlinx.android.synthetic.main.activity_android_21_transition_constrain_set_one.*

/**
 * @Describe
 * @Author thc
 * @Date  2018/6/11
 */
class ConstraintSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val constraintSetTwo = ConstraintSet()
        constraintSetTwo.clone(this, R.layout.activity_android_21_transition_constrain_set_two)
        setContentView(R.layout.activity_android_21_transition_constrain_set_one)
        val constraintSetOne = ConstraintSet()
        constraintSetOne.clone(root)

        fab_sure.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            constraintSetOne.applyTo(root)
        }
        et_count.setOnClickListener {
            TransitionManager.beginDelayedTransition(root)
            constraintSetTwo.applyTo(root)
        }
    }
}