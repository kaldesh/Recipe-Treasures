package com.example.recipetreasures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipetreasures.ui.HomeFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val PREFS_NAME = "UserSession"
    private val KEY_EMAIL = "current_user_email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)


        bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> navController.popBackStack(R.id.homeFragment, false)
                R.id.favouritesFragment -> navController.popBackStack(R.id.favouritesFragment, false)
            }
        }

        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.favouritesFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)


        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getString(KEY_EMAIL, null) == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            val email = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_EMAIL, "") ?: ""
            val titleText = "Welcome, $email"

            // Create a spannable string
            val spannable = SpannableString(titleText)

            // Apply color span to the whole text (or part of it if you want)
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)),
                0, // start index
                spannable.length, // end index
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            supportActionBar?.title = spannable
            when(destination.id) {
                R.id.detailsFragment -> bottomNav.visibility = View.GONE
                else -> bottomNav.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_menu, menu)
        val searchItem = menu?.findItem(R.id.searchFragment)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.queryHint = "Search recipes..."

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.takeIf { it.isNotBlank() }?.let {
                    val navController = findNavController(R.id.nav_host_fragment)
                    val bundle = Bundle().apply { putString("query", it) }
                    navController.navigate(R.id.searchFragment, bundle)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?) = false
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return when (item.itemId) {

            R.id.menu_sign_out -> {
                getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply()
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                true
            }
            R.id.menu_about_creator -> {
                navController.navigate(R.id.aboutUs2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}