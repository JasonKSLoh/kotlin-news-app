package com.jason.experiments.kotlinnewsapp.plugin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.jason.experiments.kotlinnewsapp.BuildConfig
import com.jason.experiments.kotlinnewsapp.ui.launch.NewsSource
import com.net.learning.kotlinnewspluginlib.NewsResult
import com.net.learning.kotlinnewspluginlib.PluginConsts
import javax.inject.Inject

/**
 * PluginManager
 * Created by jason on 23/7/18.
 */
class PluginManager : PluginConnector.PluginConnectorCallbackListener {
    companion object {
        private const val LOG_TAG = "+_PlgMgr"
        const val ACTION_GET_PLUGIN = BuildConfig.APPLICATION_ID + ".action_get_plugin"
        const val KEY_PACKAGE_NAME = "key_package_name"
        const val KEY_PLUGIN_NAME = "key_plugin_name"
    }

    @Inject
    lateinit var context: Context

    private var currentMsgFunction = 0

    private lateinit var plugins: ArrayList<String>
    private val newsSources = ArrayList<NewsSource>()
    private var pluginConnector: PluginConnector? = null
    private var pluginIndex = 0
    private var requestedNewsCategory = ""
    private var isActive = false

    var listener: PluginManagerCallbackListener? = null



    private fun getListOfPlugins(): ArrayList<String> {
        val plugins = ArrayList<String>()
        val packageManager = context.packageManager
        val baseIntent = Intent(ACTION_GET_PLUGIN)

        val resolveInfoList = packageManager.queryIntentServices(baseIntent, PackageManager.GET_RESOLVED_FILTER)
        for (resolveInfo in resolveInfoList) {
            val packageName = resolveInfo?.serviceInfo?.packageName
            packageName?.let {
                plugins.add(packageName)
            }
        }
        return plugins
    }


    fun getPluginMetaData() {
        if(isActive){
            return
        }
        isActive = true
        plugins = getListOfPlugins()
        newsSources.clear()
        getNextPluginInfo()
    }

    fun getPluginCategories(pluginPackage: String) {
        if(isActive){
            return
        }
        isActive = true
        currentMsgFunction = PluginConsts.MSG_GET_CATEGORIES
        pluginConnector?.unbindService()
        pluginConnector = PluginConnector(context, pluginPackage, this)
        pluginConnector?.bindService()
    }

    fun getPluginNews(pluginPackage: String, category: String) {
        if(isActive){
            return
        }
        isActive = true
        currentMsgFunction = PluginConsts.MSG_GET_NEWS
        requestedNewsCategory = category
        pluginConnector?.unbindService()
        pluginConnector = PluginConnector(context, pluginPackage, this)
        pluginConnector?.bindService()
    }

    private fun getNextPluginInfo() {
        pluginConnector?.unbindService()
        val pluginsIterator = plugins.listIterator(pluginIndex)
        if (pluginsIterator.hasNext()) {
            val packageName = pluginsIterator.next()
            currentMsgFunction = PluginConsts.MSG_GET_METADATA
            pluginConnector = PluginConnector(context, packageName, this)
            pluginConnector?.bindService()
            pluginIndex++
        } else {
            pluginIndex = 0
            isActive = false
            listener?.onPluginMetaDataFetched(newsSources)
        }
    }

    override fun pluginMetaDataRetrieved(pluginName: String, pluginLogo: Bitmap, pluginPackage: String) {
        getNextPluginInfo()
        newsSources.add(NewsSource(pluginName, pluginLogo, pluginPackage))
    }

    override fun pluginCategoriesRetrieved(categories: ArrayList<String>) {
        isActive = false
        listener?.onPluginCategoriesFetched(categories)
    }

    override fun pluginNewsRetrieved(news: ArrayList<NewsResult>) {
        isActive = false
        listener?.onPluginNewsFetched(news)
    }

    override fun timeoutOccurred() {
        isActive = false
        Log.d(LOG_TAG, "Timeout Occurred")
        if(currentMsgFunction == PluginConsts.MSG_GET_METADATA){
            getNextPluginInfo()
        }
        if(currentMsgFunction == PluginConsts.MSG_GET_NEWS) {
            Toast.makeText(context, "Timeout Occurred while attempting to fetch news", Toast.LENGTH_SHORT).show()
            listener?.onPluginNewsFetched(ArrayList())
        }
    }

    override fun serviceConnected() {
        when (currentMsgFunction) {
            PluginConsts.MSG_GET_METADATA -> {
                pluginConnector?.getMetaData()
            }
            PluginConsts.MSG_GET_CATEGORIES -> {
                pluginConnector?.getCategories()
            }
            PluginConsts.MSG_GET_NEWS -> {
                pluginConnector?.getNews(requestedNewsCategory)
            }
            else -> {

            }
        }
    }


    interface PluginManagerCallbackListener {
        fun onPluginMetaDataFetched(pluginMetaData: ArrayList<NewsSource>)
        fun onPluginCategoriesFetched(pluginCategories: ArrayList<String>)
        fun onPluginNewsFetched(pluginNews: ArrayList<NewsResult>)
    }
}