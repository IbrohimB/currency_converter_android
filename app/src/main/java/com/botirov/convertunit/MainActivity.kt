package com.botirov.convertunit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        tabLayout = findViewById(R.id.tabLayout)

        setupTabs()
    }

    private fun setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Length"))
        tabLayout.addTab(tabLayout.newTab().setText("Mass"))

        // Set tab selected listener
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        if (navController.currentDestination?.id == R.id.massFragment) {
                            navController.navigate(R.id.action_massFragment_to_lengthFragment)
                        }
                    }
                    1 -> {
                        if (navController.currentDestination?.id == R.id.lengthFragment) {
                            navController.navigate(R.id.action_lengthFragment_to_massFragment)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}