package com.net.learning.nytplugin

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import java.io.ByteArrayOutputStream

/**
 * MessengerService
 * Created by jason on 23/7/18.
 */
class MessengerService : Service() {
    companion object {
        var DEFAULT_KEY = "default_key"

        const val MSG_GET_METADATA = 1
        const val MSG_GET_CATEGORIES = 2
        const val MSG_GET_NEWS = 3

        const val KEY_NEWS_SRC = "key_news_src"
        const val KEY_NEWS_LOGO = "key_news_logo"
        const val KEY_NEWS_CATEGORIES = "key_news_categories"
        const val KEY_NEWS_LIST = "key_news_list"
        const val KEY_CATEGORY = "key_category"
    }

    private val messenger = Messenger(IncomingHandler())
    internal var isRunning = false

    override fun onBind(intent: Intent): IBinder? {
        return messenger.binder
    }

    @SuppressLint("HandlerLeak")
    inner class IncomingHandler : Handler() {

        override fun handleMessage(msg: Message) {
            val msgType = msg.what
            val data = msg.data


            when (msgType) {
                MSG_GET_METADATA -> {
                    sendMetaReply(msg.replyTo)
                }
                MSG_GET_CATEGORIES -> {
                    sendCategoriesReply(msg.replyTo)
                }
                MSG_GET_NEWS -> {
                    val category = data.getString(KEY_CATEGORY)
                    sendNewsReply(msg.replyTo, category)
                }
                else -> {
                    sendReply(msg.replyTo)
                }
            }

        }


        private fun sendReply(messenger: Messenger) {
            try {
                val bundle = Bundle()
                bundle.putString("REPLY", "This is a reply")
                val reply = Message.obtain(null, 0)
                reply.data = bundle
                messenger.send(reply)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        private fun sendMetaReply(messenger: Messenger) {
            try {
                val bundle = Bundle()
                val reply = Message.obtain(null, MSG_GET_METADATA)
                reply.data = bundle
                bundle.putString(KEY_NEWS_SRC, NewsConsts.PLUGIN_TITLE)
                val byteArrayOutputStream = ByteArrayOutputStream()
                val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.nty_logo)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                bundle.putByteArray(KEY_NEWS_LOGO, byteArrayOutputStream.toByteArray())

                messenger.send(reply)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        private fun sendCategoriesReply(messenger: Messenger) {
            try {
                val bundle = Bundle()
                val reply = Message.obtain(null, MSG_GET_CATEGORIES)
                reply.data = bundle
                val categoriesList = NewsConsts.CATEGORIES
                val categoriesArrayList = ArrayList<String>()
                categoriesArrayList.addAll(categoriesList)

                bundle.putStringArrayList(KEY_NEWS_CATEGORIES, categoriesArrayList)
                messenger.send(reply)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        private fun sendNewsReply(messenger: Messenger, category: String) {
            if (category.isEmpty()) {
                try {
                    val bundle = Bundle()
                    val reply = Message.obtain(null, MSG_GET_NEWS)
                    reply.data = bundle
                    bundle.putSparseParcelableArray(KEY_NEWS_LIST, null)
                    messenger.send(reply)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }

            val apiHandler = ApiHandler()
            apiHandler.fetchTopStories(category)
                    .subscribe({
                                   val newsList = ArrayList<Array<String>>()
                                   it.results?.let {
                                       for (newsResult in it) {
                                           newsList.add(newsResult.toNewsResultStringArray())
                                       }
                                   }
                                   try {
                                       val bundle = Bundle()
                                       val reply = Message.obtain(null, MSG_GET_NEWS)
                                       reply.data = bundle
                                       bundle.putSerializable(KEY_NEWS_LIST, newsList)
                                       messenger.send(reply)
                                   } catch (e: RemoteException) {
                                       e.printStackTrace()
                                   }
                               }, {
                                   Log.e("+_", it.message, it)
                               })
        }
    }

}