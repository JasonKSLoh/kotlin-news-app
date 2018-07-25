package com.net.learning.kotlinnewspluginlib

import android.content.Context
import android.graphics.Bitmap
import android.os.*
import android.util.Log
import java.io.ByteArrayOutputStream

abstract class PluginIpcHandler(val context: Context, val newsManager: NewsManager): Handler(){

    final override fun handleMessage(msg: Message) {
        val msgType = msg.what
        val data = msg.data

        when (msgType) {
            PluginConsts.MSG_GET_METADATA -> {
                sendMetaReply(msg.replyTo)
            }
            PluginConsts.MSG_GET_CATEGORIES -> {
                sendCategoriesReply(msg.replyTo)
            }
            PluginConsts.MSG_GET_NEWS -> {
                val category = data.getString(PluginConsts.KEY_CATEGORY)
                sendNewsReply(msg.replyTo, category)
            }
            else -> {
                sendReply(msg.replyTo)
            }
        }

    }


    private final fun sendReply(messenger: Messenger) {
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

    private final fun sendMetaReply(messenger: Messenger) {
        try {
            val reply = Message.obtain(null, PluginConsts.MSG_GET_METADATA)
            val bundle = getMetaReplyBundle()
            reply.data = bundle

            messenger.send(reply)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun putMetaInfoInBundle(bundle: Bundle, pluginTitle: String, pluginLogo: Bitmap){
        bundle.putString(PluginConsts.KEY_NEWS_SRC, pluginTitle)
        val byteArrayOutputStream = ByteArrayOutputStream()
        pluginLogo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        bundle.putByteArray(PluginConsts.KEY_NEWS_LOGO, byteArrayOutputStream.toByteArray())
    }

    abstract fun getMetaReplyBundle(): Bundle

    private final fun sendCategoriesReply(messenger: Messenger) {
        try {
            val reply = Message.obtain(null, PluginConsts.MSG_GET_CATEGORIES)
            val bundle = getCategoriesBundle()
            reply.data = bundle
            messenger.send(reply)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun putCategoryInfoInBundle(bundle: Bundle, categories: ArrayList<String>){
        bundle.putStringArrayList(PluginConsts.KEY_NEWS_CATEGORIES, categories)
    }

    abstract fun getCategoriesBundle(): Bundle

    private final fun sendNewsReply(messenger: Messenger, category: String) {
        if (category.isEmpty()) {
            try {
                val bundle = Bundle()
                val reply = Message.obtain(null, PluginConsts.MSG_GET_NEWS)
                reply.data = bundle
                bundle.putSerializable(PluginConsts.KEY_NEWS_LIST, ArrayList<Array<String>>())
                messenger.send(reply)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        newsManager.fetchTopStories(category)
                .subscribe({
                               val newsList = newsManager.getNewsResultsStringArrays(it)
                               try {
                                   val reply = Message.obtain(null, PluginConsts.MSG_GET_NEWS)
                                   val bundle = Bundle()
                                   bundle.putSerializable(PluginConsts.KEY_NEWS_LIST, newsList)
                                   reply.data = bundle
                                   messenger.send(reply)
                               } catch (e: RemoteException) {
                                   e.printStackTrace()
                               }
                           }, {
                               Log.e("+_", it.message, it)
                           })
    }
}