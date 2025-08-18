package com.example.recipetreasures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.recipetreasures.data.db.AppDatabase
import com.example.recipetreasures.data.model.User
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button

    private val PREFS_NAME = "UserSession"
    private val KEY_EMAIL = "current_user_email"

    private val userDao by lazy {
        AppDatabase.getDatabase(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginBtn = findViewById(R.id.loginBtn)
        signupBtn = findViewById(R.id.signupBtn)

        loginBtn.setOnClickListener { loginUser() }
        signupBtn.setOnClickListener { signupUser() }
    }

    private fun signupUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                runOnUiThread {
                    Toast.makeText(this@AuthActivity, "Email already exists!", Toast.LENGTH_SHORT).show()
                }
            } else {
                userDao.insertUser(User(email = email, password = password))
                runOnUiThread {
                    Toast.makeText(this@AuthActivity, "Account created!", Toast.LENGTH_SHORT).show()
                    // Save the session after successful signup
                    val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    prefs.edit().putString(KEY_EMAIL, email).apply()


                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun loginUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        lifecycleScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                runOnUiThread {

                    val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    prefs.edit().putString(KEY_EMAIL, email).apply()

                    Toast.makeText(this@AuthActivity, "Welcome!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@AuthActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}