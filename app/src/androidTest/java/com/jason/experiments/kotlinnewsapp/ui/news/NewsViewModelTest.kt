package com.jason.experiments.kotlinnewsapp.ui.news

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
import com.net.learning.kotlinnewspluginlib.NewsResult
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit

/**
 * NewsViewModelTest
 * Created by jason on 25/7/18.
 */
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var pluginManager: PluginManager

    lateinit var newsViewModel: NewsViewModel

    val mockPackageName = "MockPackage"

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        newsViewModel = NewsViewModel(pluginManager, mockPackageName)
    }

    @Test
    fun onPluginCategoriesFetched() {
        val categories = arrayListOf<String>("Cat1", "Cat2", "Cat3")
        newsViewModel.onPluginCategoriesFetched(categories)
        Assert.assertEquals(categories, newsViewModel.getCategoriesLiveData().value)
    }

    @Test
    fun onPluginNewsFetched() {
        val newsResults = ArrayList<NewsResult>()
        newsViewModel.onPluginNewsFetched(newsResults)
        Assert.assertEquals(newsViewModel.getNewsLiveData().value, newsResults)
    }

    @Test
    fun fetchCategories() {
        newsViewModel.fetchCategories()
        Mockito.verify(pluginManager).getPluginCategories(mockPackageName)
    }

    @Test
    fun fetchNews() {
        val categories = arrayListOf<String>("Cat1", "Cat2", "Cat3")
        newsViewModel.onPluginCategoriesFetched(categories)
        newsViewModel.onCategorySelected(1)


        newsViewModel.fetchNews()
        Mockito.verify(pluginManager).getPluginNews(mockPackageName, "Cat2")
        Assert.assertEquals(true, newsViewModel.getShouldShowLoadingLiveData().value)
    }



}