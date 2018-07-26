package com.jason.experiments.kotlinnewsapp.ui.launch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.plugin.PluginManager
import kotlinx.android.synthetic.main.activity_launch.*
import toothpick.Toothpick
import javax.inject.Inject

/**
 * LaunchActivity
 * Created by jason on 20/7/18.
 */
class LaunchActivity : AppCompatActivity(){
    private lateinit var rvNewsSources: RecyclerView
    private lateinit var newsSourceAdapter: NewsSourceAdapter
    private lateinit var tvNoPlugins: TextView

    private lateinit var launchViewModel: LaunchViewModel

    @Inject
    lateinit var pluginManager: PluginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        setupToothpick()
        tvNoPlugins = tv_no_plugins
        launchViewModel = ViewModelProviders.of(this, LaunchViewModelFactory(pluginManager)).get(LaunchViewModel::class.java)

        setupRecyclerView()
        setupObservers()
        launchViewModel.fetchPluginMetaData()
    }

    private fun setupToothpick() {
        val scope = Toothpick.openScopes(application, this)
        Toothpick.inject(this, scope)
    }

    private fun setupObservers(){
        val newsSourceObserver = Observer<ArrayList<NewsSource>>{
            it?.let {
                newsSourceAdapter.setNewsSources(it)
                if(it.isEmpty()){
                    tvNoPlugins.visibility = View.VISIBLE
                } else {
                    tvNoPlugins.visibility = View.GONE
                }
            }
        }
        launchViewModel.getNewsSources().observe(this, newsSourceObserver)
    }


    private fun setupRecyclerView() {
        rvNewsSources = rv_news_sources
        newsSourceAdapter = NewsSourceAdapter(launchViewModel.getNewsSources().value!!)
        rvNewsSources.layoutManager = LinearLayoutManager(baseContext)
        rvNewsSources.adapter = newsSourceAdapter
    }


}

