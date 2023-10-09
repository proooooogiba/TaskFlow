package com.example.taskflow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskflow.R
import com.example.taskflow.databinding.ActivityBaseBinding
import com.example.taskflow.databinding.ActivityCardDetailsBinding

class CardDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}