package com.example.cookpad.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookpad.R
import kotlinx.android.synthetic.main.activity_start.*
import maes.tech.intentanim.CustomIntent.customType

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btnStart.setOnClickListener {
            val intent = Intent(this@StartActivity,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            customType(this@StartActivity,"fadein-to-fadeout");
        }

    }
}
