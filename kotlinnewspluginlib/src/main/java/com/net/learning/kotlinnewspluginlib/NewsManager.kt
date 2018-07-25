package com.net.learning.kotlinnewspluginlib

import io.reactivex.Observable

/**
 * NewsManager
 * Created by jason on 25/7/18.
 */
interface NewsManager {
    fun fetchTopStories(category: String): Observable<*>
    fun getNewsResultsStringArrays(apiResponse: Any): ArrayList<Array<String>>
}