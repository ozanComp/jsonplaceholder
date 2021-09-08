package com.sol.jph.ui.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sol.jph.R
import com.sol.jph.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_post, R.id.navigation_home
            )
        )

        navController = findNavController(R.id.nav_host_fragment_activity_detail)

        setupActionBarWithNavController(navController!!, appBarConfiguration)
        navView.setupWithNavController(navController!!)

        supportActionBar?.hide()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }
}