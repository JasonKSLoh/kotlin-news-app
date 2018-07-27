package com.jason.experiments.kotlinnewsapp.ui.launch

import android.app.Instrumentation
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import com.jason.experiments.kotlinnewsapp.builtinplugin.NewsConsts
import com.jason.experiments.kotlinnewsapp.util.MiscUtils
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UiFlowTest
 * Created by jason on 26/7/18.
 */
@RunWith(AndroidJUnit4::class)
class UiFlowTest {
    private lateinit var uiDevice: UiDevice
    private lateinit var instrumentation: Instrumentation

    @get:Rule
    val activityRule = ActivityTestRule<LaunchActivity>(LaunchActivity::class.java)

    @Before
    fun setup() {
        instrumentation = InstrumentationRegistry.getInstrumentation()
        uiDevice = UiDevice.getInstance(instrumentation)
        uiDevice.waitForIdle()
    }

    @Test
    fun findPlugins() {
        uiDevice.waitForWindowUpdate(null, 550)
        val builtinPluginText = uiDevice.findObject(UiSelector()
                                                            .textContains(NewsConsts.PLUGIN_TITLE)
                                                            .resourceId("com.jason.experiments.kotlinnewsapp:id/tv_newssource_name"))
        val builtinPluginImg = uiDevice.findObject(UiSelector()
                                                           .className("android.widget.ImageView")
                                                           .resourceId("com.jason.experiments.kotlinnewsapp:id/iv_newssource_logo"))
        if (!builtinPluginText.exists()
                || !builtinPluginImg.exists()) {
            Assert.fail()
        }

        builtinPluginImg.click()
        checkNewsActivity()
    }

    private fun checkNewsActivity() {
        uiDevice.waitForIdle()
        val titleText = uiDevice.findObject(UiSelector().textContains(NewsConsts.PLUGIN_TITLE))
        val categoryLabel = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/tv_news_category_label"))
        val spinner = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/spinner_news_category"))
        val searchButton = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/iv_news_search_icon"))
        val recyclerView = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/rv_news"))

        if (!titleText.exists()
                || !categoryLabel.exists()
                || !spinner.exists()
                || !searchButton.exists()
                || !recyclerView.exists()) {
            Assert.fail()
        }
        if (MiscUtils.isNetworkAvailable(instrumentation.context)) {
            checkFetchNews()
        }
    }

    private fun checkFetchNews() {
        uiDevice.waitForIdle()
        val searchButton = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/iv_news_search_icon"))
        searchButton.click()
        uiDevice.waitForIdle()
        val progressBar = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/progressbar_loading"))
        if (!progressBar.exists()) {
            Assert.fail()
        }
        uiDevice.waitForWindowUpdate(null, 30000)
        val noProgressBar = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/progressbar_loading"))
        if (noProgressBar.exists()) {
            Assert.fail()
        }
        val newsItem = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/tv_title"))
        if (!newsItem.exists()) {
            Assert.fail()
        }
        newsItem.click()
        checkWebView()
    }

    private fun checkWebView() {
        uiDevice.waitForIdle()
        uiDevice.waitForWindowUpdate(null, 5000)
        val toolbar = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/toolbar_wv"))
        val webView = uiDevice.findObject(UiSelector().resourceId("com.jason.experiments.kotlinnewsapp:id/wv_web"))

        if (!toolbar.exists()) {
            Assert.fail()
        }
        if(!webView.exists()) {
            Assert.fail()
        }

    }


}