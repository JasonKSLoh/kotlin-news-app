package com.jason.experiments.kotlinnewsapp.ui.news

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
import com.jason.experiments.kotlinnewsapp.ui.launch.NewsSource
import com.net.learning.kotlinnewspluginlib.NewsResult

/**
 * NewsViewModel
 * Created by jason on 23/7/18.
 */
class NewsViewModel(injectedPluginManager: PluginManager, targetPlugin: String) : ViewModel(), PluginManager.PluginManagerCallbackListener {
    private val pluginManager: PluginManager = injectedPluginManager

    private val categoriesLiveData: MutableLiveData<ArrayList<String>> = MutableLiveData()
    private val newsLiveData: MutableLiveData<ArrayList<NewsResult>> = MutableLiveData()
    private val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val pluginPackage = targetPlugin
    private var selectedCategory: String = ""


    init {
        pluginManager.listener = this
        categoriesLiveData.value = ArrayList()
        newsLiveData.value = ArrayList()
        shouldShowLoading.value = false
    }

    fun onCategorySelected(categoryPosition: Int) {
        categoriesLiveData.value?.let {
            if (it.size > categoryPosition) {
                selectedCategory = it[categoryPosition]
            }
        }
    }

    fun getShouldShowLoadingLiveData(): LiveData<Boolean>{
        return shouldShowLoading
    }

    fun getCategoriesLiveData(): LiveData<ArrayList<String>> {
        return categoriesLiveData
    }

    fun getNewsLiveData(): LiveData<ArrayList<NewsResult>> {
        return newsLiveData
    }

    fun fetchCategories() {
        pluginManager.getPluginCategories(pluginPackage)
    }

    fun fetchNews() {
        pluginManager.getPluginNews(pluginPackage, selectedCategory)
        shouldShowLoading.postValue(true)
    }

    override fun onPluginMetaDataFetched(pluginMetaData: ArrayList<NewsSource>) {
        //Do Nothing
    }

    override fun onPluginCategoriesFetched(pluginCategories: ArrayList<String>) {
        categoriesLiveData.postValue(pluginCategories)
    }

    override fun onPluginNewsFetched(pluginNews: ArrayList<NewsResult>) {
        newsLiveData.postValue(pluginNews)
        shouldShowLoading.postValue(false)
    }
}

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(private val pluginManager: PluginManager, private val targetPlugin: String) :
        ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(pluginManager, targetPlugin) as T
    }
}