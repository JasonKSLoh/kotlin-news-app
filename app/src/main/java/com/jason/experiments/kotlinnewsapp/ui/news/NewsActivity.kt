package com.jason.experiments.kotlinnewsapp.ui.news

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import android.widget.Toast
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
import com.jason.experiments.kotlinnewsapp.util.addFragment
import kotlinx.android.synthetic.main.activity_news.*
import toothpick.Toothpick
import javax.inject.Inject

/**
 * NewsActivity
 * Created by jason on 23/7/18.
 */
class NewsActivity: AppCompatActivity(){
    companion object {
        private const val TAG_NEWS_FRAGMENT = "tag_news_fragment"
    }

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsFragment: NewsFragment
    private lateinit var tvNewsTitle: TextView

    @Inject
    lateinit var pluginManager: PluginManager

    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        tvNewsTitle = tv_news_title
        setupToothpick()
        val targetPlugin = intent.getStringExtra(PluginManager.KEY_PACKAGE_NAME)
        val newsTitle = intent.getStringExtra(PluginManager.KEY_PLUGIN_NAME)
        tvNewsTitle.text = newsTitle
        if(targetPlugin.isNullOrBlank()){
            Toast.makeText(this, "Plugin package not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        newsViewModel = ViewModelProviders.of(this, NewsViewModelFactory(pluginManager, targetPlugin)).get(NewsViewModel::class.java)
        setupFragments()
    }

    private fun setupToothpick() {
        val scope = Toothpick.openScopes(application, this)
        Toothpick.inject(this, scope)
    }


    private fun setupFragments() {
        val retrievedFragment = supportFragmentManager.findFragmentByTag(TAG_NEWS_FRAGMENT)
        if(retrievedFragment != null){
            newsFragment = retrievedFragment as NewsFragment
        } else {
            newsFragment = NewsFragment.newInstance()
            addFragment(newsFragment, R.id.news_container, TAG_NEWS_FRAGMENT)
        }
    }
}

