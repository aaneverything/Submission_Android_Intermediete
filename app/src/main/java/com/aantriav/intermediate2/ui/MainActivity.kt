package com.aantriav.intermediate2.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.aantriav.intermediate2.R
import com.aantriav.intermediate2.data.pref.UserPreferences
import com.aantriav.intermediate2.databinding.ActivityMainBinding
import com.aantriav.intermediate2.ui.screen.HomeActivity
import com.aantriav.intermediate2.ui.screen.LoginActivity
import com.aantriav.intermediate2.ui.screen.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreferences = UserPreferences(this)

        if (!userPreferences.getToken().isNullOrEmpty()) {
            Intent(this, HomeActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }

        setupUI()
        playAnimation()
    }

    private fun setupUI() {
        binding.registerButton.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.loginButton.setOnClickListener {
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewLogo, View.TRANSLATION_X, -25f, 25f).apply {
            duration = 1500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}