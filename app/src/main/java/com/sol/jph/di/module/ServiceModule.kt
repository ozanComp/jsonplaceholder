package com.sol.jph.di.module

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sol.jph.R
import com.sol.jph.ui.service.NOTIFICATION_CHANNEL_ID
import com.sol.jph.ui.view.activity.SplashActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule(_application: Application) {
    private var application = _application
    private lateinit var pendingIntent: PendingIntent

    @Singleton
    @Provides
    fun providePendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            application,
            420,
            Intent(application, SplashActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @Singleton
    @Provides
    fun provideNotificationBuilder(pendingIntent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(application, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setContentTitle("Toast Lifecycle Service")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_home_24dp)
            .setContentIntent(pendingIntent)
    }


    @Singleton
    @Provides
    fun provideNotificationManager(): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}