package com.net.learning.kotlinnewspluginlib

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger

/**
 * PluginMessengerService
 * Created by jason on 25/7/18.
 */
abstract class PluginMessengerService : Service(){
    lateinit var messenger: Messenger

    override fun onCreate() {
        super.onCreate()
        setIpcHandlerForMessenger()
    }

    abstract fun setIpcHandlerForMessenger()

    override fun onBind(intent: Intent): IBinder? {
        return messenger.binder
    }
}