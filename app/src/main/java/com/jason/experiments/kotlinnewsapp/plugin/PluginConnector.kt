package com.jason.experiments.kotlinnewsapp.plugin

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import com.jason.experiments.kotlinnewsapp.model.NewsResult


/**
 * PluginConnector
 * Created by jason on 23/7/18.
 */
class PluginConnector(val context: Context, private val targetPackage: String, val callback: PluginConnectorCallbackListener) {

    companion object {
        private const val LOG_TAG = "+_PlgCon"
        const val MSG_GET_METADATA = 1
        const val MSG_GET_CATEGORIES = 2
        const val MSG_GET_NEWS = 3

        const val KEY_NEWS_SRC = "key_news_src"
        const val KEY_NEWS_LOGO = "key_news_logo"
        const val KEY_NEWS_CATEGORIES = "key_news_categories"
        const val KEY_NEWS_LIST = "key_news_list"
        const val KEY_CATEGORY = "key_category"

        const val TIMEOUT = 500L
        const val NEWS_TIMEOUT = 35000L
    }

    private var isBound = false
    private var handlerThread = HandlerThread("IPCHandlerThread")

    private val handler: IncomingHandler
    private var inMessenger: Messenger? = null
    private var outMessenger: Messenger? = null
    private var hasFinished = false


    init {
        handlerThread.start()
        handler = IncomingHandler(handlerThread.looper)
        inMessenger = Messenger(handler)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            outMessenger = Messenger(service)
            isBound = true
            callback.serviceConnected()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            outMessenger = null
            isBound = false
            Log.d("+_", "Service disconnected")
        }
    }

    fun bindService(): Boolean {
        val intent = Intent(PluginManager.ACTION_GET_PLUGIN)
        intent.setPackage(targetPackage)
        return context.applicationContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        if (isBound) {
            context.applicationContext.unbindService(serviceConnection)
            isBound = false
            Log.d("+_", "Service unbound")
        }
    }

    fun getMetaData() {
        val msg = Message.obtain(null, MSG_GET_METADATA, 0, 0)
        msg.replyTo = inMessenger
        try {
            outMessenger?.send(msg)
        } catch (re: RemoteException) {
            re.printStackTrace()
        }
        Handler().postDelayed({
                                  if (!hasFinished) {
                                      callback.timeoutOccurred()
                                  }
                              }
                              , TIMEOUT)
    }

    fun getCategories() {
        val msg = Message.obtain(null, MSG_GET_CATEGORIES, 0, 0)
        msg.replyTo = inMessenger
        try {
            outMessenger?.send(msg)
        } catch (re: RemoteException) {
            re.printStackTrace()
        }
        Handler().postDelayed({
                                  if (!hasFinished) {
                                      callback.timeoutOccurred()
                                  }
                              }
                              , TIMEOUT)
    }

    fun getNews(category: String) {
        val msg = Message.obtain(null, MSG_GET_NEWS, 0, 0)
        msg.data.putString(KEY_CATEGORY, category)
        msg.replyTo = inMessenger
        try {
            outMessenger?.send(msg)
        } catch (re: RemoteException) {
            re.printStackTrace()
        }
        Handler().postDelayed({
                                  if (!hasFinished) {
                                      callback.timeoutOccurred()
                                  }
                              }
                              , NEWS_TIMEOUT)
    }


    inner class IncomingHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when(msg.what){
                PluginConnector.MSG_GET_METADATA -> {
                    val data = msg.data
                    val pluginName = data.getString(PluginConnector.KEY_NEWS_SRC)
                    val pluginLogoBytes = data.getByteArray(PluginConnector.KEY_NEWS_LOGO)
                    val pluginLogo = BitmapFactory.decodeByteArray(pluginLogoBytes, 0, pluginLogoBytes.size)

                    callback.pluginMetaDataRetrieved(pluginName, pluginLogo, targetPackage)
                    hasFinished = true
                }
                PluginConnector.MSG_GET_CATEGORIES -> {
                    val data = msg.data
                    val categories = data.getStringArrayList(PluginConnector.KEY_NEWS_CATEGORIES)
                    callback.pluginCategoriesRetrieved(categories)
                    hasFinished = true
                }
                PluginConnector.MSG_GET_NEWS -> {
                    val data = msg.data
                    val newsResults = ArrayList<NewsResult>()
                    try{
                        val newsResultsStringArray = data.getSerializable(PluginConnector.KEY_NEWS_LIST) as ArrayList<Array<String>>
                        for(array in newsResultsStringArray){
                            if(array.size >= 7){
                                newsResults.add(NewsResult(array[0],
                                                           array[1],
                                                           array[2],
                                                           array[3],
                                                           array[4],
                                                           array[5],
                                                           array[6]))
                            }
                        }
                    } catch (cce: ClassCastException) {
                        Log.d(LOG_TAG, cce.message, cce)
                    }
                    callback.pluginNewsRetrieved(newsResults)
                    hasFinished = true
                }
                else -> {

                }
            }
        }
    }

    interface PluginConnectorCallbackListener {
        fun pluginMetaDataRetrieved(pluginName: String, pluginLogo: Bitmap, pluginPackage: String)
        fun pluginCategoriesRetrieved(categories: ArrayList<String>)
        fun pluginNewsRetrieved(news: ArrayList<NewsResult>)
        fun timeoutOccurred()
        fun serviceConnected()
    }

}

