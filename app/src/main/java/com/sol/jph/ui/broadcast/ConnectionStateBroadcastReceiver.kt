package com.sol.jph.ui.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import com.sol.jph.model.NetworkAvailability

class ConnectionStateBroadcastReceiver(val context: Context) : LiveData<NetworkAvailability>() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: ConnectionStateBroadcastReceiver

        fun get(context: Context): ConnectionStateBroadcastReceiver {
            instance = if (Companion::instance.isInitialized){
                instance
            }else{
                ConnectionStateBroadcastReceiver(context)
            }
            return instance
        }
    }

    private val connectivityBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false)

            value = if (notConnected) {
                NetworkAvailability.DISCONNECTED
            } else {
                NetworkAvailability.CONNECTED
            }
        }
    }

    override fun onActive() {
        super.onActive()
        val broadcastIntent = IntentFilter()
        @Suppress("DEPRECATION")
        broadcastIntent.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectivityBroadcastReceiver, broadcastIntent)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(connectivityBroadcastReceiver)
    }
}