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

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userEmail = prefs.getString(KEY_EMAIL, null)


        if (userEmail == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val logoutBtn: Button = findViewById(R.id.logoutBtn)

        welcomeText.text = "Welcome, $userEmail!"

        logoutBtn.setOnClickListener {
            prefs.edit().clear().apply()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
