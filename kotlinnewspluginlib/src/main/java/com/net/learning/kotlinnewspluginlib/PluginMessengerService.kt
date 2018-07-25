package com.net.learning.kotlinnewspluginlib

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger

/**
 * PluginMessengerService: Abstract Service to handle IPC
 * Created by jason on 25/7/18.
 *
 * Just need to set the PluginIpcHandler for the message in [setIpcHandlerForMessenger]
 */
abstract class PluginMessengerService : Service(){
    lateinit var messenger: Messenger

    override fun onCreate() {
        super.onCreate()
        setIpcHandlerForMessenger()
    }

    /**
     * Set the Handler for the messenger. It should extend PluginIpcHandler
     *
     * E.g.
     * ```
     * val handler = YourPluginIpcHandler(applicationContext, YourNewsManager())
     * messenger = Messenger(handler)
     * ```
     */
    abstract fun setIpcHandlerForMessenger()

    override fun onBind(intent: Intent): IBinder? {
        return messenger.binder
    }
}