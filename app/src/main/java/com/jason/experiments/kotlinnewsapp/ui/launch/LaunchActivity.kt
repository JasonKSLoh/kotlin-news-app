package com.jason.experiments.kotlinnewsapp.ui.launch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.jason.experiments.kotlinnewsapp.R
import kotlinx.android.synthetic.main.activity_launch.*

/**
 * LaunchActivity
 * Created by jason on 20/7/18.
 */
class LaunchActivity(): AppCompatActivity(){

    lateinit var rvNewsSources: RecyclerView
    lateinit var newsSourceAdapter: NewsSourceAdapter
//    val newsSources = NewsSource.values().toList()
    val newsSources = ArrayList<NewsSource>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        newsSources.add(NewsSource.NYT)
        setupRecyclerView()

    }


    private fun setupRecyclerView(){
        rvNewsSources = rv_news_sources
        newsSourceAdapter = NewsSourceAdapter(newsSources)
        rvNewsSources.layoutManager = LinearLayoutManager(baseContext)
        rvNewsSources.adapter = newsSourceAdapter
        newsSourceAdapter.notifyDataSetChanged()
        Log.d("+_", "Set up recycler view")
    }

}