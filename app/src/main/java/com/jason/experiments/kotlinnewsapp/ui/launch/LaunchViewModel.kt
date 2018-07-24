package com.jason.experiments.kotlinnewsapp.ui.launch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jason.experiments.kotlinnewsapp.model.NewsResult
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager

/**
 * LaunchViewModel
 * Created by jason on 23/7/18.
 */
class LaunchViewModel(injectedPluginManager: PluginManager) : ViewModel(), PluginManager.PluginManagerCallbackListener {
    private val newsSources = MutableLiveData<ArrayList<NewsSource>>()
    private val pluginManager: PluginManager = injectedPluginManager

    init {
        newsSources.value = ArrayList()
        pluginManager.listener = this
    }

    fun getNewsSources(): LiveData<ArrayList<NewsSource>>{
        return newsSources
    }

    fun fetchPluginMetaData(){
        pluginManager.getPluginMetaData()
    }

    override fun onPluginMetaDataFetched(pluginMetaData: ArrayList<NewsSource>) {
        newsSources.postValue(pluginMetaData)
    }

    override fun onPluginCategoriesFetched(pluginCategories: ArrayList<String>) {
       //Do Nothing
    }

    override fun onPluginNewsFetched(pluginNews: ArrayList<NewsResult>) {
        //Do Nothing
    }
}


class LaunchViewModelFactory(private val pluginManager: PluginManager) :
        ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LaunchViewModel(pluginManager) as T
    }
}
