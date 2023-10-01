package com.example.taskflow.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.taskflow.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btn_sign_up_intro = findViewById<Button>(R.id.btn_sign_up_intro)
        val btn_sign_in_intro = findViewById<Button>(R.id.btn_sign_in_intro)

        btn_sign_in_intro.setOnClickListener{
            startActivity(Intent(this, SignInActivity::class.java))
        }

        btn_sign_up_intro.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }


}