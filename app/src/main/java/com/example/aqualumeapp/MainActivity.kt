package com.example.aqualumeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.aqualumeapp.fragment.HydrationHistoryFragment
import com.example.aqualumeapp.fragments.CalendarFragment
import com.example.aqualumeapp.fragments.HomeFragment
import com.example.aqualumeapp.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_with_bottom_nav)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(HydrationHistoryFragment())
        }

        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    // Already on home, do nothing
                    true
                }
                R.id.addFragment -> {
                    startActivity(Intent(this, HomeFragment::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.calendarFragment -> {
                    startActivity(Intent(this, CalendarFragment::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.profileFragment -> {
                    startActivity(Intent(this, ProfileFragment::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun showAddWaterDialog() {
        // Implement add water dialog/bottom sheet
        // This keeps the bottom nav visible while showing add water UI
    }

    // Method to show/hide bottom navigation from fragments
    fun setBottomNavigationVisibility(visible: Boolean) {
        bottomNavigationView.visibility = if (visible) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }
}