package com.sol.jph.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.sol.jph.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ToastService : LifecycleService() {
    companion object {
        val isServiceStarted = MutableLiveData<Boolean>()
        val toastMessage = MutableLiveData<Boolean>()
    }

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        App.instance.appComponent.inject(this)

        initValues()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> {
                    startForegroundService()
                }
                ACTION_STOP_SERVICE -> {
                    stopService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initValues() {
        isServiceStarted.postValue(false)
        toastMessage.postValue(false)
    }

    private fun startForegroundService() {
        isServiceStarted.postValue(true)
        startTimer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopService() {
        initValues()
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )

        notificationManager.createNotificationChannel(channel)
    }

    private fun startTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            while (isServiceStarted.value!!) {
                toastMessage.postValue(true)

                delay(1000 * 60 * 5)

                toastMessage.postValue(false)
            }
        }
    }
}