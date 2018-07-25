package com.net.learning.nytplugin

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Messenger
import com.net.learning.kotlinnewspluginlib.NewsManager
import com.net.learning.kotlinnewspluginlib.PluginIpcHandler
import com.net.learning.kotlinnewspluginlib.PluginMessengerService

/**
 * NytMessengerService
 * Created by jason on 23/7/18.
 */
class NytMessengerService: PluginMessengerService() {
    override fun setIpcHandlerForMessenger() {
        messenger = Messenger(IncomingHandler(applicationContext, NytNewsManager()))
    }
}

class IncomingHandler constructor(context: Context, newsManager: NewsManager): PluginIpcHandler(context, newsManager) {
    override fun getMetaReplyBundle(): Bundle {
        val bundle = Bundle()
        putMetaInfoInBundle(bundle,
                            NewsConsts.PLUGIN_TITLE,
                            BitmapFactory.decodeResource(context.resources, R.drawable.nyt_logo))
        return bundle
    }

    override fun getCategoriesBundle(): Bundle {
        val bundle = Bundle()
        putCategoryInfoInBundle(bundle,
                                NewsConsts.CATEGORIES)
        return bundle
    }
}