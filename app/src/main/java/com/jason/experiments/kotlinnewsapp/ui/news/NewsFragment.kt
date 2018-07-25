package com.jason.experiments.kotlinnewsapp.ui.news

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import com.jason.experiments.kotlinnewsapp.R
import com.net.learning.kotlinnewspluginlib.NewsResult
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * NewsFragment
 * Created by jason on 23/7/18.
 */
class NewsFragment: Fragment() {
    companion object {
        fun newInstance(): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            fragment.arguments = args
            fragment.retainInstance = true
            return fragment
        }
    }

    private lateinit var spinnerCategories: Spinner
    private lateinit var ivSearchIcon: ImageView
    private lateinit var rvNews: RecyclerView
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressbarloading: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        newsViewModel = ViewModelProviders.of(requireActivity()).get(NewsViewModel::class.java)
        setupObservers()
        newsViewModel.fetchCategories()
    }

    private fun setupObservers(){
        val categoriesObserver = Observer<ArrayList<String>>{
            it?.let{
                val spinnerCategoryAdapter = ArrayAdapter<String>(requireContext(), R.layout.item_spinner, it)
                spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategories.adapter = spinnerCategoryAdapter
            }
        }
        newsViewModel.getCategoriesLiveData().observe(this, categoriesObserver)

        val newsObserver = Observer<ArrayList<NewsResult>> {
            it?.let {
                newsAdapter.setNewsResults(it)
            }
        }
        newsViewModel.getNewsLiveData().observe(this, newsObserver)

        val progressObserver = Observer<Boolean> {
            it?.let {
                if(it){
                    progressbarloading.visibility = View.VISIBLE
                } else{
                    progressbarloading.visibility = View.GONE
                }
            }
        }
        newsViewModel.getShouldShowLoadingLiveData().observe(this, progressObserver)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews(){
        spinnerCategories = spinner_news_category
        ivSearchIcon = iv_news_search_icon
        rvNews = rv_news
        progressbarloading = progressbar_loading

        rvNews.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(ArrayList())
        rvNews.adapter = newsAdapter

        ivSearchIcon.setOnClickListener {
            newsViewModel.onCategorySelected(spinnerCategories.selectedItemPosition)
            newsViewModel.fetchNews()
        }

        ivSearchIcon.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    ivSearchIcon.scaleX = 2f
                    ivSearchIcon.scaleY = 2f
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    ivSearchIcon.scaleX = 1f
                    ivSearchIcon.scaleY = 1f
                }
                else -> {

                }

            }
             false
        }
    }




}