package com.sol.jph.ui.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.sol.jph.App
import com.sol.jph.R
import com.sol.jph.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.instance.appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        setupActionBarWithNavController(navController!!)

        supportActionBar?.hide()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController!!.navigateUp() || super.onSupportNavigateUp()
    }
}