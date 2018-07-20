package com.jason.experiments.kotlinnewsapp.network

import com.jason.experiments.kotlinnewsapp.model.NewsResult

/**
 * NewsRepo
 * Created by jason on 20/7/18.
 */
interface NewsRepo{
    fun fetchNews(): List<NewsResult>
}

interface RemoteNewsRepo: NewsRepo {

}

interface LocalNewsRepo: NewsRepo {
    fun cacheNews(news: List<NewsResult>)
}