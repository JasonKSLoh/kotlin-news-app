package com.jason.experiments.kotlinnewsapp.ui.launch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
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
 * LaunchViewModelTest
 * Created by jason on 26/7/18.
 */
class LaunchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var pluginManager: PluginManager

    lateinit var launchViewModel: LaunchViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        launchViewModel = LaunchViewModel(pluginManager)
    }

    @Test
    fun getNewsSources() {
        val newsSources = launchViewModel.getNewsSources()
        Assert.assertNotNull(newsSources.value)
        Assert.assertTrue(newsSources.value!!.isEmpty())
    }

    @Test
    fun fetchPluginMetaData() {
        launchViewModel.fetchPluginMetaData()
        Mockito.verify(pluginManager).getPluginMetaData()
    }

    @Test
    fun onPluginMetaDataFetched() {
        val testList = ArrayList<NewsSource>()
        launchViewModel.onPluginMetaDataFetched(testList)
        Assert.assertEquals(testList, launchViewModel.getNewsSources().value)
    }

    @Test
    fun onPluginCategoriesFetched() {
    }

    @Test
    fun onPluginNewsFetched() {
    }

    @Test
    fun observerTest() {
        val mockObserver = spyk<Observer<ArrayList<NewsSource>>>()
        val testNewsSources = ArrayList<NewsSource>()
        val lvmArray = launchViewModel.getNewsSources().value
        launchViewModel.getNewsSources().observeForever(mockObserver)
        verify(exactly = 1) { mockObserver.onChanged(lvmArray)}
        launchViewModel.onPluginMetaDataFetched(testNewsSources)
        verify(exactly = 2) { mockObserver.onChanged(testNewsSources)}
    }
}