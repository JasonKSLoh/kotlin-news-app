package com.jason.experiments.kotlinnewsapp.ui.main

import com.jason.experiments.kotlinnewsapp.model.NytNewsResult

/**
 * MainContract
 * Created by jason on 8/3/18.
 */
class MainContract {
    public interface Presenter{
        fun onSearchButtonClicked(category: String)
        fun primaryViewCreated()
        fun primaryViewDestroyed()
    }

    public interface PrimaryView{
        fun showProgressIndicator(shouldShow: Boolean)
        fun attachPresenter(pres: MainContract.Presenter)
        fun updateNews(fetchedResults: List<NytNewsResult>)
        fun canFetchNews() : Boolean
        fun retrievePresenter(): Presenter
    }

    public interface ParentView{
    }
}