package com.net.learning.nytplugin

/**
 * NewsConsts
 * Created by jason on 24/7/18.
 */
object NewsConsts {
    public val CATEGORIES = arrayOf("home"
                                              , "opinion"
                                              , "world"
                                              , "national"
                                              , "politics"
                                              , "upshot"
                                              , "nyregion"
                                              , "business"
                                              , "technology"
                                              , "science"
                                              , "health"
                                              , "sports"
                                              , "arts"
                                              , "books"
                                              , "movies"
                                              , "theater"
                                              , "sundayreview"
                                              , "fashion"
                                              , "tmagazine"
                                              , "food"
                                              , "travel"
                                              , "magazine"
                                              , "realestate"
                                              , "automobiles"
                                              , "obituaries"
                                              , "insider")


    const val API_ENDPOINT = "https://api.nytimes.com/svc/topstories/v2/"
    const val API_KEY = BuildConfig.API_KEY
    const val DEFAULT_TIMEOUT = 30000L

    const val PLUGIN_TITLE = "New York Times"
}