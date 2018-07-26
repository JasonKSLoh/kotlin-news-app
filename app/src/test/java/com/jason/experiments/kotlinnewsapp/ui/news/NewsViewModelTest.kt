package com.jason.experiments.kotlinnewsapp.ui.news

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
import com.net.learning.kotlinnewspluginlib.NewsResult
import io.mockk.spyk
import io.mockk.verify
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
 * Created by jason on 26/7/18.
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
    fun setup() {
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

    @Test
    fun newsObserverTest() {
        val mockNewsObserver = spyk<Observer<ArrayList<NewsResult>>>()
        val testNews = ArrayList<NewsResult>()

        val nvmNews = newsViewModel.getNewsLiveData().value
        newsViewModel.getNewsLiveData().observeForever(mockNewsObserver)
        verify(exactly = 1) { mockNewsObserver.onChanged(nvmNews) }
        newsViewModel.onPluginNewsFetched(testNews)
        verify(exactly = 2) { mockNewsObserver.onChanged(testNews) }
    }

    @Test
    fun categoriesObserverTest() {
        val mockCategoriesObserver = spyk<Observer<ArrayList<String>>>()
        val testCategories = ArrayList<String>()
        val nvmCategories = newsViewModel.getCategoriesLiveData().value

        newsViewModel.getCategoriesLiveData().observeForever(mockCategoriesObserver)
        verify(exactly = 1) { mockCategoriesObserver.onChanged(nvmCategories) }
        newsViewModel.onPluginCategoriesFetched(testCategories)
        verify(exactly = 2) { mockCategoriesObserver.onChanged(testCategories) }
    }

    @Test
    fun progressObserverTest() {
        val mockProgressObserver = spyk<Observer<Boolean>>()
        val nvmProgress = newsViewModel.getShouldShowLoadingLiveData().value

        val testNews = ArrayList<NewsResult>()
        newsViewModel.getShouldShowLoadingLiveData().observeForever(mockProgressObserver)
        verify(exactly = 1) { mockProgressObserver.onChanged(nvmProgress) }
        newsViewModel.fetchNews()
        verify(exactly = 1) { mockProgressObserver.onChanged(true) }
        newsViewModel.onPluginNewsFetched(testNews)
        verify(exactly = 2) { mockProgressObserver.onChanged(false) }
    }


}