package com.example.cookpad.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.example.cookpad.R
import com.bumptech.glide.Glide
import com.example.cookpad.until.PrefManager
import kotlinx.android.synthetic.main.activity_splash.*
import maes.tech.intentanim.CustomIntent.customType


class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 6000
    lateinit var prefManager : PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        prefManager = PrefManager(this);

        Glide.with(this).asGif().load(R.raw.gif_app).into(img_splashscreen);
        Handler().postDelayed({

            if (prefManager.user != null){
                val mainIntent = Intent(this@SplashActivity, HomeActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                customType(this@SplashActivity,"fadein-to-fadeout");
                this@SplashActivity.finish()
            }else {
                /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this@SplashActivity, StartActivity::class.java)
                this@SplashActivity.startActivity(mainIntent)
                this@SplashActivity.finish()
            }
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
