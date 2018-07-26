package com.jason.experiments.kotlinnewsapp.ui.web

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.network.NetworkConsts
import kotlinx.android.synthetic.main.activity_web.*

/**
 * WebActivity
 * Created by jason on 19/3/18.
 */
class WebActivity : AppCompatActivity() {

    private lateinit var wvWeb: WebView
    lateinit var toolbar: Toolbar
    lateinit var pgLoadingWeb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        wvWeb = wv_web
        toolbar = toolbar_wv
        pgLoadingWeb = pg_loading_web
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupWebView()
        val newsUrl = intent.extras.getString(NetworkConsts.EXTRA_WV_URL, NetworkConsts.WV_URL_DEFAULT)
        wvWeb.loadUrl(newsUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val wvSettings = wvWeb.settings
        wvSettings.javaScriptEnabled = true
        wvWeb.isVerticalScrollBarEnabled = true
        wvSettings.cacheMode = WebSettings.LOAD_DEFAULT
        wvWeb.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                pgLoadingWeb.progress = newProgress
                if (newProgress == 100) {
                    pgLoadingWeb.visibility = View.GONE
                } else {
                    pgLoadingWeb.visibility = View.VISIBLE
                }
            }
        }
        wvWeb.webViewClient = object : WebViewClient() {
            @Suppress("OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view?.loadUrl(request?.url?.toString())
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                Log.d("+_", "On Home Selected")
                super.onBackPressed()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (wvWeb.canGoBack()) {
            wvWeb.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        wvWeb.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        wvWeb.restoreState(savedInstanceState)
    }

}