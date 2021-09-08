package com.sol.jph

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.sol.jph.di.component.AppComponent
import com.sol.jph.di.component.DaggerAppComponent
import com.sol.jph.di.module.RoomModule
import com.sol.jph.di.module.ServiceModule
import com.sol.jph.ui.service.ACTION_START_SERVICE
import com.sol.jph.ui.service.ToastService

class App : Application() , LifecycleObserver {
    companion object {
        lateinit var instance: App
    }
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent
            .builder()
            .serviceModule(ServiceModule(this))
            .roomModule(RoomModule(this))
            .build()

        startToastService()

        observeToastService()
    }

    private fun observeToastService(){
        ToastService.toastMessage.observe(ProcessLifecycleOwner.get(), {
            if(it!!){
                Toast.makeText(applicationContext, "Hello from Toast Service",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startToastService() {
        applicationContext.startService(Intent(applicationContext, ToastService::class.java).apply {
            this.action = ACTION_START_SERVICE
        })
    }
}