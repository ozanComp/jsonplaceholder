package com.sol.jph.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sol.jph.App
import com.sol.jph.databinding.ActivitySplashBinding
import com.sol.jph.di.factory.ViewModelFactory
import com.sol.jph.ui.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: SplashViewModel

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.instance.appComponent.inject(this)

        if(intent.getBooleanExtra("EXIT", false)){
            finish()
        }

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)

        observe()

        getLoggedUser()
    }

    private fun observe(){
        viewModel.navigateToLogin.observe(this, {
            if(it){
                viewModel.doneNavigatetoLogin()

                val background = object : Thread() {
                    override fun run() {
                        try {
                            sleep(5000)
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                }
                background.start()
            }
        })

        viewModel.navigateToDetail.observe(this, {
            if(it){
                viewModel.doneNavigateToDetail()

                val background = object : Thread() {
                    override fun run() {
                        try {
                            sleep(5000)
                            val intent = Intent(applicationContext, DetailActivity::class.java)
                            startActivity(intent)
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                }
                background.start()
            }
        })
    }

    private fun getLoggedUser(){
        viewModel.getLoggedUser()
    }
}