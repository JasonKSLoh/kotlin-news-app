package com.jason.experiments.kotlinnewsapp.builtinplugin

/**
 * NewsConsts
 * Created by jason on 24/7/18.
 */
object NewsConsts {
    val CATEGORIES = arrayListOf<String>("business",
                                   "entertainment",
                                   "general",
                                   "health",
                                   "science",
                                   "sports",
                                   "technology")
    const val API_ENDPOINT = "https://newsapi.org/v2/top-headlines?country=us&"
    const val DEFAULT_TIMEOUT = 30000L
    const val PLUGIN_TITLE = "NewsAPI.org"
}