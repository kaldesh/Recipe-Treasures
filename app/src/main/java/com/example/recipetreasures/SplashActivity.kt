package com.example.recipetreasures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val PREFS_NAME = "UserSession"
    private val KEY_EMAIL = "current_user_email"
    private val KEY_ONBOARDING = "onboarding_finished"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val email = prefs.getString(KEY_EMAIL, null)
            val onboardingFinished = prefs.getBoolean(KEY_ONBOARDING, false)

            when {
                !onboardingFinished -> {
                    startActivity(Intent(this, OnboardingActivity::class.java))
                }
                email != null -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this, AuthActivity::class.java))
                }
            }
            finish()
        }, 3000) // 3 ثواني
    }
}
