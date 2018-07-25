package com.net.learning.kotlinnewspluginlib

import io.reactivex.Observable

/**
 * NewsManager
 * Created by jason on 25/7/18.
 */
interface NewsManager {
    /**
     * Takes in a category and returns an observable which can return the news response from a particular news source
     *
     * @param category The category of news to fetch
     * @return An observable that gets the news response(e.g. a JSON response from the news source's REST API)
     */
    fun fetchTopStories(category: String): Observable<*>

    /**
     * Converts the api response from the observable into an ArrayList of String arrays that represent a NewsResult Object
     *
     * @param apiResponse The api response from the Observable
     * @return An ArrayList of String Arrays.
     *
     * Each String array should consist of 7 strings, with each string representing the constructor params of a NewsResult object, in order
     */
    fun getNewsResultsStringArrays(apiResponse: Any): ArrayList<Array<String>>

}