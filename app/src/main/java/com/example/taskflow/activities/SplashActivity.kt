package com.example.taskflow.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.example.taskflow.R
import com.example.taskflow.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val typeFace: Typeface = Typeface.createFromAsset(assets, "carbon bl.otf")
        val tv_app_name = findViewById<TextView>(R.id.tv_app_name)
        tv_app_name.typeface = typeFace

        Handler().postDelayed({

            var currentUserID = FirestoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2500)
    }
}