package com.jason.experiments.kotlinnewsapp.ui.launch.epoxy

import android.content.Intent
import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
import com.jason.experiments.kotlinnewsapp.ui.launch.NewsSource
import com.jason.experiments.kotlinnewsapp.ui.news.NewsActivity

/**
 * PluginsEpoxyController
 * Created by jason on 27/7/18.
 */
open class PluginsEpoxyController: TypedEpoxyController<ArrayList<NewsSource>>() {

    override fun buildModels(newsSources: ArrayList<NewsSource>?) {
        val numPlugins = newsSources?.size ?: 0

        pluginHeader {
            id("Header")
            numPlugins(numPlugins)
        }
        newsSources?.let {
            for(newsSource in it){
                val onClickListener = View.OnClickListener {
                    val intent = Intent(it.context, NewsActivity::class.java)
                    intent.putExtra(PluginManager.KEY_PACKAGE_NAME, newsSource.pluginPackage)
                    intent.putExtra(PluginManager.KEY_PLUGIN_NAME, newsSource.title)
                    it.context.startActivity(intent)
                }

                plugins{
                    id(newsSource.title)
                    newsSource(newsSource)
                    onClickListener(onClickListener)
                }
            }
        }
    }
}