package com.jason.experiments.kotlinnewsapp.network

import com.google.gson.Gson
import com.jason.experiments.kotlinnewsapp.BuildConfig
import com.jason.experiments.kotlinnewsapp.model.NytNewsResponse
import com.jason.experiments.kotlinnewsapp.util.ApiConsts
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * ApiHandler
 * Created by jason on 8/3/18.
 */
class ApiHandler {

    fun fetchTopStories(category: String): Observable<NytNewsResponse>{
        val url = ApiConsts.API_ENDPOINT + category + ".json?api-key=" + BuildConfig.API_KEY
        val client = OkHttpClient.Builder()
                .connectTimeout(NetworkConsts.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetworkConsts.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetworkConsts.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build()

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        return Observable.fromCallable<Any> { client.newCall(request).execute() }
                .map<NytNewsResponse> { m  ->
                    val body = (m as Response).body()?.string()
                    val gson = Gson()
                    val result:NytNewsResponse = gson.fromJson(body, NytNewsResponse::class.java)
                    result
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Consumer<Throwable> { it.printStackTrace() })
    }

}
