package com.jason.experiments.kotlinnewsapp.builtinplugin

import com.google.gson.Gson
import com.jason.experiments.kotlinnewsapp.BuildConfig
import com.jason.experiments.kotlinnewsapp.builtinplugin.newsapimodel.NewsApiResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * ApiHandler
 * Created by jason on 24/7/18.
 */
class ApiHandler {

    fun fetchTopStories(category: String): Observable<NewsApiResponse> {
        val url = NewsConsts.API_ENDPOINT +
                "category=$category&" +
                "pageSize=100&" +
                "apiKey=" + BuildConfig.API_KEY

        val client = OkHttpClient.Builder()
                .connectTimeout(NewsConsts.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NewsConsts.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NewsConsts.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        return Observable.fromCallable<Any> { client.newCall(request).execute() }
                .map<NewsApiResponse> { m  ->
                    val body = (m as Response).body()?.string()
                    val gson = Gson()
                    val result:NewsApiResponse = gson.fromJson(body, NewsApiResponse::class.java)
                    result
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { it.printStackTrace() }
    }
}