package com.example.recipetreasures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val PREFS_NAME = "UserPrefs"
    private val KEY_EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userEmail = prefs.getString(KEY_EMAIL, "Guest")

        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val logoutBtn: Button = findViewById(R.id.logoutBtn)

        welcomeText.text = "Welcome, $userEmail!"

        logoutBtn.setOnClickListener {
            // Clear saved login data
            prefs.edit().clear().apply()

            // Go back to login page
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
