package com.jason.experiments.kotlinnewsapp.builtinplugin

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Messenger
import com.jason.experiments.kotlinnewsapp.R
import com.net.learning.kotlinnewspluginlib.NewsManager
import com.net.learning.kotlinnewspluginlib.PluginIpcHandler
import com.net.learning.kotlinnewspluginlib.PluginMessengerService
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

/**
 * NewsApiMessengerService
 * Created by jason on 24/7/18.
 */
class NewsApiMessengerService : PluginMessengerService() {
    @Inject
    lateinit var incomingHandler: PluginIpcHandler

    override fun setIpcHandlerForMessenger(){
        val scope = Toothpick.openScopes(application, this)
        scope.installModules(object : Module() {
            init {
                bind(PluginIpcHandler::class.java).to(IncomingHandler::class.java)
                bind(NewsManager::class.java).toInstance(NewsApiNewsManager())
            }
        })
        Toothpick.inject(this, scope)
        messenger = Messenger(incomingHandler)
    }
}


class IncomingHandler @Inject constructor(context: Context, newsManager: NewsManager): PluginIpcHandler(context, newsManager) {
    override fun getMetaReplyBundle(): Bundle {
        val bundle = Bundle()
        putMetaInfoInBundle(bundle,
                            NewsConsts.PLUGIN_TITLE,
                            BitmapFactory.decodeResource(context.resources, R.drawable.newsapi_plugin_logo))
        return bundle
    }

    override fun getCategoriesBundle(): Bundle {
        val bundle = Bundle()
        putCategoryInfoInBundle(bundle,
                                NewsConsts.CATEGORIES)
        return bundle
    }

}
