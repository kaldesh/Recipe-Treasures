package com.example.recipetreasures

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var btnNext: MaterialButton
    private lateinit var btnSkip: MaterialButton
    private lateinit var sharedPreferences: SharedPreferences

    private val PREFS_NAME = "UserSession"
    private val KEY_ONBOARDING = "onboarding_finished"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Check if onboarding is already finished
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        if (sharedPreferences.getBoolean(KEY_ONBOARDING, false)) {
            navigateToAuth()
            return
        }

        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter

        // Attach TabLayout to ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        // Set colors for indicators
        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.green))
        tabLayout.setTabTextColors(getColor(R.color.gray), getColor(R.color.green))

        btnSkip.setOnClickListener {
            finishOnboarding()
        }

        btnNext.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            } else {
                finishOnboarding()
            }
        }
    }

    private fun finishOnboarding() {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING, true).apply()
        navigateToAuth()
    }

    private fun navigateToAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
