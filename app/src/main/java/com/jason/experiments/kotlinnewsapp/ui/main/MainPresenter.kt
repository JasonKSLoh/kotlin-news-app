package com.jason.experiments.kotlinnewsapp.ui.main

import android.util.Log
import com.jason.experiments.kotlinnewsapp.model.NewsResult
import com.jason.experiments.kotlinnewsapp.network.ApiHandler
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * MainPresenter
 * Created by jason on 8/3/18.
 */
class MainPresenter(parent: MainContract.ParentView, primary:MainContract.PrimaryView): MainContract.Presenter {

    var compositeDisposable = CompositeDisposable()
    var primaryView = WeakReference<MainContract.PrimaryView>(primary)
    var parentView = WeakReference<MainContract.ParentView>(parent)


    override fun primaryViewCreated() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun primaryViewDestroyed() {
        compositeDisposable.clear()
    }



    override fun onSearchButtonClicked(category: String) {
        compositeDisposable.clear()
        if (primaryView.get()?.canFetchNews() != true){
            return
        }
        primaryView.get()?.showProgressIndicator(true)
        val disposable = ApiHandler().fetchTopStories(category).subscribe({r ->
            val results = r.results
            if(results != null){
                primaryView.get()?.updateNews(results)
            } else {
                Log.d("+_", "Empty list added")
                val emptyResult = ArrayList<NewsResult>()
                emptyResult.add(NewsResult())
            }
            primaryView.get()?.showProgressIndicator(false)
        })
        compositeDisposable.add(disposable)
    }
}