package com.jason.experiments.kotlinnewsapp.ui.launch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
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
 * Created by jason on 25/7/18.
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
    fun setup(){
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
}