package com.example.shalldo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private lateinit var welcomeImg: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        welcomeImg = this.findViewById(R.id.welcome_img) as ImageView;
        val anima = AlphaAnimation(0.3f, 1.0f)
        anima.duration = 1500
        welcomeImg.startAnimation(anima)
        anima.setAnimationListener(AnimationImpl())
    }

    private inner class AnimationImpl : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            welcomeImg.setBackgroundResource(R.drawable.ic_launcher_background)
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            skip()
        }
    }

    private fun skip(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}